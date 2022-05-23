package subcomponents.body.Admin.adminLoanTables.newStatus;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import loan.Loan;
import loan.enums.eLoanStatus;
import subcomponents.body.Admin.adminLoanTables.adminLoanTablesMain.adminLoanTablesController;
import utills.Engine;

public class NewTableController {

    private Engine engine =Engine.getInstance();

    private adminLoanTablesController mainTablesController;

    public void setMainController(adminLoanTablesController mainTablesController){
        this.mainTablesController = mainTablesController;
    }

    @FXML
    private TableColumn<Loan, Double> ColumnAmount;

    @FXML
    private TableColumn<Loan, String> ColumnCategory;

    @FXML
    private TableColumn<Loan, String> ColumnId;

    @FXML
    private TableColumn<Loan, Double> ColumnInterest;

    @FXML
    private TableColumn<Loan, String> ColumnName;

    @FXML
    private TableColumn<Loan, Integer> ColumnPayEvery;

    @FXML
    private TableColumn<Loan, eLoanStatus> ColumnStatus;

    @FXML
    private TableColumn<Loan, Integer> ColumnTotalYaz;

    //TableColumn<Loan, Void> colBtn = new TableColumn("Button Column");


    @FXML
    private TableView<Loan> NewTable;

    ObservableList<Loan> loanObservableList;

    private void addButtonToTable() {
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

        NewTable.getColumns().add(colBtn);

    }


    public void initialize(){
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<Loan, Double>("totalLoanCostInterestPlusOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<Loan, Double>("originalInterest"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<Loan, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<Loan, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<Loan, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<Loan, eLoanStatus>("status"));
    }

    public void initializeTable() {
        initialize();
        loanObservableList = engine.getDatabase().o_getAllLoansByStatus(eLoanStatus.NEW);
        NewTable.setItems(loanObservableList);

        mainTablesController.addButtonToTable(NewTable);
    }


}

