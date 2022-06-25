package util;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import servletDTO.LoanInformationObj;
import servletDTO.Payment.LoanPaymentObj;

public class AddCheckBoxCell {

    public static void addCheckBoxCell(TableView<?> LoansTable){
        TableColumn select = new TableColumn("select");
        select.setMinWidth(50);
        select.setPrefWidth(50);
        select.setMaxWidth(50);
        select.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LoanPaymentObj, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<LoanPaymentObj, CheckBox> arg0) {
                LoanPaymentObj user = arg0.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(user.isSelect());



                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {

                        user.setSelect(new_val);

                    }
                });
    ////asdadasdasd
                return new SimpleObjectProperty<CheckBox>(checkBox);

            }

        });
        LoansTable.getColumns().add(0, select);
    }

    public static void addCheckBoxCellScramble(TableView<?> LoansTable){
        TableColumn select = new TableColumn("select");
        select.setMinWidth(50);
        select.setPrefWidth(50);
        select.setMaxWidth(50);
        select.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LoanInformationObj, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<LoanInformationObj, CheckBox> arg0) {
                LoanInformationObj user = arg0.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(user.getSelect());



                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {

                        user.setSelect(new_val);

                    }
                });

                return new SimpleObjectProperty<CheckBox>(checkBox);

            }

        });
        LoansTable.getColumns().add(0, select);
    }
}
