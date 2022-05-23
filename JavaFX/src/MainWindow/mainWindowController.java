
package MainWindow;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import MainWindow.popUpMessage.MessageController;
import data.File.XmlFile;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import subcomponents.body.Admin.AdminMainBodyController;
import subcomponents.body.Customer.main.CustomerMainBodyController;
import subcomponents.header.MainHeaderController;
import time.Timeline;
import utills.Engine;
import javafx.scene.image.Image ;

import javax.xml.bind.JAXBException;

public class mainWindowController {
    Engine engine = Engine.getInstance();
    public enum MessageType{Error,Successfully,Information};


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
    private SimpleStringProperty customerName = new SimpleStringProperty();

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
        //mainHeaderController.initializeComboBox();
        mainHeaderController.bindProperties(isFileSelected,selectedFileProperty,currentYazProperty,customerName);
        adminMainBodyController.bindProperties(isFileSelected,selectedFileProperty);
        customerMainBodyMainController.bindProperties(customerName);
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
        customerMainBodyMainController.resetFields();
        //System.out.println(customerName.get());
    }

    public void ChangeToAdminCompenent(){
        adminMainBodyController.initializeAdminTables();
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
        //Scene scene = new Scene(load, 800, 600);
        Stage errorWindow;
        if (selectedFile == null) {
            return;
        }
        try {
            XmlFile.createInputObjectFromFile(selectedFile);
            engine.CheckInvalidFile(XmlFile.getInputObject());
        } catch (FileNotFoundException e) {
            errorWindow= MessageStage(MessageType.Error,e.getMessage());
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.show();
            e.printStackTrace();

        } catch (JAXBException e) {
            errorWindow= MessageStage(MessageType.Error,"file is corrupted");
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.show();
            e.printStackTrace();
        } catch (Exception e) {
            errorWindow= MessageStage(MessageType.Error,e.getMessage());
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.show();
            e.printStackTrace();
        }

        engine.buildDataFromDescriptor();
        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        mainHeaderController.initializeComboBox();
        adminMainBodyController.initializeAdminTables();
        isFileSelected.set(true);
    }

    public Stage MessageStage(MessageType type , String message) {
        //create stage
        Stage stage = new Stage();
        stage.setTitle("Message");
        //load fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("popUpMessage/message-fxml.fxml"));
        ScrollPane MessagePane = null;
        try {
            MessagePane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get the controller
        MessageController messageController = fxmlLoader.getController();
        messageController.setMessageText(message);
        switch (type) {
            case Error:
                Image icon = new Image(getClass().getResourceAsStream("popUpMessage/resources/error-icon-png-23.jpg")) {
                };
                messageController.setMessageImage(icon);
                break;
            case Successfully:
                icon = new Image(getClass().getResourceAsStream("popUpMessage/resources/successfully-icon-png-24.jpg"));
                messageController.setMessageImage(icon);
                break;
        }
        Scene scene = new Scene(MessagePane);
        stage.setScene(scene);
        return stage;
    }

    public ObservableList<String> getAllClientNames(){
        return engine.getDatabase().o_getAllClientNames();
    }


    public SimpleBooleanProperty getRunningServiceProperty(){
        return customerMainBodyMainController.runningServicePropertyProperty();
    }

}

