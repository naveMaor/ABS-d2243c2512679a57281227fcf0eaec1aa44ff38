package admin.adminLoanTables.activeStatus.innerTable;

import Money.operations.Payment;
import customes.Lenders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import servletDTO.admin.InnerTableObj;

public class ActiveInnerTableController {


    String loanName;
    ObservableList<Lenders> LendersObservableList = FXCollections.observableArrayList();
    ObservableList<Payment> PaymentObservableList = FXCollections.observableArrayList();
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

    public void initialize() {
        InvestedAmount.setCellValueFactory(new PropertyValueFactory<Lenders, Double>("deposit"));
        lenderName.setCellValueFactory(new PropertyValueFactory<Lenders, String>("fullName"));
        fund.setCellValueFactory(new PropertyValueFactory<Payment, Double>("fundPortion"));
        interest.setCellValueFactory(new PropertyValueFactory<Payment, Double>("interestPortion"));
        paymentAmount.setCellValueFactory(new PropertyValueFactory<Payment, Double>("fundPlusInterest"));
        yaz.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("paymentYaz"));

    }

    public void loadTableData(InnerTableObj innerTableObj) {
        LendersObservableList.clear();
        LendersObservableList.addAll(innerTableObj.getLendersList());
        PaymentObservableList.clear();
        PaymentObservableList.addAll(innerTableObj.getPaymentList());

        PaymentObservableList.removeIf(payment -> !payment.getIsPayed());


        pendingInnerTable.setItems(LendersObservableList);
        borrowerPayements.setItems(PaymentObservableList);
        if (PaymentObservableList.isEmpty()) {
            borrowerPayements.disableProperty();
        }
    }
}
