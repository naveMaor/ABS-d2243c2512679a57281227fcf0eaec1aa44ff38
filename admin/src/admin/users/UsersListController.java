package admin.users;


import admin.AdminMainController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import servletDTO.client.ClientBalanceObj;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class UsersListController implements Closeable {
    public final static int REFRESH_RATE = 2000;
    private final BooleanProperty autoUpdate;
    private final IntegerProperty totalUsers;
    private Timer timer;
    private TimerTask listRefresher;
    private HttpStatusUpdate httpStatusUpdate;

    @FXML
    private TitledPane titleTV;

    @FXML
    private TableView<ClientBalanceObj> usersTableView;

    @FXML
    private TableColumn<ClientBalanceObj, String> clientName;

    @FXML
    private TableColumn<ClientBalanceObj, Double> clientBalance;
    @FXML
    private AdminMainController adminMainController;

    public UsersListController() {
        autoUpdate = new SimpleBooleanProperty();
        totalUsers = new SimpleIntegerProperty();
        autoUpdate.set(true);
    }

    @FXML
    public void initialize() {
        titleTV.textProperty().bind(Bindings.concat("ABS Users: (", totalUsers.asString(), ")"));
        clientName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        clientBalance.setCellValueFactory(new PropertyValueFactory<>("clientBalance"));
    }

    public void setHttpStatusUpdate(HttpStatusUpdate httpStatusUpdate) {
        this.httpStatusUpdate = httpStatusUpdate;

    }

    public BooleanProperty autoUpdatesProperty() {
        return autoUpdate;
    }

    private void updateUsersList(List<ClientBalanceObj> usersInTheSystem) {
        Platform.runLater(() -> {
            ObservableList<ClientBalanceObj> items = FXCollections.observableArrayList();
            items.addAll(usersInTheSystem);
            totalUsers.set(usersInTheSystem.size());
            usersTableView.getItems().clear();
            usersTableView.getItems().addAll(usersInTheSystem);
        });
    }

    public void startListRefresher() {
        listRefresher = new admin.users.UserListRefresher(
                autoUpdate,
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        usersTableView.getItems().clear();
        totalUsers.set(0);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }


    public void setMainController(AdminMainController adminMainController) {
        this.adminMainController= adminMainController;
    }
}
