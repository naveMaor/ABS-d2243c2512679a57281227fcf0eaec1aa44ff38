package admin.adminLoanTables.finishedStatus;

import admin.adminLoanTables.InnerTablesRefresher;
import admin.adminLoanTables.finishedStatus.innerTable.finishedInnerTableController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import loan.enums.eLoanStatus;
import servletDTO.admin.AdminLoanObj;
import servletDTO.admin.InnerTableObj;
import util.AddJavaFXCell;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FinishedTableController {


    public final static int REFRESH_RATE = 2000;
    private final BooleanProperty autoUpdate = new SimpleBooleanProperty(true);
    ObservableList<AdminLoanObj> loanObservableList = FXCollections.observableArrayList();
    private Timer timer;
    private TimerTask listRefresher;
    private finishedInnerTableController innerTableController;
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
        AddJavaFXCell.addButtonToTable(FinishedTable, this::openLoanDetails, "show", "Lenders&Payments");
    }


    public void initializeTable(List<AdminLoanObj> adminLoanObj) {
        loanObservableList.clear();
        FinishedTable.getItems().clear();
        for (AdminLoanObj loanObj : adminLoanObj) {
            if (loanObj.getStatus() == eLoanStatus.FINISHED) {
                loanObservableList.add(loanObj);
            }
        }
        FinishedTable.setItems(loanObservableList);
    }


    private void openLoanDetails(AdminLoanObj adminLoanObj) {
        activeActionHandle(adminLoanObj.getLoanID());
    }


    private void activeActionHandle(String loanName) {
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
        innerTableController = fxmlLoader.getController();
        startLoanListRefresher(loanName);
        Scene scene = new Scene(finishedInnerTable);
        stage.setScene(scene);
        stage.show();
    }

    private void loadInnerTableData(InnerTableObj innerTableObj) {
        innerTableController.loadTableData(innerTableObj);
    }


    public void startLoanListRefresher(String loanName) {
        listRefresher = new InnerTablesRefresher(
                this::loadInnerTableData,
                autoUpdate,
                loanName
        );
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }
}
