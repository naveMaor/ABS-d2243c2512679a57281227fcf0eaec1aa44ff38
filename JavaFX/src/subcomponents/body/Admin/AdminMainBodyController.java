package subcomponents.body.Admin;

import MainWindow.mainWindowController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import time.Timeline;
import utills.Engine;

import java.io.IOException;

public class AdminMainBodyController {
    Engine engine = Engine.getInstance();

    private mainWindowController mainController;

    @FXML
    private Button CustomersInformationButtonId;

    @FXML
    private Button IncreaseYazButtonId;

    @FXML
    private Button LoadFileButtonId;

    @FXML
    private Button LoansButtonId;


    @FXML
    void CustomersInformationButtonListener(ActionEvent event) {
    }

    @FXML
    void IncreaseYazButtonListener(ActionEvent event) {
        Timeline.promoteStaticCurrTime();
        engine.filterAndHandleLoansListAfterPromote();
    }

    @FXML
    void LoadFileButtonListener(ActionEvent event) {
        try {
            mainController.openFileButtonAction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void LoansButtonListener(ActionEvent event) {

    }

    public void setMainController(mainWindowController mainController) {
        this.mainController = mainController;
    }


    public void bindProperties(SimpleBooleanProperty isFileSelected, SimpleStringProperty selectedFileProperty){
        CustomersInformationButtonId.disableProperty().bind(isFileSelected.not());
        IncreaseYazButtonId.disableProperty().bind(isFileSelected.not());
        //LoadFileButtonId.disableProperty().bind(isFileSelected.not());
        LoansButtonId.disableProperty().bind(isFileSelected.not());
    }
}