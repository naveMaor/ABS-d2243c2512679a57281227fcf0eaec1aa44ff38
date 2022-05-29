package subcomponents.body.Admin;

import MainWindow.mainWindowController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import subcomponents.body.Admin.adminClientTable.adminClientTableController;
import subcomponents.body.Admin.adminLoanTables.adminLoanTablesMain.adminLoanTablesController;
import utills.Engine;

public class AdminMainBodyController {
    Engine engine = Engine.getInstance();

    private mainWindowController mainController;

    @FXML
    private Button CustomersInformationButtonId;

    @FXML
    private Button IncreaseYazButtonId;

    @FXML
    private Button LoadFileButtonId;



    @FXML private AnchorPane adminLoanTables;
    @FXML private adminLoanTablesController adminLoanTablesController;

    @FXML private AnchorPane adminClientTable;
    @FXML private adminClientTableController adminClientTableController;

    @FXML
    void CustomersInformationButtonListener(ActionEvent event) {
    }

    SimpleBooleanProperty increaseYaz = new SimpleBooleanProperty(false);

    @FXML
    void IncreaseYazButtonListener(ActionEvent event) {
        engine.increaseYaz();
        initializeAdminTables();
        increaseYaz.setValue(true);
    }

    @FXML
    void LoadFileButtonListener(ActionEvent event) {
            mainController.openFileButtonAction();
    }

    @FXML
    void LoansButtonListener(ActionEvent event) {

    }

    public void setMainController(mainWindowController mainController) {
        this.mainController = mainController;
    }


    public void bindProperties(SimpleBooleanProperty isFileSelected, SimpleStringProperty selectedFileProperty, SimpleBooleanProperty isYazChanged){
        //CustomersInformationButtonId.disableProperty().bind(isFileSelected.not());
        IncreaseYazButtonId.disableProperty().bind(isFileSelected.not());
        //LoadFileButtonId.disableProperty().bind(isFileSelected.not());
        isYazChanged.bindBidirectional(increaseYaz);
    }

    public void initializeAdminTables(){
        adminLoanTablesController.initializeLoansTable();
        adminClientTableController.initializeClientTable();
    }
}