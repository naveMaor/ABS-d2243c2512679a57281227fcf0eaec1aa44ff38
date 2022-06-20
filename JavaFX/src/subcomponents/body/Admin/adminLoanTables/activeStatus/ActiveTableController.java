package subcomponents.body.Admin.adminLoanTables.activeStatus;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import loan.Loan;
import loan.enums.eLoanStatus;
import subcomponents.body.Admin.adminLoanTables.activeStatus.innerTable.ActiveInnerTableController;
import subcomponents.body.Admin.adminLoanTables.adminLoanTablesMain.adminLoanTablesController;
import utills.Engine;
import javafx.util.Callback;
import javafx.event.ActionEvent;

import java.io.IOException;


public class ActiveTableController {

    Engine engine =Engine.getInstance();

    private adminLoanTablesController mainTablesController;

    public void setMainController(adminLoanTablesController mainTablesController){
        this.mainTablesController = mainTablesController;
    }
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

    ObservableList<Loan> loanObservableList;

    @FXML
    private TableView<Loan> ActiveTable;



    public void addButtonToTable(TableView<Loan> table) {
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

    }


    public void initializeTable() {
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
        LendersColumn.setCellValueFactory(new PropertyValueFactory<Loan, Button>("infoButton"));

        loanObservableList = engine.getDatabase().o_getAllLoansByStatus(eLoanStatus.ACTIVE);
        for (Loan loan:loanObservableList){
            loan.getInfoButton().setOnAction(event -> ActiveActionHandle(loan));
        }
        ActiveTable.setItems(loanObservableList);

        //mainTablesController.addButtonToTable(ActiveTable);

}
public void ActiveActionHandle(Loan loan){
    //create stage
    Stage stage = new Stage();
    stage.setTitle("lenders info");
    //load fxml
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("innerTable/ActiveInnerTable.fxml"));
    AnchorPane activeInnerTable = null;
    try {
        activeInnerTable = fxmlLoader.load();
    } catch (IOException e) {
        e.printStackTrace();
    }
    //get the controller
    ActiveInnerTableController innerTableController = fxmlLoader.getController();
    innerTableController.initializeTable(loan.getLendersList(),loan.getPaymentsList());
    Scene scene = new Scene(activeInnerTable);
    stage.setScene(scene);
    stage.show();
}

}
