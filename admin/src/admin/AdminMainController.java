package admin;


import admin.adminClientTable.ClientTableController;
import admin.adminLoanTables.adminLoanTablesMain.AdminLoanTablesController;
import admin.users.UsersListController;
import com.google.gson.Gson;
import common.LoginController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.HttpAdminUtil;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.Objects;


public class AdminMainController {

    SimpleBooleanProperty increaseYaz = new SimpleBooleanProperty(false);
    @FXML
    private Button currYaz;


    @FXML
    private VBox usersList;
    @FXML
    private UsersListController usersListController;
    @FXML
    private AnchorPane LoginPage;
    @FXML
    private LoginController LoginPageController;
    @FXML
    private BorderPane rootBP;

    @FXML
    private Button rewindButton;
    @FXML
    private Button CustomersInformationButtonId;

    @FXML
    private Button IncreaseYazButton;
    @FXML
    private Button backToNormalButton;

    @FXML
    private AnchorPane adminLoanTables;

    @FXML
    private AdminLoanTablesController adminLoanTablesController;

    @FXML
    private AnchorPane clientsTable;
    @FXML
    private ClientTableController clientsTableController;

    @FXML
    private ComboBox<Integer> yazComboBox;


    private StringProperty currentAdminName = new SimpleStringProperty();
    private StringProperty currentYaz = new SimpleStringProperty();
    private BooleanProperty isRewind = new SimpleBooleanProperty();
    private Node login;
    private Node body;
    private Integer rewindTime = null;


    @FXML
    void IncreaseYazButtonListener1(ActionEvent event) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.INCREASE_YAZ)
                .newBuilder()
                .addQueryParameter("admin", "true")
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        HttpAdminUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        new Alert(Alert.AlertType.ERROR, "Can't increase yaz request failed").showAndWait()
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, response.body().string());
                    alert.showAndWait();
                } else {
                    Platform.runLater(() -> {
                        increaseYaz.setValue(true);
                        String jsonArrayOfUsersNames = null;
                        try {
                            jsonArrayOfUsersNames = Objects.requireNonNull(response.body()).string();
                            int responseYaz = new Gson().fromJson(jsonArrayOfUsersNames, int.class);
                            currYaz.setText("Current YAZ: " + responseYaz);
                            yazComboBox.getItems().add(responseYaz - 1);
                        } catch (IOException e) {
                            e.printStackTrace();


                        }
                    });
                }
            }
        });

    }


    @FXML
    void IncreaseYazButtonListener(ActionEvent event) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.INCREASE_YAZ)
                .newBuilder()
                .addQueryParameter("admin", "true")
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        Response response = HttpClientUtil.execute(request);


        if (response.code() != 200) {
            Alert alert = null;
            try {
                alert = new Alert(Alert.AlertType.ERROR, response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            alert.showAndWait();
        } else {
            increaseYaz.setValue(true);
            String jsonArrayOfUsersNames = null;
            try {
                jsonArrayOfUsersNames = Objects.requireNonNull(response.body()).string();
                int responseYaz = new Gson().fromJson(jsonArrayOfUsersNames, int.class);
                currYaz.setText("Current YAZ: " + responseYaz);
                yazComboBox.getItems().add(responseYaz - 1);
            } catch (IOException e) {
                e.printStackTrace();


            }

        }
    }

    @FXML
    public void rewindButtonAction(ActionEvent actionEvent) {
        rewindTime = yazComboBox.getValue();
        if (rewindTime == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "PLEASE CHOOSE YAZ FIRST!");
            alert.showAndWait();
            return;
        }
        rewindSystemRequest(rewindTime);
    }

    public void backToNormalButtonAction(ActionEvent actionEvent) {
        backToNormalSystemRequest();
    }


    @FXML
    public void initialize() {
        login = rootBP.getTop();
        body = rootBP.getCenter();
        rootBP.setCenter(null);
        LoginPageController.setMainController(this);
        usersListController.setMainController(this);
        usersListController.startListRefresher();
        IncreaseYazButton.disableProperty().bind(isRewind);
        rewindButton.disableProperty().bind(isRewind);
        backToNormalButton.disableProperty().bind(isRewind.not());
        LoginPageController.bindProperties(isRewind);
    }


    public void updateAdminName(String userName) {
        currentAdminName.set(userName);
    }

    public void switchToAdminDesktop() {
        synchronized (this) {
            rootBP.setTop(null);
            rootBP.setCenter(body);
            Scene s = rootBP.getParent().getScene();
            Stage thisStage = (Stage) s.getWindow();
            thisStage.setWidth(1200);
            thisStage.setHeight(1200);
            adminLoanTablesController.startLoanListRefresher();
            clientsTableController.startListRefresher();
        }
    }

    private void rewindSystemRequest(int yaz) {
        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(yaz, int.class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.REWIND_TIME)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();


        Response response = HttpClientUtil.execute(request);

        if (response.code() == 200) {
            String respon = null;
            try {
                respon = Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int responseYaz = new Gson().fromJson(respon, int.class);
            currYaz.setText("Current YAZ: " + responseYaz);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "system is now READONLY mode at yaz " + yaz);
            alert.showAndWait();
            isRewind.setValue(true);
        } else {
            System.out.println("failed to REWIND");
        }

    }

    private void backToNormalSystemRequest() {
        String finalUrl = HttpUrl
                .parse(Constants.LOAD_NORMAL_DATA)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        Response response = HttpClientUtil.execute(request);

        if (response.code() == 200) {
            String respon = null;
            try {
                respon = Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int responseYaz = new Gson().fromJson(respon, int.class);
            currYaz.setText("Current YAZ: " + responseYaz);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "system is now back to normal mode");
            isRewind.setValue(false);
            alert.showAndWait();
        } else {
            System.out.println("failed to LOAD NORMAL DATA");
        }
    }
}