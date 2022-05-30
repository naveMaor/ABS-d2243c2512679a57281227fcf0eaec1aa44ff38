package subcomponents.body.Admin.adminClientTable;

import ClientDTO.ClientObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loan.Loan;
import utills.Engine;

import java.util.List;

public class adminClientTableController {
    Engine engine =Engine.getInstance();


    @FXML
    private TableColumn<ClientLoans, Double> balance;

    @FXML
    private TableColumn<ClientLoans, ?> borrower;

    @FXML
    private TableColumn<ClientLoans, Integer> borrowerActive;

    @FXML
    private TableColumn<ClientLoans, Integer> borrowerFinished;

    @FXML
    private TableColumn<ClientLoans, Integer> borrowerNew;

    @FXML
    private TableColumn<ClientLoans, Integer> borrowerPending;

    @FXML
    private TableColumn<ClientLoans, Integer> borrowerRisk;

    @FXML
    private TableColumn<ClientLoans, ?> lender;

    @FXML
    private TableColumn<ClientLoans, Integer> lenderActive;

    @FXML
    private TableColumn<ClientLoans, Integer> lenderFinished;

    @FXML
    private TableColumn<ClientLoans, Integer> lenderNew;

    @FXML
    private TableColumn<ClientLoans, Integer> lenderPending;

    @FXML
    private TableColumn<ClientLoans, Integer> lenderRisk;

    @FXML
    private TableColumn<ClientLoans, String> name;

    @FXML
    private TableView<ClientLoans> adminClientTable;

    ObservableList <ClientLoans> result =  FXCollections.observableArrayList();


    ObservableList<ClientLoans> clientLoansObservableList = FXCollections.observableArrayList();


    public void initializeClientTable() {
        balance.setCellValueFactory(new PropertyValueFactory<ClientLoans, Double>("balance"));

        borrowerActive.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asBorrowerActive"));
        borrowerFinished.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asBorrowerFinished"));
        borrowerNew.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asBorrowerNew"));
        borrowerPending.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asBorrowerPending"));
        borrowerRisk.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asBorrowerRisk"));

        lenderActive.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asLenderActive"));
        lenderFinished.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asLenderFinished"));
        lenderNew.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asLenderNew"));
        lenderPending.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asLenderPending"));
        lenderRisk.setCellValueFactory(new PropertyValueFactory<ClientLoans, Integer>("asLenderRisk"));

        name.setCellValueFactory(new PropertyValueFactory<ClientLoans, String>("Name"));


        List<ClientObj> clientsObjList = engine.getDatabase().getClientsObjList();
        clientLoansObservableList.clear();
        for (ClientObj clientObj:clientsObjList){
            ClientLoans clientLoans = new ClientLoans(clientObj);
            clientLoansObservableList.add(clientLoans);
        }
        adminClientTable.setItems(clientLoansObservableList);
    }
}
