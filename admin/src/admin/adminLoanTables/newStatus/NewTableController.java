package admin.adminLoanTables.newStatus;


import admin.adminLoanTables.adminLoanTablesMain.AdminLoanTablesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loan.enums.eLoanStatus;
import servletDTO.admin.AdminLoanObj;

import java.util.List;

public class NewTableController {


    ObservableList<AdminLoanObj> loanObservableList = FXCollections.observableArrayList();
    private AdminLoanTablesController mainTablesController;
    @FXML
    private TableColumn<AdminLoanObj, Double> ColumnAmount;

    @FXML
    private TableColumn<AdminLoanObj, String> ColumnCategory;

    @FXML
    private TableColumn<AdminLoanObj, String> ColumnId;

    @FXML
    private TableColumn<AdminLoanObj, Double> ColumnInterest;

    @FXML
    private TableColumn<AdminLoanObj, String> ColumnName;

    @FXML
    private TableColumn<AdminLoanObj, Integer> ColumnPayEvery;

    @FXML
    private TableColumn<AdminLoanObj, eLoanStatus> ColumnStatus;

    @FXML
    private TableColumn<AdminLoanObj, Integer> ColumnTotalYaz;


    @FXML
    private TableView<AdminLoanObj> NewTable;

    public void setMainController(AdminLoanTablesController mainTablesController) {
        this.mainTablesController = mainTablesController;
    }

    public void initialize() {
        ColumnAmount.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("loanOriginalDepth"));
        ColumnInterest.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Double>("interestPercentagePerTimeUnit"));
        ColumnCategory.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("loanCategory"));
        ColumnId.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("loanID"));
        ColumnName.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, String>("borrowerName"));
        ColumnPayEvery.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("paymentFrequency"));
        ColumnTotalYaz.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, Integer>("originalLoanTimeFrame"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<AdminLoanObj, eLoanStatus>("status"));
    }

    public void initializeTable(List<AdminLoanObj> adminLoanObj) {
        loanObservableList.clear();
        NewTable.getItems().clear();
        initialize();
        for (AdminLoanObj loanObj : adminLoanObj) {
            if (loanObj.getStatus() == eLoanStatus.NEW) {
                loanObservableList.add(loanObj);
            }
        }
        NewTable.setItems(loanObservableList);

    }


}

