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
            System.out.println(customerInformationBodyController);
            customerInformationBodyController.setMainController(this);
            System.out.println(customerInformationBodyController);
            customerScrambleBodyController.setMainController(this);
            customerPaymentBodyController.setMainController(this);
            buyLoanController.setMainController(this);
        }
/*        paymentTabPane.getTabPane().getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        customerPaymentBodyController.loadLoanTableData();
                    }
                }
        );
        buyLoanTabPane.getTabPane().getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        buyLoanController.loadTableData();
                    }
                }
        );
        informationTabPane.getTabPane().getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        customerInformationBodyController.initializeClientTable();
                        customerInformationBodyController.loadTransactionsTable();
                    }
                }
        );
        customerPaymentBodyController.bindProperties(loadTextAfterYazChange);
        resetFields();*/
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

    public Tab getInformationTabPane() {
        return informationTabPane;
    }

    public Tab getPaymentTabPane() {
        return paymentTabPane;
    }

    public boolean isRunningServiceProperty() {
        return runningServiceProperty.get();
    }

    public SimpleBooleanProperty runningServicePropertyProperty() {
        return runningServiceProperty;
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
