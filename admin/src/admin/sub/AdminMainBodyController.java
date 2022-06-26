package admin.sub;



import engine.Engine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import admin.sub.adminClientTable.adminClientTableController;
import admin.sub.adminLoanTables.adminLoanTablesMain.adminLoanTablesController;


public class AdminMainBodyController {
    Engine engine = new Engine();


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
    void LoansButtonListener(ActionEvent event) {

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