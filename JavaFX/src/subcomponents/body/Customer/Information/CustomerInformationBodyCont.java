package subcomponents.body.Customer.Information;

import MainWindow.mainWindowController;
import exceptions.BalanceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import subcomponents.body.Customer.Information.transactionsTableView.transactionsController;
import subcomponents.body.Customer.main.CustomerMainBodyController;
import utills.Engine;

public class CustomerInformationBodyCont {
    private Engine engine= Engine.getInstance();
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
        }}

    public void createTransaction(int amount){
        try {
            engine.AccountTransaction(amount,customerMainBodyController.customerNameProperty().get());
        } catch (BalanceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Balance can not be minus!");
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }

    public SimpleStringProperty customerNameProperty() {
        return customerMainBodyController.customerNameProperty();
    }
}
