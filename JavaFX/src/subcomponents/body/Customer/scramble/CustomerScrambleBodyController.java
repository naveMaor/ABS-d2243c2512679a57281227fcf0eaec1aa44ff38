package subcomponents.body.Customer.scramble;

import customes.Client;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.converter.NumberStringConverter;
import loan.Loan;
import loan.enums.eLoanStatus;
import subcomponents.body.Customer.main.CustomerMainBodyController;
import utills.Engine;
import utills.scrambleService;

import java.util.List;

public class CustomerScrambleBodyController {
    private Engine engine=Engine.getInstance();
    private CustomerMainBodyController customerMainBodyController;
    private List<String> allCategoriesList;
    private ObservableList<Loan> userFilteredLoanList =  FXCollections.observableArrayList();
    private ObservableList<Loan> CheckBoxLoanList =  FXCollections.observableArrayList();

    private List<String> choosenCategories;
    private ObservableList<String> existChoosenCategories;

    private int amount;
    private int maxOwnership;

    private int minInterest ;
    private int minYaz;
    private int maxOpenLoans;
    private String clientName;
    @FXML
    private StackPane stackPane;

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
        int num = 0;
        ObservableList<Loan> items = ReleventLoansTable.getItems();
        clientName = customerMainBodyController.getCustomerName();
        for (Loan loan:items){
            if(loan.getSelect().isSelected()){
                CheckBoxLoanList.add(loan);
                num++;
            }
        }
        if(num==0){
            Alert alert = new Alert(Alert.AlertType.ERROR,"NO LOANS SELECTED!");
            alert.showAndWait();
        }
        else {
            ReleventLoansTable.getItems().removeAll(CheckBoxLoanList);
            engine.investing_according_to_agreed_risk_management_methodology(CheckBoxLoanList,amount,clientName,maxOwnership);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"investing completed");
            alert.showAndWait();
            if (ReleventLoansTable.getItems().size()==0){
                resetFileds();
            }
        }
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
        clientName = customerMainBodyController.getCustomerName();
        Alert alert;
        double clientBalance =engine.getDatabase().getClientByname(clientName).getMyAccount().getCurrBalance();
        minInterest = -1;
        maxOwnership = -1;
        minYaz =-1;
        amount = 0;
        maxOpenLoans = -1;
        try {
/*            Bindings.bindBidirectional(minimumInterestTextField.textProperty(),
                    minInterest,
                    new NumberStringConverter());
            Bindings.bindBidirectional(minimumInterestTextField.textProperty(),
                    minYaz,
                    new NumberStringConverter());
            Bindings.bindBidirectional(minimumInterestTextField.textProperty(),
                    maxOpenLoans,
                    new NumberStringConverter());
            Bindings.bindBidirectional(minimumInterestTextField.textProperty(),
                    amount,
                    new NumberStringConverter());
            Bindings.bindBidirectional(minimumInterestTextField.textProperty(),
                    maxOwnership,
                    new NumberStringConverter());*/
            String minInterestText = minimumInterestTextField.getText();
            if(minInterestText.matches("[0-9]+"))
                minInterest =  Integer.parseInt(minInterestText);
            //minInterest = Integer.parseInt(minimumInterestTextField.getText());

            String maxShareText = maxOwnershipTextField.getText();
            if(maxShareText.matches("[0-9]+"))
                maxOwnership =  Integer.parseInt(maxShareText);
            //maxOwnership = Integer.parseInt(maxOwnershipTextField.getText());


            String minYazText = minimumYazTextField.getText();
            if(minYazText.matches("[0-9]+"))
                minYaz =  Integer.parseInt(minYazText);
            //minYaz = Integer.parseInt(minimumYazTextField.getText());


            String investmentAmountText = amountToInvestTextField.getText();
            if(investmentAmountText.matches("[0-9]+"))
                amount =  Integer.parseInt(investmentAmountText);
            //amount=Integer.parseInt(amountToInvestTextField.getText());

            String maxInvolvedLoansText = maxOpenLoansTextField.getText();
            if(maxInvolvedLoansText.matches("[0-9]+"))
                maxOpenLoans =  Integer.parseInt(maxInvolvedLoansText);
            //maxOpenLoans = Integer.parseInt(maxOpenLoansTextField.getText());


        }
        catch (NumberFormatException e){
            alert = new Alert(Alert.AlertType.ERROR,"invalid parameters");
            alert.showAndWait();
        }
        scrambleService service =new scrambleService(clientName,minInterest,minYaz,maxOpenLoans,existChoosenCategories,maxOwnership);

        Region veil = new Region();
        veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
        veil.setPrefSize(400, 440);
        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(140, 140);
        p.setStyle(" -fx-progress-color: orange;");

        p.progressProperty().bind(service.progressProperty());
        veil.visibleProperty().bind(service.runningProperty());
        p.visibleProperty().bind(service.runningProperty());

        customerMainBodyController.getInformationTabPane().disableProperty().bind(service.runningProperty());
        customerMainBodyController.getPaymentTabPane().disableProperty().bind(service.runningProperty());
        customerMainBodyController.runningServicePropertyProperty().bind(service.runningProperty());

        ReleventLoansTable.itemsProperty().bind(service.valueProperty());

        stackPane.getChildren().addAll(veil, p);


       if (amount>clientBalance){
            alert = new Alert(Alert.AlertType.ERROR,"Amount must be less then client current balance:\n"+ clientBalance);
            alert.showAndWait();
        }
        else{
           service.start();
        }
    }

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    public void initialize(){
        allCategoriesList = engine.getDatabase().getAllCategories();
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<Loan, Double>("totalLoanCostInterestPlusOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<Loan, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanCategory"));
        ColumnCheckBox.setCellValueFactory(new PropertyValueFactory<Loan, String>("select"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<Loan, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));
        //resetFileds();
    }

    public void loadReleventLoansTable(){

        userFilteredLoanList = engine.O_getLoansToInvestList(clientName,minInterest,minYaz,maxOpenLoans,existChoosenCategories,maxOwnership);
        resetRelevantLoansTable();
        //ReleventLoansTable.setItems(userFilteredLoanList);
    }

    public void resetFileds(){
        amountToInvestTextField.clear();
        maxOpenLoansTextField.clear();
        maxOwnershipTextField.clear();
        minimumInterestTextField.clear();
        minimumYazTextField.clear();
        categoriesOptionsListView.getItems().addAll(allCategoriesList);
        userChoiceCategoriesListView.getItems().removeAll(allCategoriesList);
    }
    public void resetRelevantLoansTable(){
        ReleventLoansTable.getItems().clear();
    }
}
