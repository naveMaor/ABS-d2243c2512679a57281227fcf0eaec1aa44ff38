package admin.users;


import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import servletDTO.client.ClientBalanceObj;
import util.Constants;
import util.HttpAdminUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UserListRefresher extends TimerTask {

    private final Consumer<List<ClientBalanceObj>> usersListConsumer;
    private final BooleanProperty shouldUpdate;
    private int requestNumber;


    public UserListRefresher(BooleanProperty shouldUpdate, Consumer<List<ClientBalanceObj>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        Request request = new Request.Builder()
                .url(Constants.USERS_LIST)
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
                            ClientBalanceObj[] usersNames = new Gson().fromJson(jsonArrayOfUsersNames, ClientBalanceObj[].class);
                            usersListConsumer.accept(Arrays.asList(usersNames));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
}
