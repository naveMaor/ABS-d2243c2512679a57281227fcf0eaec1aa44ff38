package client.sub.Information;

import Money.operations.Transaction;
import client.sub.Information.transactionsTableView.TransactionsController;
import client.sub.main.CustomerMainBodyController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import loan.enums.eLoanStatus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import servletDTO.ClientDTOforServlet;
import servletDTO.LoanInformationObj;
import util.AddJavaFXCell;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.List;

public class CustomerInformationBodyCont {

    private CustomerMainBodyController customerMainBodyController;

    @FXML
    private BorderPane transactions;
    @FXML
    private TransactionsController transactionsController;

    @FXML
    private TableColumn<LoanInformationObj, String> borrowerLoanID;

    @FXML
    private TableColumn<LoanInformationObj, String> lenderLoanID;

    @FXML
    private TableColumn<LoanInformationObj, String> borrowerLoanCategory;

    @FXML
    private TableColumn<LoanInformationObj, String> lenderLoanCat;

    @FXML
    private TableColumn<LoanInformationObj, eLoanStatus> borrowerLoanStatus;

    @FXML
    private TableColumn<LoanInformationObj, eLoanStatus> lenderLoanStatus;

    @FXML
    private TableColumn<LoanInformationObj, String> lenderBorrowerName;

    @FXML
    private TableColumn<LoanInformationObj, Double> sellPrice;

    @FXML
    private TableView<LoanInformationObj> lenderTable;

    @FXML
    private TableView<LoanInformationObj> borrowerTable;

    private ObservableList<LoanInformationObj> clientAsLenderLoanList = FXCollections.observableArrayList();
    private ObservableList<LoanInformationObj> clientAsBorrowLoanList = FXCollections.observableArrayList();


    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }


    @FXML
    public void initialize() {
        clientAsLenderLoanList = FXCollections.observableArrayList();
        clientAsBorrowLoanList = FXCollections.observableArrayList();
        if (transactionsController != null) {
            transactionsController.setMainController(this);
        }
        borrowerLoanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        lenderLoanID.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        borrowerLoanCategory.setCellValueFactory(new PropertyValueFactory<>("loanCategory"));
        lenderLoanCat.setCellValueFactory(new PropertyValueFactory<>("loanCategory"));
        lenderBorrowerName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        borrowerLoanStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        lenderLoanStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        AddJavaFXCell.addButtonToTable(lenderTable,
                this::putLoanOnSaleRequest,
                "Sell");

    }

    public void bindDisable(BooleanProperty booleanProperty) {
        transactionsController.bindDisable(booleanProperty);
        lenderTable.disableProperty().bind(booleanProperty);

    }

    public void loadClientTransactions(List<Transaction> transactionList) {

        transactionsController.loadClientTransactions(transactionList);
    }

    private void customiseFactory(TableColumn<LoanInformationObj, eLoanStatus> calltypel) {
        calltypel.setCellFactory(column -> {
            return new TableCell<LoanInformationObj, eLoanStatus>() {
                @Override
                protected void updateItem(eLoanStatus item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<LoanInformationObj> currentRow = getTableRow();

                    if (!isEmpty()) {

                        if (item == eLoanStatus.RISK)
                            currentRow.setStyle("-fx-background-color:red");

                    }
                }
            };
        });
    }

    public ClientDTOforServlet getCurrClient() {
        return customerMainBodyController.getCurrClient();
    }

    private void putLoanOnSaleRequest(LoanInformationObj loan) {

        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(loan.getLoanID(), String.class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.PUT_LOAN_ON_SELL)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();


        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url put loan on sale")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    String jsonOfClientString = null;
                    try {
                        jsonOfClientString = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.body().close();

                    if (response.code() == 200) {
                        //customerMainBodyController.loadData();
                        loan.setOnSale(true);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "loan " + loan.getLoanID() + "is now on sale!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, jsonOfClientString);
                        alert.showAndWait();
                    }
                });
            }

        });
    }

    public void loadLenderLoanTable(List<LoanInformationObj> loanInformationObjs) {
        synchronized (this) {
            clientAsLenderLoanList.clear();
            clientAsLenderLoanList.addAll(loanInformationObjs);
        }
        if (customerMainBodyController.getCurrClient() != null) {
            lenderTable.setItems(clientAsLenderLoanList);
            customiseFactory(lenderLoanStatus);
        }
    }

    public void loadBorrowerLoanTable(List<LoanInformationObj> loanInformationObjs) {
        synchronized (this) {
            clientAsBorrowLoanList.clear();
            clientAsBorrowLoanList.addAll(loanInformationObjs);
        }
        if (customerMainBodyController.getCurrClient() != null) {
            borrowerTable.setItems(clientAsBorrowLoanList);
            customiseFactory(borrowerLoanStatus);
        }

    }
}
