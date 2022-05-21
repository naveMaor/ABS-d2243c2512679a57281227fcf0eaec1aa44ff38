package subcomponents.body.Customer.Information;

import MainWindow.mainWindowController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import subcomponents.body.Customer.Information.transactionsTableView.transactionsController;
import subcomponents.body.Customer.main.CustomerMainBodyController;
import utills.Engine;

public class CustomerInformationBodyCont {
    Engine engine= Engine.getInstance();
    private CustomerMainBodyController customerMainBodyController;

    @FXML
    private BorderPane transactions;
    @FXML
    private transactionsController transactionsController;

    public void setMainController(CustomerMainBodyController customerMainBodyController) {
        this.customerMainBodyController = customerMainBodyController;
    }

    @FXML public void initialize() {
        if (transactionsController != null) {
            transactionsController.setMainController(this);
            System.out.println(transactionsController);
        }}

    public void createTransaction(int amount){
        engine.AccountTransaction(amount,customerMainBodyController.customerNameProperty().get());
    }

    public SimpleStringProperty customerNameProperty() {
        return customerMainBodyController.customerNameProperty();
    }
}
