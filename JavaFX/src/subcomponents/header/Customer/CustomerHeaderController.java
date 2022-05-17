package subcomponents.header.Customer;

import MainWindow.mainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import subcomponents.app.subsCustomerMainWindowController;

public class CustomerHeaderController {

    private subsCustomerMainWindowController CustomerMainWindowController;

    @FXML
    private AnchorPane CustomerHeader;

    @FXML
    void InformationButtonActionListener(ActionEvent event) {
        CustomerMainWindowController.ChangeToInformationCompenent();
    }

    @FXML
    void PatmentButtonActionListener(ActionEvent event) {
        CustomerMainWindowController.ChangeToPaymentCompenent();
    }

    @FXML
    void ScrambleButtonActionListener(ActionEvent event) {

    }

    public void setMainController(subsCustomerMainWindowController CustomerMainWindowController) {
        this.CustomerMainWindowController = CustomerMainWindowController;
    }

}
