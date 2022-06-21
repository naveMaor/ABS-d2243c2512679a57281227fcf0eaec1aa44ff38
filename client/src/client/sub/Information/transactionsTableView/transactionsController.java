package client.sub.Information.transactionsTableView;

import Money.operations.Transaction;
import client.sub.Information.CustomerInformationBodyCont;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import engine.Engine;

public class transactionsController {
    private Engine engine = Engine.getInstance();
    private CustomerInformationBodyCont customerInformationBodyCont;

    @FXML
    private Label currentBalanceLabel;

    @FXML
    private TextField amountTextField;

    @FXML
    private Button chargeButton;



    @FXML
    private Button withdrawButton;


    @FXML
    void activateAmountTextField(ActionEvent event) {

    }

    @FXML
    void activateChargeButton(ActionEvent event) {
        int amount=Integer.parseInt(amountTextField.getText());
        if(amount<0){
            amount=amount*(-1);
        }
        customerInformationBodyCont.createTransaction(amount);
        loadTableData();
    }

    @FXML
    void activateWithdrawButton(ActionEvent event) {
        int amount=Integer.parseInt(amountTextField.getText());
        if(amount>0){
            amount=amount*(-1);
        }
        customerInformationBodyCont.createTransaction(amount);
        loadTableData();
    }


    @FXML
    private TableColumn<Transaction, Integer> amount;

    @FXML
    private TableColumn<Transaction, Integer> postBalance;

    @FXML
    private TableColumn<Transaction, Integer> preBalance;

    @FXML
    private TableView<Transaction> transactionsTableView;

    @FXML
    private TableColumn<Transaction, Integer> yaz;

    ObservableList<Transaction> transactionsObservableList = FXCollections.observableArrayList();;

    public void setMainController(CustomerInformationBodyCont customerInformationBodyCont) {
        this.customerInformationBodyCont = customerInformationBodyCont;
    }

    public void initialize(){
        amount.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("sum"));
        postBalance.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("balanceAfter"));
        preBalance.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("balanceBefore"));
        yaz.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("timeOfMovement"));
    }



    public void loadTableData(){
        String customerName=customerInformationBodyCont.customerNameProperty().get();
        double balance = engine.getDatabase().getClientByname(customerName).getMyAccount().getCurrBalance();
        transactionsObservableList =engine.getClientTransactionsList(customerName);
        transactionsTableView.getItems().clear();
        transactionsTableView.setItems(transactionsObservableList);
        amountTextField.setText("");
        currentBalanceLabel.textProperty().set(String.valueOf(balance));
        //todo:take the name of the customer from the main controller and pass
    }
}
