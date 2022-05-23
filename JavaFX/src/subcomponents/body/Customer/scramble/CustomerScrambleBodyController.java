package subcomponents.body.Customer.scramble;

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

public class CustomerScrambleBodyController {
    Engine engine=Engine.getInstance();
    private CustomerMainBodyController customerMainBodyController;
    List<String> allCategoriesList;
    ObservableList<Loan> userFilteredLoanList =  FXCollections.observableArrayList();
    List<String> choosenCategories;
    ObservableList<String> existChoosenCategories;
    @FXML
    private Button activateScrambleButton;

    @FXML
    private Label amountToInvestLabel;

    @FXML
    private TextField amountToInvestTextField;

    @FXML
    private ListView<String> categoriesOptionsListView;

    @FXML
    private Button forwardCategoriesButton;

    @FXML
    private Label maxOpenLoansLabel;

    @FXML
    private TextField maxOpenLoansTextField;

    @FXML
    private Label maxOwnershipLabel;

    @FXML
    private TextField maxOwnershipTextField;

    @FXML
    private Label minInterestLabel;

    @FXML
    private Label minYazLabel;

    @FXML
    private TextField minimumInterestTextField;

    @FXML
    private TextField minimumYazTextField;

    @FXML
    private Button showRelevantLoansListButton;

    @FXML
    private ListView<String> userChoiceCategoriesListView;

    @FXML
    private TableColumn<Loan, Double> ColumnAmount;

    @FXML
    private TableColumn<Loan, String> ColumnCategory;

    @FXML
    private TableColumn<Loan, String> ColumnId;

    @FXML
    private TableColumn<Loan, Double> ColumnInterest;

    @FXML
    private TableColumn<Loan, String> ColumnName;

    @FXML
    private TableColumn<Loan, Integer> ColumnPayEvery;

    @FXML
    private TableColumn<Loan, eLoanStatus> ColumnStatus;

    @FXML
    private TableColumn<Loan, Integer> ColumnTotalYaz;

    @FXML
    private TableColumn<Loan, String> ColumnCheckBox;


    @FXML
    private TableView<Loan> ReleventLoansTable;


    @FXML
    void activateActivateScrambleButton(ActionEvent event) {
/*
        int amount=Integer.parseInt(amountToInvestTextField.getText());
        int maxOwnership = Integer.parseInt(maxOwnershipTextField.getText());
*/

    }


    @FXML
    void activateForwardCategoriesButton(ActionEvent event) {
        choosenCategories = categoriesOptionsListView.getSelectionModel().getSelectedItems();
        existChoosenCategories = userChoiceCategoriesListView.getItems();
        for(String category:choosenCategories){
            if(!existChoosenCategories.contains(category)){
                existChoosenCategories.add(category);
                categoriesOptionsListView.getItems().remove(category);
            }
        }
    }

    @FXML
    void activateBackwardCategoriesButton(ActionEvent event) {
        //tsad yamin
        choosenCategories = userChoiceCategoriesListView.getSelectionModel().getSelectedItems();
        //tsad smal
        existChoosenCategories = categoriesOptionsListView.getItems();

        for(String category:choosenCategories){
            if(!existChoosenCategories.contains(category)){
                existChoosenCategories.add(category);
                userChoiceCategoriesListView.getItems().remove(category);
            }
        }
    }

/*    @FXML
    void activateMinimumInterestTextField(ActionEvent event) {

    }*/


    @FXML
    void activateShowRelevantLoansListButton(ActionEvent event) {
        String clientName = customerMainBodyController.getCustomerName();
        int minInterest = Integer.parseInt(minimumInterestTextField.getText());
        int minYaz = Integer.parseInt(minimumYazTextField.getText());
        int maxOpenLoans = Integer.parseInt(maxOpenLoansTextField.getText());
        userFilteredLoanList = engine.O_getLoansToInvestList(clientName,minInterest,minYaz,maxOpenLoans,existChoosenCategories);
        loadReleventLoansTable();
        resetFileds();
    }

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    public void initialize(){
        allCategoriesList = engine.getDatabase().getAllCategories();
        categoriesOptionsListView.getItems().addAll(allCategoriesList);
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<Loan, Double>("totalLoanCostInterestPlusOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<Loan, Double>("originalInterest"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanCategory"));
        ColumnCheckBox.setCellValueFactory(new PropertyValueFactory<Loan, String>("select"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<Loan, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));
    }

    public void loadReleventLoansTable(){
        ReleventLoansTable.setItems(userFilteredLoanList);
    }

    public void resetFileds(){
        amountToInvestTextField.setText("");
        maxOpenLoansTextField.setText("");
        maxOwnershipTextField.setText("");
        minimumInterestTextField.setText("");
        minimumYazTextField.setText("");
        categoriesOptionsListView.getItems().addAll(allCategoriesList);
        userChoiceCategoriesListView.getItems().removeAll(allCategoriesList);
    }
}
