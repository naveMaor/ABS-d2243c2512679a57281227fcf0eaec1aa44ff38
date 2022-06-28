package admin.adminLoanTables.activeStatus;

import admin.adminLoanTables.activeStatus.innerTable.ActiveInnerTableController;
import admin.adminLoanTables.adminLoanTablesMain.adminLoanTablesController;
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
import engine.Engine;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import servletDTO.admin.AdminLoanObj;

import java.io.IOException;
import java.util.List;


public class ActiveTableController {

    Engine engine =Engine.getInstance();

    private adminLoanTablesController mainTablesController;

    public void setMainController(adminLoanTablesController mainTablesController){
        this.mainTablesController = mainTablesController;
    }
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
    private TableColumn<AdminLoanObj, Button> LendersColumn;


    @FXML
    private TableColumn<AdminLoanObj, Integer> ActiveStatusYaz;

    @FXML
    private TableColumn<AdminLoanObj, Integer> NextPaymentColumn;

    ObservableList<AdminLoanObj> loanObservableList;

    @FXML
    private TableView<AdminLoanObj> ActiveTable;



/*
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
*/

    public void initialize(){
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
    }


    public void initializeTable(List<AdminLoanObj> adminLoanObj) {
        loanObservableList.clear();
        ActiveTable.getItems().clear();
        for(AdminLoanObj loanObj:adminLoanObj){
            if(loanObj.getStatus()==eLoanStatus.PENDING){
                loanObservableList.add(loanObj);
            }
        }
        ActiveTable.setItems(loanObservableList);
}
/*
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
*/

}
