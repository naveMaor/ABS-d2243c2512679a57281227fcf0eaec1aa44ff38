package util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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


    public static void addSellButtonToTable(TableView<LoanInformationObj> lenderTable, Consumer<LoanInformationObj> SellLoan) {
        TableColumn<LoanInformationObj, Void> colBtn = new TableColumn("Sell");
        Button Sellbtn;
        Callback<TableColumn<LoanInformationObj, Void>, TableCell<LoanInformationObj, Void>> cellFactory = new Callback<TableColumn<LoanInformationObj, Void>, TableCell<LoanInformationObj, Void>>() {
            @Override
            public TableCell<LoanInformationObj, Void> call(final TableColumn<LoanInformationObj, Void> param) {
                final TableCell<LoanInformationObj, Void> cell = new TableCell<LoanInformationObj, Void>() {

                    private Button btn = new Button("Action");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            LoanInformationObj data = getTableView().getItems().get(getIndex());
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
