package subcomponents.body.Customer.Payment;

import Money.operations.Payment;
import exceptions.messageException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import loan.Loan;
import loan.enums.eLoanStatus;
import subcomponents.body.Customer.main.CustomerMainBodyController;
import utills.Engine;

import java.util.List;

public class CustomerPaymentBodyController {
    private Engine engine=Engine.getInstance();
    private ObservableList<String> CheckBoxLoanList =  FXCollections.observableArrayList();
    private ObservableList<Loan> loanList =  FXCollections.observableArrayList();
    private CustomerMainBodyController customerMainBodyController;


    @FXML
    private ListView<String> LoansListView;

    @FXML
    private TableColumn<Loan, Double> currPay;

    @FXML
    private TableColumn<Loan, Double> leftPay;

    @FXML
    private Button closeEntireLoanButton;

    @FXML
    private TableColumn<Loan, String> loanId;


    @FXML
    private TableColumn<Loan, Integer> nextYaz;

    @FXML
    private TextArea notificationsTextArea;

    @FXML
    private Button pay;

    @FXML
    private TableColumn<Loan, String> select;

    @FXML
    private TableView<Loan> ReleventLoansTable;

    @FXML
    void activateBackwardLoanButton(ActionEvent event) {
        List<String> choosenLoans=LoansListView.getSelectionModel().getSelectedItems();
        LoansListView.getItems().removeAll(choosenLoans);
    }

    @FXML
    void activateCloseEntireLoanButton(ActionEvent event) {

    }

    @FXML
    void activateForwardLoanButton(ActionEvent event) {
        ObservableList<Loan> items = ReleventLoansTable.getItems();
        int num = 0;
        for (Loan loan:items){
            if(loan.getSelect().isSelected()&&loan.getNextExpectedPaymentAmountDataMember()>0&&loan.getNextYazToPay()==0){
                CheckBoxLoanList.add(loan.getLoanID());
                num++;
            }
        }
        if(num==0){
            Alert alert = new Alert(Alert.AlertType.ERROR,"NO LOANS SELECTED!\nPlease select by the check box \nYou can only choose loans that has next yaz 0 and loans that you not payed already");
            alert.showAndWait();
        }
        else {
            for(String loanID:CheckBoxLoanList){
                if(!LoansListView.getItems().contains(loanID)){
                    LoansListView.getItems().add(loanID);
                }
            }
        }
        for (Loan loan:items) {
            loan.getSelect().setSelected(false);
            //loan.setSelect(false);
        }
        CheckBoxLoanList.clear();
    }

    @FXML
    void activatePayLoanSinglePaymentButton(ActionEvent event) {
        List<String> loanNameList = LoansListView.getItems();
        List<Loan> tmp = engine.getDatabase().getLoanList();
        ObservableList <Loan> loanList = FXCollections.observableArrayList();
        for (Loan loan:tmp){
            if (loanNameList.contains(loan.getLoanID())){
                loanList.add(loan);
            }
        }

        try {
            engine.paySinglePaymentForLoanList(loanList);
        } catch (messageException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
            alert.showAndWait();
        }
        loadLoanTableData();
        LoansListView.getItems().clear();
    }

    private List<Loan> choosenLoans;

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    public void initialize() {
        currPay.setCellValueFactory(new PropertyValueFactory<Loan, Double>("nextExpectedPaymentAmountDataMember"));
        leftPay.setCellValueFactory(new PropertyValueFactory<Loan, Double>("totalRemainingLoan"));
        loanId.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        nextYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("nextYazToPay"));
        select.setCellValueFactory(new PropertyValueFactory<Loan, String>("select"));

    }

    public void loadLoanTableData(){
        ObservableList <Loan> loanListForTable =engine.getDatabase().o_getAllLoansByClientName(customerMainBodyController.getCustomerName());
        for (Loan loan:loanListForTable
             ) {
            eLoanStatus status = loan.getStatus();
            if((status== eLoanStatus.FINISHED)||(status== eLoanStatus.NEW)||(status== eLoanStatus.PENDING)){
                loanListForTable.remove(loan);
            }
        }
        //because of unexpected bug that the compliler does not run on all over the items i have to write it down again
        for(Loan loan:loanListForTable){
            eLoanStatus status = loan.getStatus();
            if((status== eLoanStatus.FINISHED)||(status== eLoanStatus.NEW)||(status== eLoanStatus.PENDING)){
                loanListForTable.remove(loan);
            }
        }
        ReleventLoansTable.getItems().clear();
        ReleventLoansTable.setItems(loanListForTable);
    }
}
