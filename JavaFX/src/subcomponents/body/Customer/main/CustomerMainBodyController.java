package subcomponents.body.Customer.main;

import MainWindow.mainWindowController;
import Money.operations.Transaction;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import subcomponents.body.Customer.Information.CustomerInformationBodyCont;
import subcomponents.body.Customer.Payment.CustomerPaymentBodyController;
import subcomponents.body.Customer.scramble.CustomerScrambleBodyController;
import utills.Engine;
import javafx.scene.control.ScrollPane;


public class CustomerMainBodyController {
    Engine engine = Engine.getInstance();
    private mainWindowController mainController;
    @FXML private ScrollPane customerInformationBody;
    @FXML private CustomerInformationBodyCont customerInformationBodyController;

    @FXML private AnchorPane customerScrambleBody;
    @FXML private CustomerScrambleBodyController customerScrambleBodyController;

    @FXML private AnchorPane customerPaymentBody;
    @FXML private CustomerPaymentBodyController customerPaymentBodyController;


/*    @FXML
    private ScrollPane customerInformationBody;
    @FXML
    private CustomerInformationBodyCont customerInformationBodyCont;
    @FXML
    private ScrollPane customerPaymentBody;
    @FXML
    private CustomerIpaymentBodyController customerIpaymentBodyController;*/

    private SimpleStringProperty customerName = new SimpleStringProperty();

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    @FXML public void initialize() {
        if (customerInformationBodyController != null&& customerScrambleBodyController!=null) {
            System.out.println(customerInformationBodyController);
            customerInformationBodyController.setMainController(this);
            System.out.println(customerInformationBodyController);
            customerScrambleBodyController.setMainController(this);
        }
        customerScrambleBodyController.initialize();
    }

    public void bindProperties(SimpleStringProperty customerName){
        this.customerName.bind(Bindings.concat(customerName));
    }

    public void setMainController(mainWindowController mainController) {
        this.mainController = mainController;
    }

    public ObservableList<Transaction> getAllClientTransactions(){

        return engine.getClientTransactionsList(customerName.get());
    }

}
