package client.sub.buyLoan;

import client.sub.main.CustomerMainBodyController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loan.enums.eLoanStatus;
import servletDTO.LoanInformationObj;

public class BuyLoanController {
    private CustomerMainBodyController customerMainBodyController;


    @FXML
    private TableColumn<LoanInformationObj, Double> ColumnAmount;

    @FXML
    private TableColumn<LoanInformationObj, String> ColumnCategory;

    @FXML
    private TableColumn<LoanInformationObj, String> ColumnId;

    @FXML
    private TableColumn<LoanInformationObj, Double> ColumnInterest;

    @FXML
    private TableColumn<LoanInformationObj, String> ColumnName;

    @FXML
    private TableColumn<LoanInformationObj, Integer> ColumnPayEvery;

    @FXML
    private TableColumn<LoanInformationObj, eLoanStatus> ColumnStatus;

    @FXML
    private TableColumn<LoanInformationObj, Integer> ColumnTotalYaz;


    @FXML
    private TableColumn<LoanInformationObj, Double> TotalLoanCost;

    @FXML
    private TableView<LoanInformationObj> ReleventLoansTable;

    public void initialize() {
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Double>("loanOriginalDepth"));
        TotalLoanCost.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Double>("totalLoanCostInterestPlusOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, eLoanStatus>("status"));
        addBuyButton();
    }

    public void loadTableData(){
        buyLoanTableListRequest();
    }

    private void buyLoanTableListRequest(){

    }

    private void buyLoanRequest(){

    }

    private void addBuyButton(){
        buyLoanRequest();
    }

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

}
