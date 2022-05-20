package subcomponents.header;

import MainWindow.mainWindowController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class MainHeaderController {

    private mainWindowController mainController;

    @FXML
    private Label NameCurrentYaz;

    @FXML
    private Label NameFilePath;

    @FXML
    private ComboBox<String> ViewByComboBox;


    @FXML
    void ViewByComboBoxListener(ActionEvent event) {
        String selectedBomboBox = ViewByComboBox.getSelectionModel().getSelectedItem().toString();
        if (selectedBomboBox.equals("Admin")){
            mainController.ChangeToAdminCompenent();

        }
        else{
            mainController.ChangeToCustomerCompenent();
        }
    }



    public void setMainController(mainWindowController mainController) {
        this.mainController = mainController;
    }


    public void initializeComboBox() {
        ObservableList <String> clientNameList = mainController.getAllClientNames();
        clientNameList.add("Admin");
        ViewByComboBox.setItems(clientNameList);
    }

    public void bindProperties(SimpleBooleanProperty isFileSelected, SimpleStringProperty selectedFileProperty, SimpleIntegerProperty currentYazProperty){
        NameCurrentYaz.disableProperty().bind(isFileSelected.not());
        ViewByComboBox.disableProperty().bind(isFileSelected.not());
        //promoteYazButtonId.disableProperty().bind(isFileSelected.not());
        NameFilePath.textProperty().bind(selectedFileProperty);
        NameCurrentYaz.textProperty().bind(Bindings.concat("Current Yaz: ", currentYazProperty));
        //todo add isFileSelected bolean!!
    }




}

