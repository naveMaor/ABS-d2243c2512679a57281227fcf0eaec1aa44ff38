package subcomponents.body.Customer.main;

import MainWindow.mainWindowController;
import Money.operations.Transaction;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import subcomponents.body.Customer.Information.CustomerInformationBodyCont;
import subcomponents.body.Customer.Payment.CustomerIpaymentBodyController;
import utills.Engine;
import javafx.scene.control.ScrollPane;


public class CustomerMainBodyController {
    Engine engine = Engine.getInstance();
    private mainWindowController mainController;
    @FXML private ScrollPane customerInformationBody;
    @FXML private CustomerInformationBodyCont customerInformationBodyController;


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
        if (customerInformationBodyController != null) {
            System.out.println(customerInformationBodyController);
            customerInformationBodyController.setMainController(this);
            System.out.println(customerInformationBodyController);

        }}

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
