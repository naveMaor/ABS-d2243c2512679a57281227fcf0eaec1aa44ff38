package admin.adminClientTable;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import servletDTO.client.ClientLoansObj;
import util.Constants;
import util.HttpAdminUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ClientTableRefresher extends TimerTask {


    private final Consumer<List<ClientLoansObj>> clientsListConsumer;
    private final BooleanProperty shouldUpdate;
    private int requestNumber;


    public ClientTableRefresher(BooleanProperty shouldUpdate, Consumer<List<ClientLoansObj>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.clientsListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        Request request = new Request.Builder()
                .url(Constants.CLIENTS_INFO)
                .build();

        HttpAdminUtil.runAsync(request, new okhttp3.Callback() {

            @Override
            public void onFailure(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Users Request Ended with failure")
                );
            }

            @Override
            public void onResponse(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if (response.code() == 200) {
                            String jsonArrayOfUsersNames = response.body().string();
                            ClientLoansObj[] clientLoansList = new Gson().fromJson(jsonArrayOfUsersNames, ClientLoansObj[].class);

                            clientsListConsumer.accept(Arrays.asList(clientLoansList));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }


}
