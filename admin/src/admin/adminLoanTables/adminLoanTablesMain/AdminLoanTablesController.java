package admin.adminLoanTables.adminLoanTablesMain;

import admin.adminLoanTables.TablesRefresher;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import loan.Loan;
import admin.adminLoanTables.activeStatus.ActiveTableController;
import admin.adminLoanTables.finishedStatus.FinishedTableController;
import admin.adminLoanTables.newStatus.NewTableController;
import admin.adminLoanTables.pendingStatus.PendingTableController;
import admin.adminLoanTables.riskStatus.RiskTableController;
import servletDTO.admin.AdminLoanObj;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminLoanTablesController {
    @FXML AnchorPane NewTable;
    @FXML NewTableController NewTableController;
    @FXML AnchorPane PendingTable;
    @FXML PendingTableController PendingTableController;
    @FXML AnchorPane ActiveTable;
    @FXML ActiveTableController ActiveTableController;
    @FXML AnchorPane RiskTable;
    @FXML RiskTableController RiskTableController;
    @FXML AnchorPane FinishedTable;
    @FXML FinishedTableController FinishedTableController;


    private Timer timer;
    private TimerTask listRefresher;
    public final static int REFRESH_RATE = 2000;
    private final BooleanProperty autoUpdate = new SimpleBooleanProperty(true);

    @FXML public void initialize() {

        if(NewTableController!=null ){
            NewTableController.setMainController(this);
        }

        if(ActiveTableController!=null){
            ActiveTableController.setMainController(this);
        }
        if(PendingTableController != null){
            PendingTableController.setMainController(this);
        }
        if(RiskTableController != null) {
            RiskTableController.setMainController(this);
        }


    }

        public void loadAdminTablesData(List<AdminLoanObj> adminLoanObj){
        NewTableController.initializeTable(adminLoanObj);
        PendingTableController.initializeTable(adminLoanObj);
        ActiveTableController.initializeTable(adminLoanObj);
        RiskTableController.initializeTable(adminLoanObj);
        FinishedTableController.initializeTable(adminLoanObj);
    }


    public void addButtonToTable(TableView<Loan> table) {
        TableColumn<Loan, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<Loan, Void>, TableCell<Loan, Void>> cellFactory =
                new Callback<TableColumn<Loan, Void>, TableCell<Loan, Void>>()
                {
                    @Override
                    public TableCell<Loan, Void> call(final TableColumn<Loan, Void> param) {

                        final TableCell<Loan, Void> cell = new TableCell<Loan, Void>()
                        {

                            private final Button btn = new Button("Loan info");

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

    }



    public void startLoanListRefresher() {
        listRefresher = new TablesRefresher(
                this::loadAdminTablesData,
                autoUpdate
        );
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }


}
