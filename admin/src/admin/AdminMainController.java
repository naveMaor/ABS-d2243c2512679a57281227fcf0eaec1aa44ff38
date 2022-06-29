package admin;


import admin.adminClientTable.ClientTableController;
import admin.adminLoanTables.adminLoanTablesMain.AdminLoanTablesController;
import admin.users.UsersListController;
import com.google.gson.Gson;
import common.LoginController;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.HttpAdminUtil;

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
    private Button CustomersInformationButtonId;

    @FXML
    private Button IncreaseYazButtonId;

    @FXML
    private Button LoadFileButtonId;


    @FXML
    private AnchorPane adminLoanTables;


    @FXML
    private AdminLoanTablesController adminLoanTablesController;

    @FXML
    private AnchorPane clientsTable;
    @FXML
    private ClientTableController clientsTableController;

    private StringProperty currentAdminName = new SimpleStringProperty();
    private StringProperty currentYaz = new SimpleStringProperty();
    private Node login;
    private Node body;


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

        HttpAdminUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                               new Alert(Alert.AlertType.ERROR,"Can't increase yaz request failed").showAndWait()
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    new Alert(Alert.AlertType.ERROR, response.body().string()).showAndWait();
                } else {
                    Platform.runLater(() -> {
                        increaseYaz.setValue(true);
                        String jsonArrayOfUsersNames = null;
                        try {
                            jsonArrayOfUsersNames = Objects.requireNonNull(response.body()).string();
                            int responseYaz = new Gson().fromJson(jsonArrayOfUsersNames, int.class);
                            currYaz.setText("Current YAZ: "+ responseYaz);
                        } catch (IOException e) {
                            e.printStackTrace();


                        }
                    });
                }
            }
        });

    }


    public void bindProperties(SimpleBooleanProperty isFileSelected, SimpleStringProperty selectedFileProperty, SimpleBooleanProperty isYazChanged) {
        //CustomersInformationButtonId.disableProperty().bind(isFileSelected.not());
        IncreaseYazButtonId.disableProperty().bind(isFileSelected.not());
        //LoadFileButtonId.disableProperty().bind(isFileSelected.not());
        isYazChanged.bindBidirectional(increaseYaz);
    }


    @FXML
    public void initialize() {
        login = rootBP.getTop();
        body = rootBP.getCenter();
        rootBP.setCenter(null);
        LoginPageController.setMainController(this);
        usersListController.setMainController(this);
        usersListController.startListRefresher();



    }




    public void rewindButtonListener(ActionEvent actionEvent) {
    }

    public void updateAdminName(String userName) {
        currentAdminName.set(userName);
    }

    public void switchToAdminDesktop() {
        synchronized (this) {
            rootBP.setTop(null);
            rootBP.setCenter(body);
            adminLoanTablesController.startLoanListRefresher();
            clientsTableController.startListRefresher();
        }
    }


}