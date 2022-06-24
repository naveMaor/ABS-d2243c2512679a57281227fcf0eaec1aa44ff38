package client.sub.Payment;

import client.sub.main.CustomerMainBodyController;
import com.google.gson.Gson;
import exceptions.messageException;
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
import time.Timeline;
import engine.Engine;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.*;

public class CustomerPaymentBodyController {
//    private Engine engine=Engine.getInstance();
    private Engine engine = new Engine();

    private ObservableList<String> CheckBoxLoanList =  FXCollections.observableArrayList();
    private ObservableList<Loan> loanListForTable =  FXCollections.observableArrayList();
    private ObservableList<Loan> loanListForTextArea =  FXCollections.observableArrayList();
    private CustomerMainBodyController customerMainBodyController;
    private Map<String, String> clientNameCommentMap =new HashMap<>();
    private SimpleBooleanProperty loadTextAfterYazChange = new SimpleBooleanProperty();

    @FXML
    private TextField partialAmoutLable;

    @FXML
    private ListView<String> LoansListView;

    @FXML
    private TableColumn<Loan, Double> currPay;

    @FXML
    private TableColumn<Loan, Double> leftPay;

    @FXML
    private Button closeEntireLoanButton;

    @FXML
    private TableColumn<Loan, String> loanId;


    @FXML
    private TableColumn<Loan, Integer> nextYaz;

    @FXML
    private TextArea notificationsTextArea;

    @FXML
    private Button pay;

    @FXML
    private TableColumn<Loan, String> select;

    @FXML
    private TableView<Loan> ReleventLoansTable;

    @FXML
    private TableColumn<Loan, eLoanStatus> status;

    @FXML
    void activateBackwardLoanButton(ActionEvent event) {
        List<String> choosenLoans=LoansListView.getSelectionModel().getSelectedItems();
        LoansListView.getItems().removeAll(choosenLoans);
    }

    private List<Loan> PayLoanstmp = new ArrayList<>();

    //todo: add servlet here
    private void payLoans(PayOption payOption){
        List<String> loanNameList = LoansListView.getItems();
        //createAllLoanListRequest();
        ObservableList <Loan> loanList = FXCollections.observableArrayList();
        for (Loan loan:PayLoanstmp){
            if (loanNameList.contains(loan.getLoanID())){
                loanList.add(loan);
            }
        }
        try {
            if(payOption==PayOption.entire)
                engine.payEntirePaymentForLoanList(loanList);
            else if (payOption==PayOption.single){
                engine.paySinglePaymentForLoanList(loanList);
            }
        } catch (messageException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
            alert.showAndWait();
        }
        loadLoanTableData();
        LoansListView.getItems().clear();
    }


    private void payEntirePaymentForLoanList(){
        List<String> loanNameList = LoansListView.getItems();

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

    }



    @FXML
    void activateCloseEntireLoanButton(ActionEvent event) {
/*        List<String> loanNameList = LoansListView.getItems();
        List<Loan> tmp = engine.getDatabase().getLoanList();
        ObservableList <Loan> loanList = FXCollections.observableArrayList();
        for (Loan loan:tmp){
            if (loanNameList.contains(loan.getLoanID())){
                loanList.add(loan);
            }
        }


        try {
            engine.payEntirePaymentForLoanList(loanList);
        } catch (messageException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
            alert.showAndWait();
        }
        loadLoanTableData();
        LoansListView.getItems().clear();*/
        payLoans(PayOption.entire);
    }

    @FXML
    void activateForwardLoanButton(ActionEvent event) {
        ObservableList<Loan> items = ReleventLoansTable.getItems();
        int num = 0;
        for (Loan loan:items){
            if (((loan.getNextExpectedPaymentAmountDataMember() > 0) && (loan.getNextYazToPay() == 0)) || loan.getStatus() == eLoanStatus.RISK) {
/*                if (loan.getSelect().isSelected()) {
                    CheckBoxLoanList.add(loan.getLoanID());
                    num++;*/
                    if (loan.getSelect()) {
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
        for (Loan loan:items) {

            //loan.getSelect().setSelected(false);
            loan.setSelect(false);

        }
        CheckBoxLoanList.clear();
    }


    @FXML
    void activatePayLoanSinglePaymentButton(ActionEvent event) {
/*        List<String> loanNameList = LoansListView.getItems();
        List<Loan> tmp = engine.getDatabase().getLoanList();
        ObservableList <Loan> loanList = FXCollections.observableArrayList();
        for (Loan loan:tmp){
            if (loanNameList.contains(loan.getLoanID())){
                loanList.add(loan);
            }
        }

        try {
            engine.paySinglePaymentForLoanList(loanList);
        } catch (messageException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
            alert.showAndWait();
        }
        loadLoanTableData();
        LoansListView.getItems().clear();*/
        payLoans(PayOption.single);
    }

    @FXML
    void activatePayPartial(ActionEvent event) {
        int amount = Integer.parseInt(partialAmoutLable.getText());
        List<String> loanNameList = LoansListView.getItems();
        List<Loan> tmp = engine.getDatabase().getLoanList();
        ObservableList <Loan> loanList = FXCollections.observableArrayList();
        if(loanNameList.size()!=1){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please choose only one loan at a time for paying that partial amount!");
            alert.showAndWait();
        }
        else {
            for (Loan loan:tmp){
                if (loanNameList.contains(loan.getLoanID())){
                    loanList.add(loan);
                }
            }
            try {
                engine.payPartialAmountForLoan(loanList.get(0),amount);
            } catch (messageException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage());
                alert.showAndWait();            }
        }
        loadLoanTableData();
        LoansListView.getItems().clear();
    }

    private List<Loan> choosenLoans;

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    public void initialize() {
        currPay.setCellValueFactory(new PropertyValueFactory<Loan, Double>("nextExpectedPaymentAmountDataMember"));
        leftPay.setCellValueFactory(new PropertyValueFactory<Loan, Double>("totalRemainingLoan"));
        loanId.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        nextYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("nextYazToPay"));
        select.setCellValueFactory(new PropertyValueFactory<Loan, String>("select"));
        status.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));

    }

    public void bindProperties(SimpleBooleanProperty yazChanged){
        loadTextAfterYazChange.bindBidirectional(yazChanged);
    }

    public void loadLoanTableData(){
        loadTextAreaData();
        loanListForTable.clear();
        ReleventLoansTable.getItems().clear();

        ObservableList<Loan> tmp =  engine.getDatabase().o_getAllLoansByClientName(customerMainBodyController.getCustomerName());
        for (Loan loan:tmp
             ) {
            eLoanStatus status = loan.getStatus();
            if((status== eLoanStatus.RISK)||(status==eLoanStatus.ACTIVE)){
                loanListForTable.add(loan);
            }
        }
        //because of unexpected bug that the compliler does not run on all over the items i have to write it down again

        ReleventLoansTable.setItems(loanListForTable);

        customiseFactory(status);

    }


    //todo:add get servlet for loan list from database
    public void loadTextAreaData(){
        StringBuilder comment = new StringBuilder("Yaz now:" + Timeline.getCurrTime() + ", you need to pay for these loans:\n");
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
        }


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





}
