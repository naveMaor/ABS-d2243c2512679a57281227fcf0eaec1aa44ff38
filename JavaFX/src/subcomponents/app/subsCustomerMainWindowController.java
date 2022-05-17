package subcomponents.app;

import MainWindow.mainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import subcomponents.body.Customer.Information.CustomerInformationBodyCont;

public class subsCustomerMainWindowController {

    private mainWindowController mainController;


    @FXML private BorderPane subsCustomerMainWindow;

    @FXML private AnchorPane CustomerHeader;
    @FXML private subcomponents.header.Customer.CustomerHeaderController CustomerHeaderController;

    @FXML private ScrollPane CustomerInformationBody;
    @FXML private CustomerInformationBodyCont CustomerInformationBodyController;

    @FXML private ScrollPane CustomerPaymentBody;
    @FXML private subcomponents.body.Customer.Payment.CustomerIpaymentBodyController CustomerPaymentBodyController;

    @FXML public void initialize() {

        subsCustomerMainWindow.setBottom(null);
        //subsCustomerMainWindow.setCenter(null);
        //root.setBottom(null);
        if (CustomerHeaderController != null && CustomerInformationBodyController != null) {
            CustomerHeaderController.setMainController(this);
            CustomerInformationBodyController.setMainController(this);
        }
        if(CustomerPaymentBodyController!=null){
            CustomerPaymentBodyController.setCustomerMainWindowController(this);
        }
    }

    public void setMainController(mainWindowController mainController) {
        this.mainController = mainController;
    }


    public void ChangeToInformationCompenent(){
        subsCustomerMainWindow.setCenter(CustomerInformationBody);
    }
    public void ChangeToPaymentCompenent(){
        subsCustomerMainWindow.setCenter(CustomerPaymentBody);
    }


}
