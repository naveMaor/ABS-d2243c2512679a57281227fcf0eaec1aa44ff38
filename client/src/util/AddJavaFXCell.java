package util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.Callback;
import servletDTO.LoanInformationObj;
import servletDTO.Payment.LoanPaymentObj;

import java.util.function.Consumer;

public class AddJavaFXCell {

    private BooleanProperty booleanProperty =new SimpleBooleanProperty();

    public static void addCheckBoxCellPayment(TableView<?> LoansTable){
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


    public static <T> void addButtonToTable(TableView<T> lenderTable, Consumer<T> SellLoan, String action) {
        TableColumn<T, Void> colBtn = new TableColumn(action);

        Callback<TableColumn<T, Void>, TableCell<T, Void>> cellFactory = new Callback<TableColumn<T, Void>, TableCell<T, Void>>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                final TableCell<T, Void> cell = new TableCell<T, Void>() {

                    private Button btn = new Button(action);
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            T data = getTableView().getItems().get(getIndex());
                                SellLoan.accept(data);
                                btn.setDisable(true);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        lenderTable.getColumns().add(colBtn);
    }



}
