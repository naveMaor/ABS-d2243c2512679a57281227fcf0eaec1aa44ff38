package client.sub.scramble;

import client.sub.main.CustomerMainBodyController;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import loan.enums.eLoanStatus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import servletDTO.LoanInformationObj;
import servletDTO.RelevantLoansRequestObj;
import servletDTO.ScrambleRequestObj;
import util.AddCheckBoxCell;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CustomerScrambleBodyController {

    private CustomerMainBodyController customerMainBodyController;
    private Set<String> allCategoriesList;
    private ObservableList<LoanInformationObj> userFilteredLoanList = FXCollections.observableArrayList();
    private ObservableList<LoanInformationObj> CheckBoxLoanList = FXCollections.observableArrayList();

    private List<String> chosenCategories;
    private ObservableList<String> existChosenCategories = FXCollections.observableArrayList();

    private int amount;

    private int maxOwnership;

    private int minInterest;
    private int minYaz;
    private int maxOpenLoans;
    private String clientName;
    @FXML
    private StackPane stackPane;


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
    private ListView<String> userChoiceCategoriesListView;

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

//    @FXML
//    private TableColumn<LoanInformationObj, CheckBox> ColumnCheckBox;

    @FXML
    private TableColumn<LoanInformationObj, Double> TotalLoanCost;

    @FXML
    private TableView<LoanInformationObj> ReleventLoansTable;


    @FXML
    void activateActivateScrambleButton(ActionEvent event) {
        int num = 0;
        ObservableList<LoanInformationObj> items = ReleventLoansTable.getItems();
        clientName = customerMainBodyController.getCustomerName();
        for (LoanInformationObj loan : items) {
/*            if(loan.getSelect().isSelected()){
                CheckBoxLoanList.add(loan);
                num++;*/
            if (loan.getSelect()) {
                CheckBoxLoanList.add(loan);
                num++;
            }
        }
        if (num == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "NO LOANS SELECTED!");
            alert.showAndWait();
        } else {

            ScrambleRequestObj requestObj = new ScrambleRequestObj(CheckBoxLoanList, amount, clientName, maxOwnership);
            String requestString = HttpClientUtil.GSON_INST.toJson(requestObj, ScrambleRequestObj.class);
            RequestBody body = RequestBody.create(requestString, HttpClientUtil.JSON);
            String finalUrl = HttpUrl
                    .parse(Constants.SCRAMBLE_LOANS)
                    .newBuilder()
                    .build()
                    .toString();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .post(body)
                    .build();

            HttpClientUtil.runAsync(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Platform.runLater(() ->
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Unknown Error");
                        alert.showAndWait();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                        {
                            try {
                                Alert alert = new Alert(Alert.AlertType.ERROR, response.body().string());
                                alert.showAndWait();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        Platform.runLater(() ->
                        {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "investing completed");
                            alert.showAndWait();
                            resetFields();
                            resetRelevantLoansTable();

                        });
                    }
                    //   return false;
                }
            });
        }

    }


    @FXML
    void activateForwardCategoriesButton(ActionEvent event) {
        chosenCategories = categoriesOptionsListView.getSelectionModel().getSelectedItems();
        existChosenCategories = userChoiceCategoriesListView.getItems();
        for (String category : chosenCategories) {
            if (!existChosenCategories.contains(category)) {
                existChosenCategories.add(category);
                categoriesOptionsListView.getItems().remove(category);
            }
        }
    }

    @FXML
    void activateBackwardCategoriesButton(ActionEvent event) {
        //tsad yamin
        chosenCategories = userChoiceCategoriesListView.getSelectionModel().getSelectedItems();
        //tsad smal
        existChosenCategories = categoriesOptionsListView.getItems();

        for (String category : chosenCategories) {
            if (!existChosenCategories.contains(category)) {
                existChosenCategories.add(category);
                userChoiceCategoriesListView.getItems().remove(category);
            }
        }
    }

    @FXML
    void activateShowRelevantLoansListButton(ActionEvent event) {
        clientName = customerMainBodyController.getCustomerName();
        Alert alert;
        minInterest = -1;
        maxOwnership = -1;
        minYaz = -1;
        amount = 0;
        maxOpenLoans = -1;
        try {
            String minInterestText = minimumInterestTextField.getText();
            if (minInterestText.matches("[0-9]+"))
                minInterest = Integer.parseInt(minInterestText);


            String maxShareText = maxOwnershipTextField.getText();
            if (maxShareText.matches("[0-9]+"))
                maxOwnership = Integer.parseInt(maxShareText);


            String minYazText = minimumYazTextField.getText();
            if (minYazText.matches("[0-9]+"))
                minYaz = Integer.parseInt(minYazText);


            String investmentAmountText = amountToInvestTextField.getText();
            if (investmentAmountText.matches("[0-9]+"))
                amount = Integer.parseInt(investmentAmountText);


            String maxInvolvedLoansText = maxOpenLoansTextField.getText();
            if (maxInvolvedLoansText.matches("[0-9]+"))
                maxOpenLoans = Integer.parseInt(maxInvolvedLoansText);
            //maxOpenLoans = Integer.parseInt(maxOpenLoansTextField.getText());


        } catch (NumberFormatException e) {
            alert = new Alert(Alert.AlertType.ERROR, "invalid parameters");
            alert.showAndWait();
        }
        clientName = customerMainBodyController.getCurrClient().getFullName();
        List<String> n = existChosenCategories;
        RelevantLoansRequestObj requestObj = new  RelevantLoansRequestObj(clientName,amount,n,minInterest,minYaz, maxOpenLoans,maxOwnership) ;

        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(requestObj,RelevantLoansRequestObj.class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = Objects.requireNonNull(HttpUrl
                .parse(Constants.RELEVANT_LOANS))
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
                        System.out.println("failed to call show relevant loans body controller")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if (response.code() == 200) {
                            String jsonOfClientString = Objects.requireNonNull(response.body()).string();
                            Objects.requireNonNull(response.body()).close();
                            ObservableList<LoanInformationObj> filterLoans = FXCollections.observableArrayList();
                            LoanInformationObj[] responseLoans = new Gson().fromJson(jsonOfClientString, LoanInformationObj[].class);
                            filterLoans.addAll(responseLoans);
                            Region veil = new Region();
                            veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
                            veil.setPrefSize(400, 440);
                            ProgressIndicator p = new ProgressIndicator();
                            p.setMaxSize(140, 140);
                            p.setStyle(" -fx-progress-color: orange;");
                            ReleventLoansTable.setItems(filterLoans);

                        }
                        else
                        {
                            Alert a = new Alert(Alert.AlertType.ERROR, response.body().string());
                            a.showAndWait();

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });


    }

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    public void initialize() {

        allCategoriesList = new HashSet<>();
        getAllCategories();
        resetFields();
        AddCheckBoxCell.addCheckBoxCellScramble(ReleventLoansTable);
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Double>("loanOriginalDepth"));
        TotalLoanCost.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Double>("totalLoanCostInterestPlusOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<LoanInformationObj, eLoanStatus>("status"));


    }

    private void getAllCategories() {

        String finalUrl = HttpUrl
                .parse(Constants.CATEGORIES)
                .newBuilder()
                .addQueryParameter("username", clientName)
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
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Categories Not return from server!");
                        alert.showAndWait();
                    } else {
                        try {
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            String[] cat = new Gson().fromJson(jsonOfClientString, String[].class);
                            allCategoriesList.clear();
                            allCategoriesList.addAll(Arrays.asList(cat));
                            categoriesOptionsListView.getItems().addAll(allCategoriesList);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });

            }
        });
    }

    public void resetFields() {
        ObservableList<LoanInformationObj> items = ReleventLoansTable.getItems();
        for (LoanInformationObj loan : items) {
            loan.setSelect(false);
        }
        amountToInvestTextField.clear();
        maxOpenLoansTextField.clear();
        maxOwnershipTextField.clear();
        minimumInterestTextField.clear();
        minimumYazTextField.clear();
        userChoiceCategoriesListView.getItems().removeAll();
        categoriesOptionsListView.getItems().clear();
        userChoiceCategoriesListView.getItems().clear();
        getAllCategories();
    }

    public void resetRelevantLoansTable() {
        ReleventLoansTable.getItems().clear();
    }
}
