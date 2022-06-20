package subcomponents.body.Customer.Information;

import exceptions.BalanceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import loan.Loan;
import loan.enums.eLoanStatus;
import subcomponents.body.Customer.Information.transactionsTableView.transactionsController;
import subcomponents.body.Customer.main.CustomerMainBodyController;
import utills.Engine;

public class CustomerInformationBodyCont {
    private Engine engine= Engine.getInstance();
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
        clientAsBorrowLoanList.clear();
        clientAsBorrowLoanList.addAll(engine.getDatabase().getClientByname(customerNameProperty().get()).getClientAsBorrowLoanList());
        borrowerTable.setItems(clientAsBorrowLoanList);
        customiseFactory(borrowerLoanStatus);

        clientAsLenderLoanList.clear();
        clientAsLenderLoanList.addAll(engine.getDatabase().getClientByname(customerNameProperty().get()).getClientAsLenderLoanList());
        lenderTable.setItems(clientAsLenderLoanList);
        customiseFactory(lenderLoanStatus);

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

}
