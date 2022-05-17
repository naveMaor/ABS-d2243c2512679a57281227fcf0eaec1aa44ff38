package subcomponents.app;

import MainWindow.mainWindowMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SubsCustomerMainWindowMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello There in FXML");

        //subcomponents/body/Customer/Information/CustomerInformationBody.fxml
        Parent load = FXMLLoader.load(getClass().getResource("subsCustomerMainWindow.fxml"));
        Scene scene = new Scene(load, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(SubsCustomerMainWindowMain.class);
    }}