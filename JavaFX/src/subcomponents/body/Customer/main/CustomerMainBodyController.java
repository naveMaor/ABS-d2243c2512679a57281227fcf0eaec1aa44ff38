package subcomponents.body.Customer.main;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import subcomponents.body.Customer.Information.CustomerInformationBodyController;
import subcomponents.body.Customer.Payment.CustomerIpaymentBodyController;

import java.awt.*;

public class CustomerMainBodyController {

    @FXML
    private ScrollPane customerInformationBody;
    @FXML
    private CustomerInformationBodyController customerInformationBodyController;
    @FXML
    private ScrollPane customerPaymentBody;
    @FXML
    private CustomerIpaymentBodyController customerIpaymentBodyController;


}
