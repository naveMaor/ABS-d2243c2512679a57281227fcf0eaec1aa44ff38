package subcomponents.body.Customer.Information;

import MainWindow.mainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import subcomponents.app.subsCustomerMainWindowController;

public class CustomerInformationBodyController {

    private subsCustomerMainWindowController customerMainWindowController;


    @FXML
    void ChargeTransactionButtonListener(ActionEvent event) {

    }

    @FXML
    void InformationLoanerLoansTableButtonListener(ActionEvent event) {

    }

    @FXML
    void LenderLoansTableButtonListener(ActionEvent event) {

    }

    @FXML
    void WithdrawTransactionButtonListener(ActionEvent event) {

    }

    public void setMainController(subsCustomerMainWindowController customerMainWindowController) {
        this.customerMainWindowController = customerMainWindowController;
    }
}
