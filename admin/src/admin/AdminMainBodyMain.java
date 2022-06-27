package admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminMainBodyMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent load = FXMLLoader.load(getClass().getResource("AdminMainBody.fxml"));

        primaryStage.setTitle("ABS System");
        Scene scene = new Scene(load, 1200, 800);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(AdminMainBodyMain.class);
    }}

