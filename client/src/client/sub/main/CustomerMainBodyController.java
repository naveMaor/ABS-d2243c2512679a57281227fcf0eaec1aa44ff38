package client.sub.main;

//import MainWindow.mainWindowController;

import client.main.ClientMainController;
import client.sub.ClientRefresher;
import client.sub.Information.CustomerInformationBodyCont;
import client.sub.Payment.CustomerPaymentBodyController;
import client.sub.buyLoan.BuyLoanController;
import client.sub.scramble.CustomerScrambleBodyController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import servletDTO.ClientDTOforServlet;

import java.util.Timer;
import java.util.TimerTask;


public class CustomerMainBodyController {


    public final static int REFRESH_RATE = 4000;
    private final BooleanProperty isRewind = new SimpleBooleanProperty(false);
    private ClientMainController mainController;
    @FXML
    private AnchorPane customerInformationBody;
    @FXML
    private CustomerInformationBodyCont customerInformationBodyController;
    @FXML
    private AnchorPane customerScrambleBody;
    @FXML
    private CustomerScrambleBodyController customerScrambleBodyController;
    @FXML
    private AnchorPane customerPaymentBody;
    @FXML
    private CustomerPaymentBodyController customerPaymentBodyController;
    @FXML
    private AnchorPane buyLoan;
    @FXML
    private BuyLoanController buyLoanController;
    @FXML
    private Tab informationTabPane;
    @FXML
    private Tab paymentTabPane;
    @FXML
    private Tab buyLoanTabPane;
    private Timer timer;
    private TimerTask listRefresher;
    private SimpleStringProperty customerName = new SimpleStringProperty();
    private SimpleBooleanProperty loadTextAfterYazChange = new SimpleBooleanProperty();
    private SimpleBooleanProperty runningServiceProperty = new SimpleBooleanProperty();

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public boolean isLoadTextAfterYazChange() {
        return loadTextAfterYazChange.get();
    }

    public SimpleBooleanProperty loadTextAfterYazChangeProperty() {
        return loadTextAfterYazChange;
    }

    @FXML
    public void initialize() {
        if (customerInformationBodyController != null && customerScrambleBodyController != null && customerPaymentBodyController != null) {
            customerInformationBodyController.setMainController(this);
            customerScrambleBodyController.setMainController(this);
            customerPaymentBodyController.setMainController(this);
            buyLoanController.setMainController(this);
        }
        customerInformationBodyController.bindDisable(isRewind);
        customerScrambleBodyController.bindDisable(isRewind);
        customerPaymentBodyController.bindDisable(isRewind);
        buyLoanController.bindDisable(isRewind);


    }

    public void bindProperties(SimpleStringProperty customerName, SimpleBooleanProperty yazChanged) {
        this.customerName.bind(Bindings.concat(customerName));
        loadTextAfterYazChange.bindBidirectional(yazChanged);
    }

    public void setMainController(ClientMainController mainController) {
        this.mainController = mainController;
    }


    public BooleanProperty isRewindProperty() {
        return isRewind;
    }


    public ClientDTOforServlet getCurrClient() {
        return mainController.getCurrClient();
    }


    public void startLoanListRefresher() {

        listRefresher = new ClientRefresher(
                customerInformationBodyController::loadBorrowerLoanTable,
                customerInformationBodyController::loadLenderLoanTable,
                customerPaymentBodyController::loadPaymentData,
                mainController::setCurrClient,
                isRewind,
                customerScrambleBodyController::setAllCategories,
                buyLoanController::loadTableData,
                customerInformationBodyController::loadClientTransactions

        );
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }


}
