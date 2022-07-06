package client;


import client.main.ClientMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static void main(String[] args) {
        launch(ClientMain.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // load main fxml
        Parent load = FXMLLoader.load(getClass().getResource("/client/main/ClientMain.fxml"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/client/main/ClientMain.fxml")
        );

        ScrollPane root = loader.load();

        ClientMainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);


        primaryStage.setTitle("ABS System");

        Scene scene = new Scene(load, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}