package client.main;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.TimerTask;
import java.util.function.Consumer;

public class YazRefresher extends TimerTask {

    private final Consumer<Integer> yazConsumer;
    private final BooleanProperty shouldUpdate;
    private int requestNumber;


    public YazRefresher(BooleanProperty shouldUpdate, Consumer<Integer> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.yazConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        Request request = new Request.Builder()
                .url(Constants.GET_CURR_YEZ)
                .build();

        HttpClientUtil.runAsync(request, new okhttp3.Callback() {

            @Override
            public void onFailure(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull IOException e) {
                Platform.runLater(() ->
//
                                System.out.println("Users Request Ended with failure...")
                );
            }

            @Override
            public void onResponse(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if (response.code() == 200) {
                            String jsonArrayOfUsersNames;
                            jsonArrayOfUsersNames = Objects.requireNonNull(response.body()).string();
                            int responseYaz = new Gson().fromJson(jsonArrayOfUsersNames, int.class);
                            yazConsumer.accept(responseYaz);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
}
