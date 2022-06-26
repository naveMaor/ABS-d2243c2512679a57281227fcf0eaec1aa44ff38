package client.sub.loanforsell;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;



public class LoanSellTabController {

    //Sub components
    @FXML private ScrollPane buyLoansTable;
//    @FXML private LoanBuyTableController buyLoansTableController;

    //JavaFX components
    @FXML private ScrollPane buySellLoanSP;
    @FXML private AnchorPane buySellLoanAP;
    @FXML private TabPane buySellLoanTP;
    @FXML private Tab sellLoanTab;
//    @FXML private CheckListView<String> sellLoanCLV;

    @FXML private Button confirmSellButton;
    @FXML private Label sellErrorMessage;
    @FXML private Tab buyLoanTab;
    @FXML private Button confirmBuyButton;
    @FXML private Label errorBuyMessage;

    //Regular Fields
//    private HeaderCustomerController headerCustomerController;



    @FXML
    private void initialize() {
        buySellLoanTP.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            sellErrorMessage.setVisible(false);
//            if(sellLoanCLV.getCheckModel().getCheckedItems().size()>0)
//                sellLoanCLV.getCheckModel().getCheckedItems().clear();
            errorBuyMessage.setText("");
        });

    }


    public void confirmSellOnAction(ActionEvent actionEvent) {
    }

    public void confirmBuyButtonOnAction(ActionEvent actionEvent) {
    }
}


