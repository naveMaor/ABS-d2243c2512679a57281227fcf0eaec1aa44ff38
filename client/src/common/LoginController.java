package common;

import client.main.ClientMainController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableFloatValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;

public class LoginController {

    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    @FXML
    public TextField userNameTextField;

    //private ChatAppMainController chatAppMainController;
    @FXML
    public Label errorMessageLabel;
    @FXML
    public Button loginButton;
    @FXML
    public Button quitButton;
    private ClientMainController clientMainController;
    private BooleanProperty systemInRewindMode = new SimpleBooleanProperty(false);


    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);

    }
    private void createRewindRequest(){
        String finalUrl = HttpUrl
                .parse(Constants.GET_REWIND)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        Response response = HttpClientUtil.execute(request);
        if (response.code() == 200) {

            try {
                String jsonOfClientString = response.body().string();
                response.body().close();
                boolean rewind = new Gson().fromJson(jsonOfClientString, boolean.class);
                systemInRewindMode.setValue(rewind);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("failed to call url rewind");
        }
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        createRewindRequest();
        if (systemInRewindMode.getValue()) {
            errorMessageProperty.set("The System is in rewind mode sorry you have to wait");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Some`thing went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        clientMainController.updateUserName(userName);
                        clientMainController.switchToClientDesktop();
                    });
                }
            }
        });
    }

    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

    public void setMainController(ClientMainController mainController) {
        this.clientMainController = mainController;
    }

}
