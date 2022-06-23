package client.sub.Information;

import com.google.gson.Gson;
import client.sub.Information.transactionsTableView.transactionsController;
import client.sub.main.CustomerMainBodyController;
import customes.Client;
import exceptions.BalanceException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loan.Loan;
import loan.enums.eLoanStatus;
import engine.Engine;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;

public class CustomerInformationBodyCont {

    private CustomerMainBodyController customerMainBodyController;

    @FXML
    private BorderPane transactions;
    @FXML
    private transactionsController transactionsController;

    @FXML
    private TableColumn<Loan, String> borrowerLoanID;

    @FXML
    private TableColumn<Loan, String> lenderLoanID;

    @FXML
    private TableColumn<Loan, String> borrowerLoanCategory;

    @FXML
    private TableColumn<Loan, String> lenderLoanCat;

    @FXML
    private TableColumn<Loan, eLoanStatus> borrowerLoanStatus;

    @FXML
    private TableColumn<Loan, eLoanStatus> lenderLoanStatus;

    @FXML
    private TableColumn<Loan, String> lenderBorrowerName;

    @FXML
    private TableView<Loan> lenderTable;

    @FXML
    private TableView<Loan> borrowerTable;

    ObservableList<Loan> clientAsLenderLoanList = FXCollections.observableArrayList();
    ObservableList<Loan> clientAsBorrowLoanList = FXCollections.observableArrayList();


    public void initializeClientTable(){
        createLoansAsLenderRequest();
        createLoansAsBorrowerRequest();
    }

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    @FXML public void initialize() {
        if (transactionsController != null) {
            transactionsController.setMainController(this);
        }
        borrowerLoanID.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        lenderLoanID.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        borrowerLoanCategory.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanCategory"));
        lenderLoanCat.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanCategory"));
        lenderBorrowerName.setCellValueFactory(new PropertyValueFactory<Loan, String>("borrowerName"));
        borrowerLoanStatus.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));
        lenderLoanStatus.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));
    }

    public void createTransaction(int amount) {
        createTransactionRequest(amount);
    }
    public SimpleStringProperty customerNameProperty() {
        return customerMainBodyController.customerNameProperty();
    }

    public void loadTransactionsTable(){
        transactionsController.loadTableData();
    }

    private void customiseFactory(TableColumn<Loan, eLoanStatus> calltypel) {
        calltypel.setCellFactory(column -> {
            return new TableCell<Loan, eLoanStatus>() {
                @Override
                protected void updateItem(eLoanStatus item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<Loan> currentRow = getTableRow();

                    if (!isEmpty()) {

                        if(item==eLoanStatus.RISK)
                            currentRow.setStyle("-fx-background-color:red");

                    }
                }
            };
        });
    }

    private void createLoansAsLenderRequest(){
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                //todo parameter name here
                .parse(Constants.LOANS_AS_LENDER)
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
                        System.out.println("failed to call url information body controller")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            clientAsLenderLoanList.clear();
                            String jsonOfClientString = response.body().string();
                            // response.body().close();
                            Gson gson = new Gson();
                            Loan[] loanAsLenderList = new Gson().fromJson(jsonOfClientString, Loan[].class);
                            clientAsLenderLoanList.addAll(loanAsLenderList);
                            lenderTable.setItems(clientAsLenderLoanList);
                            customiseFactory(lenderLoanStatus);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }

    private void createLoansAsBorrowerRequest(){
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                //todo parameter name here
                .parse(Constants.LOANS_AS_BORROW)
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
                        System.out.println("failed to call url information body controller")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            clientAsBorrowLoanList.clear();
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            Loan[] loanAsBorrowList = new Gson().fromJson(jsonOfClientString, Loan[].class);
                            clientAsBorrowLoanList.addAll(loanAsBorrowList);
                            borrowerTable.setItems(clientAsBorrowLoanList);
                            customiseFactory(borrowerLoanStatus);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }

    private void createTransactionRequest(int amount){
        String finalUrl = HttpUrl
                //todo parameter amount
                .parse(Constants.CREATE_TRANSACTION)
                .newBuilder()
                .addQueryParameter("amount", String.valueOf(amount))
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
                            //Notifications.create().title("Success").text(response.body().string()).hideAfter(Duration.seconds(5)).position(Pos.CENTER).show();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,response.body().string());
                            alert.showAndWait();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

            });

        }
    });


    }

    public Client getCurrClient(){
        return customerMainBodyController.getCurrClient();
    }
}
