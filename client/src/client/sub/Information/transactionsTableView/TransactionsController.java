package client.sub.Information.transactionsTableView;

import Money.operations.Transaction;
import client.sub.Information.CustomerInformationBodyCont;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import servletDTO.ClientDTOforServlet;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.List;

public class TransactionsController {
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

    @FXML
    void activateAmountTextField(ActionEvent event) {

    }

    @FXML
    void activateChargeButton(ActionEvent event) {
        int amount = Integer.parseInt(amountTextField.getText());
        if (amount < 0) {
            amount = amount * (-1);
        }
        createTransaction(amount);
        amountTextField.clear();
        //loadTableData();
    }

    @FXML
    void activateWithdrawButton(ActionEvent event) {
        int amount = Integer.parseInt(amountTextField.getText());
        if (amount > 0) {
            amount = amount * (-1);
        }
        amountTextField.clear();
        createTransaction(amount);
        //loadTableData();
    }

    public void setMainController(CustomerInformationBodyCont customerInformationBodyCont) {
        this.customerInformationBodyCont = customerInformationBodyCont;
    }

    public void initialize() {
        amount.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("sum"));
        postBalance.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("balanceAfter"));
        preBalance.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("balanceBefore"));
        yaz.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("timeOfMovement"));
        currentBalanceLabel.textProperty().bind(Bindings.concat(balance));
    }


    public void bindDisable(BooleanProperty autoUpdate) {
        chargeButton.disableProperty().bind(autoUpdate);
        withdrawButton.disableProperty().bind(autoUpdate);
        amountTextField.disableProperty().bind(autoUpdate);
    }

    public void createTransaction(int amount) {
        createTransactionRequest(amount);
    }

    private void createTransactionRequest(int amount) {
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

                Platform.runLater(() -> {

                    if (response.code() != 200) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Balance can not be minus!");
                        alert.showAndWait();
                    } else {
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

    public void restFields() {
        transactionsObservableList.clear();
    }


    public void loadClientTransactions(List<Transaction> transactionList) {
        synchronized (this) {
            ClientDTOforServlet client = customerInformationBodyCont.getCurrClient();
            transactionsObservableList.clear();
            if (client != null) {
                transactionsTableView.setItems(transactionsObservableList);
                transactionsObservableList.addAll(transactionList);
                double TmpBalance = client.getMyAccount().getCurrBalance();
                balance.set(TmpBalance);


            } else {
                balance.set(0.0);
            }

        }
    }
}
