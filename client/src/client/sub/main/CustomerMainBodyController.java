package client.sub.main;

//import MainWindow.mainWindowController;
import Money.operations.Transaction;
import client.main.ClientMainController;
import client.sub.Information.CustomerInformationBodyCont;
import client.sub.Payment.CustomerPaymentBodyController;
import client.sub.scramble.CustomerScrambleBodyController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import engine.Engine;


public class CustomerMainBodyController {
//    Engine engine = Engine.getInstance();
    private Engine engine = new Engine();

    //private mainWindowController mainController;
    private ClientMainController mainController;
    @FXML private ScrollPane customerInformationBody;
    @FXML private CustomerInformationBodyCont customerInformationBodyController;

    @FXML private AnchorPane customerScrambleBody;
    @FXML private CustomerScrambleBodyController customerScrambleBodyController;

    @FXML private AnchorPane customerPaymentBody;
    @FXML private CustomerPaymentBodyController customerPaymentBodyController;

    @FXML private Tab informationTabPane;
    @FXML private Tab paymentTabPane;

    public Engine getEngine()
    {
        return mainController.getEngine();
    }

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
        }
        paymentTabPane.getTabPane().getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        customerPaymentBodyController.loadLoanTableData();
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
        customerScrambleBodyController.initialize();
        resetFields();
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

    public ObservableList<Transaction> getAllClientTransactions(){

        return engine.getClientTransactionsList(customerName.get());
    }


    public void resetFields(){
        customerScrambleBodyController.resetFileds();
        customerScrambleBodyController.resetRelevantLoansTable();
    }

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

    public void loadData(){
        customerInformationBodyController.initializeClientTable();
        customerInformationBodyController.loadTransactionsTable();
        customerPaymentBodyController.loadLoanTableData();
    }

}
