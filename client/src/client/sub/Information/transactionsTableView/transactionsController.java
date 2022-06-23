package client.sub.Information.transactionsTableView;

import Money.operations.Transaction;
import client.sub.Information.CustomerInformationBodyCont;
import com.google.gson.Gson;
import customes.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import engine.Engine;
import loan.Loan;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;

public class transactionsController {
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


    public void loadTableData() {
        createTransactionListRequest();
        transactionsTableView.getItems().clear();
        transactionsTableView.setItems(transactionsObservableList);
        amountTextField.setText("");
        //TODO FIXXXXQWE123123
        if (customerInformationBodyCont.getCurrClient() != null) {
            double balance = customerInformationBodyCont.getCurrClient().getMyAccount().getCurrBalance();
            currentBalanceLabel.textProperty().set(String.valueOf(balance));
        }
    }




    private void createTransactionListRequest(){
        String finalUrl = HttpUrl
                //todo parameter name here
                .parse(Constants.GET_TRANSACTION_LIST)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url transaction list")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            String jsonOfClientString = response.body().string();
                            // response.body().close();
                            Transaction[] TransactionList = new Gson().fromJson(jsonOfClientString, Transaction[].class);
                            transactionsObservableList.clear();
                            transactionsObservableList.addAll(TransactionList);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }

}
