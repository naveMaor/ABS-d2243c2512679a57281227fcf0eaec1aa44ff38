package admin.adminLoanTables;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Alert;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import servletDTO.admin.AdminLoanObj;
import servletDTO.admin.InnerTableObj;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class InnerTablesRefresher extends TimerTask {

    private final Consumer<InnerTableObj> loanConsumer;
    private final BooleanProperty shouldUpdate;
    private final String loanName;

    public InnerTablesRefresher(Consumer<InnerTableObj> loanConsumer, BooleanProperty shouldUpdate, String loanName) {
        this.loanConsumer = loanConsumer;
        this.shouldUpdate = shouldUpdate;
        this.loanName = loanName;
    }


    private void createInnerTableLoanRequest(String loanName) {

        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(loanName,String.class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.GET_INNER_TABLE_OBJ)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();


        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url load inner table data")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    String jsonOfClientString = null;
                    try {
                        jsonOfClientString = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.body().close();

                    if(response.code()==200){
                        Gson gson = new Gson();
                        InnerTableObj innerTableObj = new Gson().fromJson(jsonOfClientString, InnerTableObj.class);
                        loanConsumer.accept(innerTableObj);
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,jsonOfClientString);
                        alert.showAndWait();
                    }
                });
            }

        });

    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }
        createInnerTableLoanRequest(loanName);
    }
}
