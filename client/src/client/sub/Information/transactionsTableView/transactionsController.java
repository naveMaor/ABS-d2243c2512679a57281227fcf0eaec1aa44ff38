package client.sub.Information.transactionsTableView;

import Money.operations.Transaction;
import client.sub.Information.CustomerInformationBodyCont;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import servletDTO.ClientDTOforServlet;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    private SimpleDoubleProperty balance = new SimpleDoubleProperty();

    @FXML
    void activateAmountTextField(ActionEvent event) {

    }

    @FXML
    void activateChargeButton(ActionEvent event) {
        int amount=Integer.parseInt(amountTextField.getText());
        if(amount<0){
            amount=amount*(-1);
        }
        createTransaction(amount);
        //loadTableData();
    }

    @FXML
    void activateWithdrawButton(ActionEvent event) {
        int amount=Integer.parseInt(amountTextField.getText());
        if(amount>0){
            amount=amount*(-1);
        }
        createTransaction(amount);
        //loadTableData();
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

    private ObservableList<Transaction> transactionsObservableList = FXCollections.observableArrayList();

    public void setMainController(CustomerInformationBodyCont customerInformationBodyCont) {
        this.customerInformationBodyCont = customerInformationBodyCont;
    }

    public void initialize(){
        amount.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("sum"));
        postBalance.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("balanceAfter"));
        preBalance.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("balanceBefore"));
        yaz.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("timeOfMovement"));
        currentBalanceLabel.textProperty().bind(Bindings.concat(balance));
    }

/*
    public void loadTableData() {
        createTransactionListRequest();
    }
*/

/*
    private void createTransactionListRequest(){
        String finalUrl = HttpUrl
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
                            response.body().close();
                            updatedData(jsonOfClientString);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
*/


    public void createTransaction(int amount) {
        createTransactionRequest(amount);
    }

    private void createTransactionRequest(int amount){
        String finalUrl = HttpUrl
                .parse(Constants.CREATE_TRANSACTION)
                .newBuilder()
                .addQueryParameter("amount", String.valueOf(amount))
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to create transaction")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Platform.runLater(() ->{

                    if(response.code() != 200){
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Balance can not be minus!");
                        alert.showAndWait();
                    }
                    else
                    {
                        try {
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            //updatedData(jsonOfClientString);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });

            }
        });


    }


/*    private void updatedData(String jsonOfClientString){
        ClientDTOforServlet client= customerInformationBodyCont.getCurrClient();
*//*        Transaction[] TransactionList = new Gson().fromJson(jsonOfClientString, Transaction[].class);
        transactionsObservableList.clear();
        transactionsObservableList.addAll(Arrays.asList(TransactionList));
        transactionsTableView.setItems(transactionsObservableList);*//*
        amountTextField.setText("");
        double TmpBalance = client.getMyAccount().getCurrBalance();
        balance.set(TmpBalance);
    }*/

    public void loadClientTransactions(List<Transaction> transactionList) {
        synchronized (this){
            transactionsObservableList.clear();
            transactionsObservableList.addAll(transactionList);
            transactionsTableView.setItems(transactionsObservableList);
        }
        ClientDTOforServlet client= customerInformationBodyCont.getCurrClient();
        //amountTextField.setText("");
        double TmpBalance = client.getMyAccount().getCurrBalance();
        balance.set(TmpBalance);
    }
}
