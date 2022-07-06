package admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AdminAppMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent load = FXMLLoader.load(getClass().getResource("AdminMain.fxml"));

        primaryStage.setTitle("ABS System");

        Scene scene = new Scene(load, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.show();

    }

    public static void main(String[] args) {
        launch(AdminAppMain.class);
    }}

