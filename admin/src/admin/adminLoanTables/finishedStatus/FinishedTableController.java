package admin.adminLoanTables.finishedStatus;

import admin.adminLoanTables.finishedStatus.innerTable.finishedInnerTableController;
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
import engine.Engine;
import servletDTO.admin.AdminLoanObj;

import java.io.IOException;
import java.util.List;

public class FinishedTableController {

    Engine engine =Engine.getInstance();


    @FXML
    private TableColumn<AdminLoanObj, String> ColumnId;

    @FXML
    private TableColumn<AdminLoanObj, String> ColumnName;

    @FXML
    private TableColumn<AdminLoanObj, eLoanStatus> ColumnStatus;

    @FXML
    private TableColumn<AdminLoanObj, String> ColumnCategory;

    @FXML
    private TableColumn<AdminLoanObj, Double> ColumnAmount;

    @FXML
    private TableColumn<AdminLoanObj, Integer> ColumnTotalYaz;

    @FXML
    private TableColumn<AdminLoanObj, Integer> ColumnPayEvery;

    @FXML
    private TableColumn<AdminLoanObj, Double> ColumnInterest;


    @FXML
    private TableColumn<AdminLoanObj, Integer> ActiveStatusYaz;

    @FXML
    private TableColumn<AdminLoanObj, Integer> NextPaymentColumn;

    @FXML
    private TableColumn<AdminLoanObj, Integer> FinishedStatusYaz;


    @FXML
    private TableView<AdminLoanObj> FinishedTable;

    ObservableList<AdminLoanObj> loanObservableList;

    public void initialize(List<AdminLoanObj> adminLoanObj) {
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("loanOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, eLoanStatus>("status"));
        ActiveStatusYaz.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("startLoanYaz"));
        NextPaymentColumn.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("nextYazToPay"));
        FinishedStatusYaz.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("endLoanYaz"));
    }

/*    public void ActiveActionHandle(Loan loan){
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
    }*/

    public void initializeTable(List<AdminLoanObj> adminLoanObj) {
        loanObservableList.clear();
        FinishedTable.getItems().clear();
        for(AdminLoanObj loanObj:adminLoanObj){
            if(loanObj.getStatus()==eLoanStatus.RISK){
                loanObservableList.add(loanObj);
            }
        }
        FinishedTable.setItems(loanObservableList);
    }
}
