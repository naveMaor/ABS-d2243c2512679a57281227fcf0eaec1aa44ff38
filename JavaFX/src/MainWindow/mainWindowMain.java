
package MainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class mainWindowMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // load main fxml
        Parent load = FXMLLoader.load(getClass().getResource("mainWindowFXML.fxml"));
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "mainWindowFXML.fxml"
                )
        );

        ScrollPane root = loader.load();
        mainWindowController controller =loader.getController();
        controller.setPrimaryStage(primaryStage);


        primaryStage.setTitle("ABS System");

        Scene scene = new Scene(load, 1200, 800);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(mainWindowMain.class);
    }}

