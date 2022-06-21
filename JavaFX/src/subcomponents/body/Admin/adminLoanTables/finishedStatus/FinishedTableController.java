package subcomponents.body.Admin.adminLoanTables.finishedStatus;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import loan.Loan;
import loan.enums.eLoanStatus;
import subcomponents.body.Admin.adminLoanTables.finishedStatus.innerTable.finishedInnerTableController;
import engine.Engine;

import java.io.IOException;

public class FinishedTableController {

    Engine engine =Engine.getInstance();


    @FXML
    private TableColumn<Loan, String> ColumnId;

    @FXML
    private TableColumn<Loan, String> ColumnName;

    @FXML
    private TableColumn<Loan, eLoanStatus> ColumnStatus;

    @FXML
    private TableColumn<Loan, String> ColumnCategory;

    @FXML
    private TableColumn<Loan, Double> ColumnAmount;

    @FXML
    private TableColumn<Loan, Integer> ColumnTotalYaz;

    @FXML
    private TableColumn<Loan, Integer> ColumnPayEvery;

    @FXML
    private TableColumn<Loan, Double> ColumnInterest;

    @FXML
    private TableColumn<Loan, Button> LendersColumn;


    @FXML
    private TableColumn<Loan, Integer> ActiveStatusYaz;

    @FXML
    private TableColumn<Loan, Integer> NextPaymentColumn;

    @FXML
    private TableColumn<Loan, Integer> FinishedStatusYaz;


    @FXML
    private TableView<Loan> FinishedTable;

    ObservableList<Loan> loanObservableList;

    public void initialize() {
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<Loan, Double>("loanOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<Loan, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<Loan, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));
        ActiveStatusYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("startLoanYaz"));
        NextPaymentColumn.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("nextYazToPay"));
        FinishedStatusYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("endLoanYaz"));
        LendersColumn.setCellValueFactory(new PropertyValueFactory<Loan, Button>("infoButton"));

        loanObservableList = engine.getDatabase().o_getAllLoansByStatus(eLoanStatus.FINISHED);
        for (Loan loan:loanObservableList){
            loan.getInfoButton().setOnAction(event -> ActiveActionHandle(loan));
        }

        FinishedTable.setItems(loanObservableList);

    }

    public void ActiveActionHandle(Loan loan){
        //create stage
        Stage stage = new Stage();
        stage.setTitle("lenders info");
        //load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("innerTable/finishedInnerTable.fxml"));
        AnchorPane finishedInnerTable = null;
        try {
            finishedInnerTable = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get the controller
        finishedInnerTableController innerTableController = fxmlLoader.getController();
        innerTableController.initializeTable(loan.getLendersList(),loan.getPaymentsList());
        Scene scene = new Scene(finishedInnerTable);
        stage.setScene(scene);
        stage.show();
    }
}
