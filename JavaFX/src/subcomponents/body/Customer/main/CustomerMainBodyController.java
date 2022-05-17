package subcomponents.body.Customer.main;

import MainWindow.mainWindowController;
import javafx.fxml.FXML;
import subcomponents.body.Customer.Information.CustomerInformationBodyCont;
import subcomponents.body.Customer.Payment.CustomerIpaymentBodyController;

import java.awt.*;

public class CustomerMainBodyController {

    private mainWindowController mainController;


/*    @FXML
    private ScrollPane customerInformationBody;
    @FXML
    private CustomerInformationBodyCont customerInformationBodyCont;
    @FXML
    private ScrollPane customerPaymentBody;
    @FXML
    private CustomerIpaymentBodyController customerIpaymentBodyController;*/


    public void setMainController(mainWindowController mainController) {
        this.mainController = mainController;
    }

}
