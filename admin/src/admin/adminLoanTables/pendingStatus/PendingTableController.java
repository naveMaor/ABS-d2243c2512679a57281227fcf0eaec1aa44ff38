package admin.adminLoanTables.pendingStatus;

import admin.adminLoanTables.InnerTablesRefresher;
import admin.adminLoanTables.adminLoanTablesMain.AdminLoanTablesController;
import admin.adminLoanTables.finishedStatus.innerTable.finishedInnerTableController;
import admin.adminLoanTables.pendingStatus.innerTable.PendingInnerTableController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import loan.enums.eLoanStatus;
import engine.Engine;
import servletDTO.admin.AdminLoanObj;
import servletDTO.admin.InnerTableObj;
import util.AddJavaFXCell;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PendingTableController {

    Engine engine =Engine.getInstance();

    private AdminLoanTablesController mainTablesController;

    private Timer timer;
    private TimerTask listRefresher;
    public final static int REFRESH_RATE = 2000;
    private final BooleanProperty autoUpdate = new SimpleBooleanProperty(true);
    private PendingInnerTableController innerTableController;

    public void setMainController(AdminLoanTablesController mainTablesController){
        this.mainTablesController = mainTablesController;
    }
    @FXML
    private TableView<AdminLoanObj> PendingTable;

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
    private TableColumn<AdminLoanObj, Double> RaisedColmun;

    @FXML
    private TableColumn<AdminLoanObj, Double> LeftToMakeActive;

    ObservableList<AdminLoanObj> loanObservableList = FXCollections.observableArrayList();




/*    public void addButtonToTable(TableView<AdminLoanObj> table) {
        TableColumn<Loan, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<Loan, Void>, TableCell<Loan, Void>> cellFactory = new Callback<TableColumn<Loan, Void>, TableCell<Loan, Void>>() {
            @Override
            public TableCell<Loan, Void> call(final TableColumn<Loan, Void> param) {
                final TableCell<Loan, Void> cell = new TableCell<Loan, Void>() {

                    private final Button btn = new Button("Action");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Loan data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data.getStatus());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        table.getColumns().add(colBtn);

    }*/

    public void initialize() {
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("loanOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("interestPercentagePerTimeUnit"));
        LeftToMakeActive.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("missingMoney"));
        RaisedColmun.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("totalRaisedDeposit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, eLoanStatus>("status"));
        AddJavaFXCell.addButtonToTable(PendingTable,this::openLoanDetails,"show","lenders");
    }


        public void initializeTable(List<AdminLoanObj> adminLoanObj) {
        loanObservableList.clear();
        PendingTable.getItems().clear();
        for(AdminLoanObj loanObj:adminLoanObj){
            if(loanObj.getStatus()==eLoanStatus.PENDING){
                loanObservableList.add(loanObj);
            }
        }
        PendingTable.setItems(loanObservableList);

    }
/*
    public void ActiveActionHandle(Loan loan){
        //create stage
        Stage stage = new Stage();
        stage.setTitle("lenders info");
        //load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("innerTable/PendingInnerTable.fxml"));
        AnchorPane pendingInnerTable = null;
        try {
            pendingInnerTable = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get the controller
        PendingInnerTableController innerTableController = fxmlLoader.getController();
        innerTableController.initializeTable(loan.getLendersList());
        Scene scene = new Scene(pendingInnerTable);
        stage.setScene(scene);
        stage.show();
    }
*/

    private void openLoanDetails(AdminLoanObj adminLoanObj){
        activeActionHandle(adminLoanObj.getLoanID());
    }



    private void activeActionHandle(String loanName){
        //create stage
        Stage stage = new Stage();
        stage.setTitle("lenders info");
        //load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("innerTable/PendingInnerTable.fxml"));
        AnchorPane pendingInnerTable = null;
        try {
            pendingInnerTable = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get the controller
        innerTableController = fxmlLoader.getController();
        startLoanListRefresher(loanName);
        Scene scene = new Scene(pendingInnerTable);
        stage.setScene(scene);
        stage.show();
    }

    private void loadInnerTableData(InnerTableObj innerTableObj){
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
