package admin.adminLoanTables;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import servletDTO.admin.AdminLoanObj;
import util.Constants;
import util.HttpAdminUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TablesRefresher extends TimerTask {

    private final Consumer<List<AdminLoanObj>> loanConsumer;
    private final BooleanProperty shouldUpdate;

    public TablesRefresher(Consumer<List<AdminLoanObj>> loanConsumer, BooleanProperty shouldUpdate) {
        this.loanConsumer = loanConsumer;
        this.shouldUpdate = shouldUpdate;
    }

    private void getAdminTablesDataRequest(){
        Request request = new Request.Builder()
                .url(Constants.ADMIN_LOAN_LIST)
                .build();

        HttpAdminUtil.runAsync(request, new okhttp3.Callback() {

            @Override
            public void onFailure(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull IOException e) {
                Platform.runLater(() ->
//                        httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...")
                                //TODO ADD LOGS
                                System.out.println("Users Request Ended with failure...")
                );
            }

            @Override
            public void onResponse(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if (response.code() == 200) {
                            String jsonArrayOfUsersNames = response.body().string();
                            AdminLoanObj[] adminLoanObjsArray = new Gson().fromJson(jsonArrayOfUsersNames, AdminLoanObj[].class);
                            loanConsumer.accept(Arrays.asList(adminLoanObjsArray));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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
        getAdminTablesDataRequest();
    }
}
