package subcomponents.body.Admin.adminLoanTables.finishedStatus.innerTable;

import Money.operations.Payment;
import customes.Lenders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import engine.Engine;

import java.util.List;

public class finishedInnerTableController {

    Engine engine =Engine.getInstance();


    @FXML
    private TableColumn<Lenders, Double> InvestedAmount;

    @FXML
    private TableColumn<Lenders, String> lenderName;

    @FXML
    private TableView<Lenders> pendingInnerTable;


    @FXML
    private TableView<Payment> borrowerPayements;

    @FXML
    private TableColumn<Payment, Double> fund;

    @FXML
    private TableColumn<Payment, Double> interest;


    @FXML
    private TableColumn<Payment, Double> paymentAmount;


    @FXML
    private TableColumn<Payment, Integer> yaz;

    ObservableList<Lenders> LendersObservableList = FXCollections.observableArrayList();
    ObservableList<Payment> PaymentObservableList = FXCollections.observableArrayList();


    public void initializeTable(List<Lenders> lendersObservableList, List<Payment> paymentObservableList) {
        InvestedAmount.setCellValueFactory(new PropertyValueFactory<Lenders, Double>("deposit"));
        lenderName.setCellValueFactory(new PropertyValueFactory<Lenders, String>("fullName"));
        fund.setCellValueFactory(new PropertyValueFactory<Payment, Double>("fundPortion"));
        interest.setCellValueFactory(new PropertyValueFactory<Payment, Double>("interestPortion"));
        paymentAmount.setCellValueFactory(new PropertyValueFactory<Payment, Double>("fundPlusInterest"));
        yaz.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("paymentYaz"));


        LendersObservableList.addAll(lendersObservableList);
        PaymentObservableList.addAll(paymentObservableList);

        PaymentObservableList.removeIf(payment -> !payment.getIsPayed());

        pendingInnerTable.setItems(LendersObservableList);
        borrowerPayements.setItems(PaymentObservableList);
    }


}
