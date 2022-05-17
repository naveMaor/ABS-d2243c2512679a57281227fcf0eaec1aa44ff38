package subcomponents.body.Admin.adminLoanTables.pendingStatus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import loan.enums.eLoanStatus;
import loanDTO.LoanObj;
import time.Timeline;

public class PendingTableController {

    @FXML
    private TableView<LoanObj> PendingTable;

    @FXML
    private TableColumn<LoanObj, String> ColumnId;

    @FXML
    private TableColumn<LoanObj, String> ColumnName;

    @FXML
    private TableColumn<LoanObj, eLoanStatus> ColumnStatus;

    @FXML
    private TableColumn<LoanObj, String> ColumnCategory;

    @FXML
    private TableColumn<LoanObj, Double> ColumnAmount;

    @FXML
    private TableColumn<LoanObj, Timeline> ColumnTotalYaz;

    @FXML
    private TableColumn<LoanObj, Timeline> ColumnPayEvery;

    @FXML
    private TableColumn<LoanObj, Double> ColumnInterest;

    @FXML
    private TableColumn<LoanObj, Button> LendersColumn;

    @FXML
    private TableColumn<LoanObj, Double> RaisedColmun;

    @FXML
    private TableColumn<LoanObj, Double> LeftToMakeActive;



}
