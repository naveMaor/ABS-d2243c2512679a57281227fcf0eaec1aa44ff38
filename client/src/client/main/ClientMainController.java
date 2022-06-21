package client.main;

import client.sub.main.CustomerMainBodyController;
import common.LoginController;
import data.File.XmlFile;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

public class ClientMainController {
    private Stage primaryStage;
    @FXML private BorderPane root;
    @FXML private AnchorPane loginComponent;
    @FXML private LoginController loginComponentController;
    @FXML private ScrollPane customerMainBody;
    @FXML private CustomerMainBodyController customerMainBodyController;
    Engine engine = Engine.getInstance();
    public enum MessageType{Error,Successfully,Information};

    private StringProperty currentUserName = new SimpleStringProperty();


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    private Label welcomeLable;


    Node header ;
    Node clientDesktop ;
    Node login ;


    @FXML
    public void initialize() {
        header = root.getTop();
        clientDesktop = root.getCenter();
        login = root.getBottom();
        welcomeLable.setAlignment(Pos.CENTER);
        welcomeLable.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
        root.setCenter(null);
        root.setTop(null);
        loginComponentController.setMainController(this);
        welcomeLable.textProperty().bind(Bindings.concat("Welcome ",currentUserName));


        //customerMainBody.setFitToWidth(true); // tried to set the node to middle of the screen
        //CustomerMainBody.setFitToHeight(true);
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    public void switchToClientDesktop(){
        root.setBottom(null);
        root.setCenter(clientDesktop);
        root.setTop(header);
    }


}