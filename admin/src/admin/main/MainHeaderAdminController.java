package admin.main;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class MainHeaderAdminController {


    @FXML
    private Label NameCurrentYaz;

    @FXML
    private Label NameFilePath;

    @FXML
    private ComboBox<String> ViewByComboBox;

    private SimpleStringProperty choosenCustomerName = new SimpleStringProperty();

    @FXML
    void ViewByComboBoxListener(ActionEvent event) {
        String selectedBomboBox = ViewByComboBox.getValue();
        choosenCustomerName.setValue(selectedBomboBox);
        }





    public void initializeComboBox() {

    }

    public void bindProperties(SimpleBooleanProperty isFileSelected, SimpleStringProperty selectedFileProperty, SimpleIntegerProperty currentYazProperty, SimpleStringProperty customerName) {
        NameCurrentYaz.disableProperty().bind(isFileSelected.not());
        ViewByComboBox.disableProperty().bind(isFileSelected.not());
        //promoteYazButtonId.disableProperty().bind(isFileSelected.not());
        NameFilePath.textProperty().bind(selectedFileProperty);
        NameCurrentYaz.textProperty().bind(Bindings.concat("Current Yaz: ", currentYazProperty));
        //todo add isFileSelected bolean!!
        customerName.bind(Bindings.concat(choosenCustomerName));

    }


}

