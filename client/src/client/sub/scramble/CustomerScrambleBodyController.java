package client.sub.scramble;

import client.sub.main.CustomerMainBodyController;
import com.google.gson.Gson;
import com.oracle.webservices.internal.api.message.ContentType;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import engine.Engine;
import engine.scrambleService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import loan.Loan;
import loan.enums.eLoanStatus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import servletDTO.ScrambleRequestObj;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerScrambleBodyController {
    //    private Engine engine=Engine.getInstance();
    private Engine engine;
    private CustomerMainBodyController customerMainBodyController;
    private List<String> allCategoriesList;
    private ObservableList<Loan> userFilteredLoanList = FXCollections.observableArrayList();
    private ObservableList<Loan> CheckBoxLoanList = FXCollections.observableArrayList();

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
    private TableColumn<Loan, Double> TotalLoanCost;

    @FXML
    private TableView<Loan> ReleventLoansTable;


    @FXML
    void activateActivateScrambleButton(ActionEvent event) {
        int num = 0;
        ObservableList<Loan> items = ReleventLoansTable.getItems();
        clientName = customerMainBodyController.getCustomerName();
        for (Loan loan : items) {
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
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new Gson();
            ScrambleRequestObj requestObj = new ScrambleRequestObj(CheckBoxLoanList,amount,clientName,maxOwnership);

            String requestString = gson.toJson(requestObj,ScrambleRequestObj.class);

            RequestBody body = RequestBody.create(requestString,JSON);
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
                            resetFileds();
                            resetRelevantLoansTable();
                            customerMainBodyController.loadData();

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

/*    @FXML
    void activateMinimumInterestTextField(ActionEvent event) {

    }*/


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


        Gson gson = new Gson();
        String jsonExistChosenCategories = gson.toJson(existChosenCategories);


        String finalUrl = HttpUrl
                .parse(Constants.RELEVANT_LOANS)
                .newBuilder()
                .addQueryParameter("username",clientName )
                .addQueryParameter("minInterest", String.valueOf(minInterest))
                .addQueryParameter("minYaz", String.valueOf(minYaz))
                .addQueryParameter("maxOpenLoans", String.valueOf(maxOpenLoans))
                .addQueryParameter("existChoosenCategories", jsonExistChosenCategories)
                .addQueryParameter("maxOwnership", String.valueOf(maxOwnership))
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
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
                        if(response.code()==200){
                            String jsonOfClientString = response.body().string();
                            // response.body().close();
                            scrambleService service= new Gson().fromJson(jsonOfClientString, scrambleService.class);
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

                            double clientBalance = service.getClient().getMyAccount().getCurrBalance();

                            if (amount > clientBalance || amount == 0) {
                                Alert a = new Alert(Alert.AlertType.ERROR, "Amount must be set to less then client current balance:\n" + clientBalance);
                                a.showAndWait();
                            } else {
                                service.start();
                            }
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
        engine = new Engine();
        allCategoriesList = engine.getDatabase().getAllCategories();
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<Loan, Double>("loanOriginalDepth"));
        TotalLoanCost.setCellValueFactory(new PropertyValueFactory<Loan, Double>("totalLoanCostInterestPlusOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<Loan, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanCategory"));
        ColumnCheckBox.setCellValueFactory(new PropertyValueFactory<Loan, String>("select"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<Loan, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));

    }


    public void resetFileds() {
        ObservableList<Loan> items = ReleventLoansTable.getItems();
        for (Loan loan : items) {
            loan.setSelect(false);
        }
        amountToInvestTextField.clear();
        maxOpenLoansTextField.clear();
        maxOwnershipTextField.clear();
        minimumInterestTextField.clear();
        minimumYazTextField.clear();
        categoriesOptionsListView.getItems().addAll(allCategoriesList);
        userChoiceCategoriesListView.getItems().removeAll(allCategoriesList);
    }

    public void resetRelevantLoansTable() {
        ReleventLoansTable.getItems().clear();
    }
}
