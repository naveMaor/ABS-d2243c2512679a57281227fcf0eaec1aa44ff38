package admin;

import client.main.ClientMainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AdminMainMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent load = FXMLLoader.load(getClass().getResource("AdminMain.fxml"));

        primaryStage.setTitle("ABS System");

        Scene scene = new Scene(load, 1200, 800);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stopppppppppppppp");
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.show();

    }

    public static void main(String[] args) {
        launch(AdminMainMain.class);
    }}

