package admin.adminClientTable;


import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import servletDTO.client.ClientLoansObj;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClientTableController implements Closeable {


    public final static int REFRESH_RATE = 4000;
    private final BooleanProperty autoUpdate;
    ObservableList<ClientLoansObj> result = FXCollections.observableArrayList();
    ObservableList<ClientLoansObj> clientLoansObservableList = FXCollections.observableArrayList();
    private Timer timer;
    private TimerTask listRefresher;
    @FXML
    private TableColumn<ClientLoansObj, Double> balance;
    @FXML
    private TableColumn<ClientLoansObj, ?> borrower;
    @FXML
    private TableColumn<ClientLoansObj, Integer> borrowerActive;
    @FXML
    private TableColumn<ClientLoansObj, Integer> borrowerFinished;
    @FXML
    private TableColumn<ClientLoansObj, Integer> borrowerNew;
    @FXML
    private TableColumn<ClientLoansObj, Integer> borrowerPending;
    @FXML
    private TableColumn<ClientLoansObj, Integer> borrowerRisk;
    @FXML
    private TableColumn<ClientLoansObj, ?> lender;
    @FXML
    private TableColumn<ClientLoansObj, Integer> lenderActive;
    @FXML
    private TableColumn<ClientLoansObj, Integer> lenderFinished;
    @FXML
    private TableColumn<ClientLoansObj, Integer> lenderNew;
    @FXML
    private TableColumn<ClientLoansObj, Integer> lenderPending;
    @FXML
    private TableColumn<ClientLoansObj, Integer> lenderRisk;
    @FXML
    private TableColumn<ClientLoansObj, String> name;
    @FXML
    private TableView<ClientLoansObj> adminClientTable;


    public ClientTableController() {
        autoUpdate = new SimpleBooleanProperty();
        autoUpdate.set(true);
    }

    @FXML
    public void initialize() {
        balance.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Double>("balance"));

        borrowerActive.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asBorrowerActive"));
        borrowerFinished.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asBorrowerFinished"));
        borrowerNew.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asBorrowerNew"));
        borrowerPending.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asBorrowerPending"));
        borrowerRisk.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asBorrowerRisk"));

        lenderActive.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asLenderActive"));
        lenderFinished.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asLenderFinished"));
        lenderNew.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asLenderNew"));
        lenderPending.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asLenderPending"));
        lenderRisk.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, Integer>("asLenderRisk"));
        name.setCellValueFactory(new PropertyValueFactory<ClientLoansObj, String>("Name"));
        autoUpdate.set(true);

    }


    private void updateTableClientsList(List<ClientLoansObj> usersInTheSystem) {
        Platform.runLater(() -> {
            ObservableList<ClientLoansObj> items = FXCollections.observableArrayList();
            items.addAll(usersInTheSystem);
            adminClientTable.getItems().clear();
            adminClientTable.getItems().addAll(usersInTheSystem);

        });
    }

    public void startListRefresher() {
        listRefresher = new admin.adminClientTable.ClientTableRefresher(
                autoUpdate,
                this::updateTableClientsList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        adminClientTable.getItems().clear();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
}