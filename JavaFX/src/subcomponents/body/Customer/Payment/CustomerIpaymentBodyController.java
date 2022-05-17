package subcomponents.body.Customer.Payment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import subcomponents.app.subsCustomerMainWindowController;

public class CustomerIpaymentBodyController {

    private subsCustomerMainWindowController customerMainWindowController;


    @FXML
    void PaymentControlButtonListener(ActionEvent event) {

    }

    @FXML
    void PaymentLoanerLoansTableButtonListener(ActionEvent event) {

    }

    public void setCustomerMainWindowController(subsCustomerMainWindowController customerMainWindowController) {
        this.customerMainWindowController = customerMainWindowController;
    }

}
