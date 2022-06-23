package client.main;

import client.sub.main.CustomerMainBodyController;
import common.LoginController;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.Constants;
import util.HttpClientUtil;

import java.io.File;
import java.io.IOException;

public class ClientMainController {
    //Engine engine = Engine.getInstance();
    private final Engine engine = new Engine();
    Node header;
    Node clientDesktop;
    Node login;
    private Stage primaryStage;
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane loginComponent;
    @FXML
    private LoginController loginComponentController;
    @FXML
    private ScrollPane customerMainBody;

    ;
    @FXML
    private CustomerMainBodyController customerMainBodyController;
    private StringProperty currentUserName = new SimpleStringProperty();
    @FXML
    private Label welcomeLable;
    @FXML
    private Button NewLoan;

    public Engine getEngine() {
        return engine;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        header = root.getTop();
        clientDesktop = root.getCenter();
        login = root.getBottom();
        welcomeLable.setAlignment(Pos.CENTER);
        welcomeLable.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
        root.setCenter(null);
        root.setTop(null);
        loginComponentController.setMainController(this);
        welcomeLable.textProperty().bind(Bindings.concat("Welcome ", currentUserName));


        //customerMainBody.setFitToWidth(true); // tried to set the node to middle of the screen
        //CustomerMainBody.setFitToHeight(true);
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    public void switchToClientDesktop() {
        root.setBottom(null);
        root.setCenter(clientDesktop);
        root.setTop(header);
    }

    public void NewLoanButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();
        createFileRequest(absolutePath);
    }

    public void createFileRequest(String absolutePath) {
        File f = new File(absolutePath);
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", f.getName(), RequestBody.create(f, MediaType.parse("text/xml")))
                        .build();

        String finalUrl = HttpUrl
                .parse(Constants.NEW_LOAN_FROM_FILE)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Unknown Error");
                    alert.showAndWait();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() ->
                    {
                        try {
                            Alert alert = new Alert(Alert.AlertType.ERROR, response.body().string());
                            alert.showAndWait();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    Platform.runLater(() ->
                    {
                        try {
                            //Notifications.create().title("Success").text(response.body().string()).hideAfter(Duration.seconds(5)).position(Pos.CENTER).show();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, response.body().string());
                            alert.showAndWait();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                //   return false;
            }
        });
    }


    public enum MessageType {Error, Successfully, Information}
}