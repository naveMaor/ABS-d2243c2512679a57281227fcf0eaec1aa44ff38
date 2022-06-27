package client.sub.buyLoan;

import client.sub.main.CustomerMainBodyController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loan.enums.eLoanStatus;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import servletDTO.BuyLoanObj;
import servletDTO.LoanInformationObj;
import util.AddJavaFXCell;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;

public class BuyLoanController {
    private CustomerMainBodyController customerMainBodyController;


    @FXML
    private TableColumn<BuyLoanObj, Double> ColumnAmount;

    @FXML
    private TableColumn<BuyLoanObj, String> ColumnCategory;

    @FXML
    private TableColumn<BuyLoanObj, String> ColumnId;

    @FXML
    private TableColumn<BuyLoanObj, Double> ColumnInterest;

    @FXML
    private TableColumn<BuyLoanObj, Double> priceColumn;

    @FXML
    private TableColumn<BuyLoanObj, String> ColumnName;

    @FXML
    private TableColumn<BuyLoanObj, Integer> ColumnPayEvery;

    @FXML
    private TableColumn<BuyLoanObj, String> ColumnLender;

    @FXML
    private TableColumn<BuyLoanObj, Integer> ColumnTotalYaz;


    @FXML
    private TableColumn<BuyLoanObj, Double> TotalLoanCost;

    @FXML
    private TableView<BuyLoanObj> ReleventLoansTable;

    private ObservableList<BuyLoanObj> LoanToBuyList = FXCollections.observableArrayList();


    public void initialize() {
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, Double>("loanOriginalDepth"));
        TotalLoanCost.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, Double>("totalLoanCostInterestPlusOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, Integer>("originalLoanTimeFrame"));
        ColumnLender.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, String>("sellerName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<BuyLoanObj, Double>("price"));
        addBuyButton();
    }

    public void loadTableData(){
        buyLoanTableListRequest();
    }

    private void buyLoanTableListRequest(){
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.GET_LOAN_TO_BUY_LIST)
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
                        System.out.println("failed to call url get loan to buy list")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            LoanToBuyList.clear();
                            String jsonOfClientString = response.body().string();
                            // response.body().close();
                            Gson gson = new Gson();
                            BuyLoanObj[] BuyLoansArray = new Gson().fromJson(jsonOfClientString, BuyLoanObj[].class);
                            LoanToBuyList.addAll(BuyLoansArray);
                            ReleventLoansTable.setItems(LoanToBuyList);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }

    private void buyLoanRequest(BuyLoanObj loan){


        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(loan.getLoanID(),String.class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.BUY_LOAN)
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
                        System.out.println("failed to call url buy loan")
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

                    if(response.code()==200){
                        //loan.setOnSale(false);
                        customerMainBodyController.loadData();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"loan "+loan.getLoanID()+"is now on sale!");
                        alert.showAndWait();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,jsonOfClientString);
                        alert.showAndWait();
                    }
                });
            }

        });

    }

    private void addBuyButton(){
        AddJavaFXCell.addButtonToTable(ReleventLoansTable,this::buyLoanRequest,"Sell");

    }

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

}
