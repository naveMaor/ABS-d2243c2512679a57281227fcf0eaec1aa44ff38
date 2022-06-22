package client.sub.Information;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import client.sub.Information.transactionsTableView.transactionsController;
import client.sub.main.CustomerMainBodyController;
import customes.Client;
import data.File.XmlFile;
import exceptions.BalanceException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loan.Loan;
import loan.enums.eLoanStatus;
import engine.Engine;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.HttpClientUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerInformationBodyCont {
    //private Engine engine= Engine.getInstance();
    private Engine engine = new Engine();

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

/*    //todo: get the two tables information from one servlet
    public void initializeClientTable(){
        clientAsBorrowLoanList.clear();
        clientAsBorrowLoanList.addAll(engine.getDatabase().getClientByname("Avrum").getClientAsBorrowLoanList());
        //tmp
        //clientAsBorrowLoanList.addAll(engine.getDatabase().getClientByname(customerNameProperty().get()).getClientAsBorrowLoanList());
        borrowerTable.setItems(clientAsBorrowLoanList);
        customiseFactory(borrowerLoanStatus);


        clientAsLenderLoanList.clear();
        //tmp
        clientAsLenderLoanList.addAll(engine.getDatabase().getClientByname("Avrum").getClientAsLenderLoanList());
        //clientAsLenderLoanList.addAll(engine.getDatabase().getClientByname(customerNameProperty().get()).getClientAsLenderLoanList());
        lenderTable.setItems(clientAsLenderLoanList);
        customiseFactory(lenderLoanStatus);

    }*/

    //todo: get the two tables information from one servlet
    public void initializeClientTable(){
        clientAsLenderLoanList.clear();
        clientAsBorrowLoanList.clear();

        //String userName = userNameTextField.getText();
        String userName = "Avrum";

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                //todo parameter name here
                .parse(Constants.LOANS_PER_CUSTOMER)
                .newBuilder()
                .addQueryParameter("username", "Avrum")
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
                                String jsonOfClientString = response.body().string();
                                // response.body().close();
                                Loan[] loanList = new Gson().fromJson(jsonOfClientString, Loan[].class);
                                clientAsBorrowLoanList.addAll(loanList);
                                borrowerTable.setItems(clientAsBorrowLoanList);
                                customiseFactory(borrowerLoanStatus);
                            }

                            //TODO: A LOAN LIST NEEDED HERE ALSO:
/*                            clientAsLenderLoanList.addAll(loansList);
                            lenderTable.setItems(clientAsLenderLoanList);
                            customiseFactory(lenderLoanStatus);*/
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
        //tmp
        initializeClientTable();
    }

    //todo: add a servlet here
    public void createTransaction(int amount){
        try {
            engine.AccountTransaction(amount,customerMainBodyController.customerNameProperty().get());
        } catch (BalanceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Balance can not be minus!");
            alert.setTitle("Error");
            alert.showAndWait();
        }
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



    private Stage primaryStage;


}
