package client.main;

import client.main.newLoanWindow.NewLoanWindowController;
import client.sub.main.CustomerMainBodyController;
import common.LoginController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import servletDTO.ClientDTOforServlet;
import util.Constants;
import util.HttpClientUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class ClientMainController implements Closeable {
    public final static int REFRESH_RATE = 4000;
    private final IntegerProperty cuerrYaz;
    private final BooleanProperty autoUpdate;
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
    @FXML
    private CustomerMainBodyController customerMainBodyController;
    private ClientDTOforServlet currClient;
    private NewLoanWindowController newLoanWindowController;
    private Timer timer;
    private TimerTask listRefresher;
    @FXML
    private Label welcomeLable;
    @FXML
    private Button NewLoanFile;
    @FXML
    private Button NewLoanUser;

    @FXML
    private Button currYAZ;
    private StringProperty currentUserName = new SimpleStringProperty();

    public ClientMainController() {
        autoUpdate = new SimpleBooleanProperty();
        cuerrYaz = new SimpleIntegerProperty();
        autoUpdate.set(true);
    }

    public void NewManualLoanButton(ActionEvent actionEvent) {
        Stage Newstage = new Stage();
        Newstage.setMinHeight(600);
        Newstage.setMinWidth(600);
        Newstage.setTitle("Add new loan");

        URL newLoanWindow = getClass().getResource("newLoanWindow/NewLoanWindow.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(newLoanWindow);
            Parent root = fxmlLoader.load();
            newLoanWindowController = fxmlLoader.getController();

            newLoanWindowController.setMainController(this);

            Scene scene = new Scene(root, 700, 600);
            Newstage.setScene(scene);
            Newstage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public boolean getIsRewind(){
        return customerMainBodyController.getIsRewind();
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
        customerMainBodyController.setMainController(this);
        welcomeLable.textProperty().bind(Bindings.concat("Welcome ", currentUserName));
        bindDisable(customerMainBodyController.isRewindProperty());
        startListRefresher();
    }
    private void updateYAZ(Integer currYaz) {
        Platform.runLater(() -> {
            currYAZ.setText("Current YAZ: " + currYaz);

        });
    }

    public void startListRefresher() {
        listRefresher = new client.main.YazRefresher(
                autoUpdate,
                this::updateYAZ);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }


    public void switchToClientDesktop() {
        synchronized (this) {
            root.setBottom(null);
            root.setCenter(clientDesktop);
            root.setTop(header);
/*            try {
                createClientDTORequest();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            customerMainBodyController.startLoanListRefresher();
            //customerMainBodyController.loadData();
        }


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
                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
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
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, response.body().string());
                            alert.showAndWait();
                            //loadData();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                //   return false;
            }
        });
    }



    public ClientDTOforServlet getCurrClient() {
/*        try {
            createClientDTORequest();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return currClient;
    }

    public void setCurrClient(ClientDTOforServlet currClient) {
        this.currClient = currClient;
    }

    public void bindDisable(BooleanProperty autoUpdate){
        NewLoanFile.disableProperty().bind(autoUpdate);
        NewLoanUser.disableProperty().bind(autoUpdate);
    }
}