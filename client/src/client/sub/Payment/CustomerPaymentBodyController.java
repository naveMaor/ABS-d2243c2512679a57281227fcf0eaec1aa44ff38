package client.sub.Payment;

import client.sub.main.CustomerMainBodyController;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import loan.Loan;
import loan.enums.eLoanStatus;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import servletDTO.ClientDTOforServlet;
import servletDTO.Payment.LoanPaymentObj;
import servletDTO.Payment.PartialPaymentObj;
import engine.Engine;
import util.Constants;
import util.AddJavaFXCell;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.*;

public class CustomerPaymentBodyController {
    private Engine engine = new Engine();

    private ObservableList<String> CheckBoxLoanList =  FXCollections.observableArrayList();
    private ObservableList<LoanPaymentObj> loanListForTable =  FXCollections.observableArrayList();
    private ObservableList<Loan> loanListForTextArea =  FXCollections.observableArrayList();
    private CustomerMainBodyController customerMainBodyController;
    private Map<String, String> clientNameCommentMap =new HashMap<>();
    private SimpleBooleanProperty loadTextAfterYazChange = new SimpleBooleanProperty();

    @FXML
    private TextField partialAmoutLable;

    @FXML
    private ListView<String> LoansListView;

    @FXML
    private TableColumn<LoanPaymentObj, Double> currPay;

    @FXML
    private TableColumn<LoanPaymentObj, Double> leftPay;

    @FXML
    private Button closeEntireLoanButton;

    @FXML
    private TableColumn<LoanPaymentObj, String> loanId;


    @FXML
    private TableColumn<LoanPaymentObj, Integer> nextYaz;

    @FXML
    private TextArea notificationsTextArea;

    @FXML
    private Button pay;


    @FXML
    private TableView<LoanPaymentObj> ReleventLoansTable;

    @FXML
    private TableColumn<LoanPaymentObj, eLoanStatus> status;

    @FXML
    void activateBackwardLoanButton(ActionEvent event) {
        List<String> choosenLoans=LoansListView.getSelectionModel().getSelectedItems();
        LoansListView.getItems().removeAll(choosenLoans);
    }

    private List<Loan> PayLoanstmp = new ArrayList<>();

    private void payLoans(PayOption payOption){
        if(payOption==PayOption.entire)
            payEntirePaymentForLoanListRequest();
        else if (payOption==PayOption.single){
            paySinglePaymentForLoanListRequest();
        }
        //loadLoanTableData();
        LoansListView.getItems().clear();
    }


    private void payEntirePaymentForLoanListRequest(){
        String[] loanNameList = LoansListView.getItems().toArray(new String[0]);

        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(loanNameList,String[].class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.PAY_ENTIRE_PAYMENT)
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
                        System.out.println("failed to call url pay Entire Payment")
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
                        loadReleventLoansTable(jsonOfClientString);
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,jsonOfClientString);
                        alert.showAndWait();
                    }
                });
            }

        });

    }


    private void paySinglePaymentForLoanListRequest(){
        String[] loanNameList = LoansListView.getItems().toArray(new String[0]);

        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(loanNameList,String[].class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.PAY_SINGLE_PAYMENT)
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
                        System.out.println("failed to call url payment body")
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
                        loadReleventLoansTable(jsonOfClientString);
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,jsonOfClientString);
                        alert.showAndWait();
                    }
                });
            }

        });

    }



    private void loadReleventLoansTable(String jsonOfClientString){
        loanListForTable.clear();
        LoanPaymentObj[] availableLoans = new Gson().fromJson(jsonOfClientString, LoanPaymentObj[].class);
        loanListForTable.addAll(Arrays.asList(availableLoans));
        ReleventLoansTable.setItems(loanListForTable);
        LoansListView.getItems().clear();
    }



    @FXML
    void activateCloseEntireLoanButton(ActionEvent event) {
        payLoans(PayOption.entire);
    }


    @FXML
    void activateForwardLoanButton(ActionEvent event) {
        ObservableList<LoanPaymentObj> items = ReleventLoansTable.getItems();
        int num = 0;
        for (LoanPaymentObj loan:items){
            if (((loan.getNextExpectedPaymentAmountDataMember() > 0) && (loan.getNextYazToPay() == 0)) || loan.getStatus() == eLoanStatus.RISK) {
                if (loan.isSelect()) {
                    CheckBoxLoanList.add(loan.getLoanID());
                    num++;
                }
            }
        }
        if(num==0){
            Alert alert = new Alert(Alert.AlertType.ERROR,"NO LOANS SELECTED!\nPlease select by the check box \nYou can only choose loans that has next yaz 0 and loans that you not payed already Or loans that are on risk");
            alert.showAndWait();
        }
        else {
            for(String loanID:CheckBoxLoanList){
                if(!LoansListView.getItems().contains(loanID)){
                    LoansListView.getItems().add(loanID);
                }
            }
        }
        for (LoanPaymentObj loanPaymentObj:items) {
            loanPaymentObj.setSelect(false);
        }
        CheckBoxLoanList.clear();
    }


    @FXML
    void activatePayLoanSinglePaymentButton(ActionEvent event) {
        payLoans(PayOption.single);
    }

    @FXML
    void activatePayPartial(ActionEvent event) {
        int amount = Integer.parseInt(partialAmoutLable.getText());
        payPartialAmountForLoanRequest(amount);
        //loadLoanTableData();
    }


    private void payPartialAmountForLoanRequest(int amount){
        String[] loanNameList = LoansListView.getItems().toArray(new String[0]);

        PartialPaymentObj partialPaymentObj = new PartialPaymentObj(loanNameList,amount);
        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(partialPaymentObj,PartialPaymentObj.class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.PAY_PARTIAL_PAYMENT)
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
                        System.out.println("failed to call url payment body")
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
                        loadReleventLoansTable(jsonOfClientString);
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,jsonOfClientString);
                        alert.showAndWait();
                    }
                });
            }
        });
    }


    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    public void initialize() {
        //LoanPaymentObj loanPaymentObj = new LoanPaymentObj(200,500,"stam",0,eLoanStatus.ACTIVE);

        currPay.setCellValueFactory(new PropertyValueFactory<LoanPaymentObj, Double>("nextExpectedPaymentAmountDataMember"));
        leftPay.setCellValueFactory(new PropertyValueFactory<LoanPaymentObj, Double>("totalRemainingLoan"));
        loanId.setCellValueFactory(new PropertyValueFactory<LoanPaymentObj, String>("loanID"));
        nextYaz.setCellValueFactory(new PropertyValueFactory<LoanPaymentObj, Integer>("nextYazToPay"));
        //select.setCellValueFactory(new PropertyValueFactory<LoanPaymentObj, Boolean>("select"));
        status.setCellValueFactory(new PropertyValueFactory<LoanPaymentObj, eLoanStatus>("status"));


        AddJavaFXCell.addCheckBoxCellPayment(ReleventLoansTable);


        //ReleventLoansTable.getItems().add(loanPaymentObj);

    }

    public void bindProperties(SimpleBooleanProperty yazChanged){
        loadTextAfterYazChange.bindBidirectional(yazChanged);
    }

/*
    public void loadLoanTableData(){
        loadTextAreaData();
        updatePaymentLoanListRequest();

    }
*/


    //todo:add get servlet for loan list from database
    public void loadTextAreaData(){
/*        StringBuilder comment = new StringBuilder("Yaz now:" + Timeline.getCurrTime() + ", you need to pay for these loans:\n");
        ObservableList<Loan> tmp =  engine.getDatabase().o_getAllLoansByClientName(customerMainBodyController.getCustomerName());
        int num=1;
        boolean append = false;
        for (Loan loan:tmp) {
            eLoanStatus status = loan.getStatus();
            if((status== eLoanStatus.RISK)||(status==eLoanStatus.ACTIVE) && loan.getNextYazToPay() ==0){
                append = true;
                comment.append(num).append(". ").append(loan.getLoanID()).append("\n");
                num++;
            }
        }
        comment.append("\n");
        if(append&&loadTextAfterYazChange.getValue()){
            clientNameCommentMap.put(customerMainBodyController.getCustomerName(), String.valueOf(comment));
            notificationsTextArea.clear();
            notificationsTextArea.setText(clientNameCommentMap.get(customerMainBodyController.getCustomerName()));
        }*/

        ClientDTOforServlet clientDTOforServlet = customerMainBodyController.getCurrClient();
        notificationsTextArea.clear();
        notificationsTextArea.setText(clientDTOforServlet.getNotification());


    }

    private void customiseFactory(TableColumn<LoanPaymentObj, eLoanStatus> calltypel) {
        calltypel.setCellFactory(column -> {
            return new TableCell<LoanPaymentObj, eLoanStatus>() {
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


/*
    private void updatePaymentLoanListRequest(){

        String finalUrl = HttpUrl
                .parse(Constants.GET_PAYMENT_LOAN_LIST)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url payment load table")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            loanListForTable.clear();
                            ReleventLoansTable.getItems().clear();
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            LoanPaymentObj[] tmp = new Gson().fromJson(jsonOfClientString, LoanPaymentObj[].class);
                            loanListForTable.addAll(tmp);
                            ReleventLoansTable.setItems(loanListForTable);
                            customiseFactory(status);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
*/


    public void loadPaymentData(List<LoanPaymentObj> loanPaymentObjs){
        loadTextAreaData();
        synchronized (this){
            loanListForTable.clear();
            ReleventLoansTable.getItems().clear();
            loanListForTable.addAll(loanPaymentObjs);
            ReleventLoansTable.setItems(loanListForTable);
        }

        customiseFactory(status);

    }

}
