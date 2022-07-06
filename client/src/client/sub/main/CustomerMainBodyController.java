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


    private ClientMainController mainController;
    @FXML private ScrollPane customerInformationBody;
    @FXML private CustomerInformationBodyCont customerInformationBodyController;

    @FXML private AnchorPane customerScrambleBody;
    @FXML private CustomerScrambleBodyController customerScrambleBodyController;

    @FXML private AnchorPane customerPaymentBody;
    @FXML private CustomerPaymentBodyController customerPaymentBodyController;

    @FXML private AnchorPane buyLoan;
    @FXML private BuyLoanController buyLoanController;

    @FXML private Tab informationTabPane;
    @FXML private Tab paymentTabPane;
    @FXML private Tab buyLoanTabPane;

    private Timer timer;
    private TimerTask listRefresher;
    public final static int REFRESH_RATE = 2000;
    private final BooleanProperty autoUpdate = new SimpleBooleanProperty(true);


    private SimpleStringProperty customerName = new SimpleStringProperty();
    private SimpleBooleanProperty loadTextAfterYazChange = new SimpleBooleanProperty();

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    private SimpleBooleanProperty runningServiceProperty= new SimpleBooleanProperty();

    public boolean isLoadTextAfterYazChange() {
        return loadTextAfterYazChange.get();
    }

    public SimpleBooleanProperty loadTextAfterYazChangeProperty() {
        return loadTextAfterYazChange;
    }

    @FXML public void initialize() {
        if (customerInformationBodyController != null&& customerScrambleBodyController!=null&& customerPaymentBodyController!=null) {
            customerInformationBodyController.setMainController(this);
            customerScrambleBodyController.setMainController(this);
            customerPaymentBodyController.setMainController(this);
            buyLoanController.setMainController(this);
        }
        customerInformationBodyController.bindDisable(autoUpdate);
        customerScrambleBodyController.bindDisable(autoUpdate);
        customerPaymentBodyController.bindDisable(autoUpdate);
        buyLoanController.bindDisable(autoUpdate);
    }

    public void bindProperties(SimpleStringProperty customerName, SimpleBooleanProperty yazChanged){
        this.customerName.bind(Bindings.concat(customerName));
        loadTextAfterYazChange.bindBidirectional(yazChanged);
        //yazChanged.bind(loadTextAfterYazChange);
    }
/*
    public void setMainController(mainWindowController mainController) {
        this.mainController = mainController;
    }*/

    public void setMainController(ClientMainController mainController) {
        this.mainController = mainController;
    }



/*    public void resetFields(){
        customerScrambleBodyController.resetFields();
        customerScrambleBodyController.resetRelevantLoansTable();
    }*/

    public boolean isAutoUpdate() {
        return autoUpdate.get();
    }

    public BooleanProperty autoUpdateProperty() {
        return autoUpdate;
    }

/*    public void loadData(){
        synchronized (this) {
                customerInformationBodyController.initializeClientTable();
                customerInformationBodyController.loadTransactionsTable();
                customerPaymentBodyController.loadLoanTableData();
                buyLoanController.loadTableData();
        }
    }*/

    public ClientDTOforServlet getCurrClient(){
        return mainController.getCurrClient();
    }


    public void startLoanListRefresher() {
        listRefresher = new ClientRefresher(
                customerInformationBodyController::loadBorrowerLoanTable,
                customerInformationBodyController::loadLenderLoanTable,
                customerInformationBodyController::loadClientTransactions,
                customerPaymentBodyController::loadPaymentData,
                mainController::setCurrClient,
                autoUpdate,
                customerScrambleBodyController::setAllCategories,
                buyLoanController::loadTableData
                );
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }


}
