package client;

/*import MainWindow.mainWindowController;
import MainWindow.mainWindowMain;*/
import client.main.ClientMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // load main fxml
        Parent load = FXMLLoader.load(getClass().getResource("/client/main/ClientMain.fxml"));
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/client/main/ClientMain.fxml"
                )
        );

        ScrollPane root = loader.load();
        //BorderPane root = loader.load();
        ClientMainController controller =loader.getController();
        controller.setPrimaryStage(primaryStage);


        primaryStage.setTitle("ABS System");

        Scene scene = new Scene(load, 1200, 800);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(ClientMain.class);
    }}