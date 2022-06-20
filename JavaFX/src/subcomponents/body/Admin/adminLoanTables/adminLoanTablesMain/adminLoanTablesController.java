package subcomponents.body.Admin.adminLoanTables.adminLoanTablesMain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import loan.Loan;
import subcomponents.body.Admin.adminLoanTables.activeStatus.ActiveTableController;
import subcomponents.body.Admin.adminLoanTables.finishedStatus.FinishedTableController;
import subcomponents.body.Admin.adminLoanTables.newStatus.NewTableController;
import subcomponents.body.Admin.adminLoanTables.pendingStatus.PendingTableController;
import subcomponents.body.Admin.adminLoanTables.riskStatus.RiskTableController;

public class adminLoanTablesController {
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

        public void initializeLoansTable(){
        NewTableController.initializeTable();
        PendingTableController.initializeTable();
        ActiveTableController.initializeTable();
        RiskTableController.initializeTable();
        FinishedTableController.initialize();
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

}
