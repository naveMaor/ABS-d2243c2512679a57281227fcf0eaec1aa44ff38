
package MainWindow;

import java.io.File;

import data.File.XmlFile;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import subcomponents.body.Admin.AdminMainBodyController;
import subcomponents.body.Customer.main.CustomerMainBodyController;
import subcomponents.header.MainHeaderController;
import time.Timeline;
import utills.Engine;

public class mainWindowController {
    Engine engine = Engine.getInstance();



    @FXML private BorderPane root;
    @FXML private BorderPane mainHeader;
    @FXML private MainHeaderController mainHeaderController;
    @FXML private ScrollPane adminMainBody;
    @FXML private AdminMainBodyController adminMainBodyController;
    @FXML private ScrollPane customerMainBodyMain;
    @FXML private CustomerMainBodyController customerMainBodyMainController;

    private Stage primaryStage;
    private SimpleBooleanProperty isFileSelected;
    private SimpleBooleanProperty isAdminWindow;
    private SimpleStringProperty selectedFileProperty;
    private SimpleIntegerProperty currentYazProperty;

    public mainWindowController() {
        isAdminWindow = new SimpleBooleanProperty(true);
        isFileSelected = new SimpleBooleanProperty(false);
        selectedFileProperty = new SimpleStringProperty();
        currentYazProperty = new SimpleIntegerProperty(Timeline.getCurrTime());
        currentYazProperty.bind(Timeline.getCurrTimePropery());
    }

    @FXML public void initialize() {
        root.setBottom(null);
        if (mainHeaderController != null && adminMainBodyController != null) {
            mainHeaderController.setMainController(this);
            adminMainBodyController.setMainController(this);
        }
        if(customerMainBodyMainController!=null){
            customerMainBodyMainController.setMainController(this);
        }
        mainHeaderController.initializeComboBox();
        mainHeaderController.bindProperties(isFileSelected,selectedFileProperty,currentYazProperty);
        adminMainBodyController.bindProperties(isFileSelected,selectedFileProperty);
    }

    //todo:do we really need it?!?
    public void setHeaderComponentController(MainHeaderController headerComponentController) {
        this.mainHeaderController = headerComponentController;
        headerComponentController.setMainController(this);
    }
    public void setBodyComponentController(AdminMainBodyController bodyComponentController) {
        this.adminMainBodyController = bodyComponentController;
        bodyComponentController.setMainController(this);
    }



    public void ChangeToCustomerCompenent(){
        root.setCenter(customerMainBodyMain);
    }

    public void ChangeToAdminCompenent(){
        root.setCenter(adminMainBody);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        try {
            XmlFile.createInputObjectFromFile(selectedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        engine.buildDataFromDescriptor();
        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);
    }



}

