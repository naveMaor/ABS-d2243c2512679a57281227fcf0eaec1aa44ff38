package client.main;

import client.main.newLoanWindow.NewLoanWindowController;
import client.sub.main.CustomerMainBodyController;
import com.google.gson.Gson;
import com.sun.deploy.cache.JarSigningData;
import common.LoginController;
import customes.Client;
import data.File.XmlFile;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import servletDTO.ClientDTOforServlet;
import util.Constants;
import util.HttpClientUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class ClientMainController {
    private Stage primaryStage;
    @FXML private BorderPane root;
    @FXML private AnchorPane loginComponent;
    @FXML private LoginController loginComponentController;
    @FXML private ScrollPane customerMainBody;
    @FXML private CustomerMainBodyController customerMainBodyController;
    //Engine engine = Engine.getInstance();
    private Engine engine = new Engine();
    private ClientDTOforServlet currClient;
    private NewLoanWindowController newLoanWindowController;

    @FXML
    private Label welcomeLable;
    @FXML
    private Button NewLoan;
    @FXML
    private Label currentYazLable;



    private StringProperty currentUserName = new SimpleStringProperty();
    private StringProperty currentYaz = new SimpleStringProperty();


    public void NewManualLoanButton(ActionEvent actionEvent) {
        Stage Newstage = new Stage();
        Newstage.setMinHeight(600);
        Newstage.setMinWidth(600);
        Newstage.setTitle("Add new loan");

        URL newLoanWindow = getClass().getResource("newLoanWindow/NewLoanWindow.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(newLoanWindow);
            Parent root = fxmlLoader.load();
            newLoanWindowController = fxmlLoader.getController();

            newLoanWindowController.setMainController(this);

            Scene scene = new Scene(root, 700, 600);
            Newstage.setScene(scene);
            Newstage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }




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
        customerMainBodyController.setMainController(this);
        welcomeLable.textProperty().bind(Bindings.concat("Welcome ",currentUserName));
        currentYazLable.textProperty().bind(Bindings.concat(currentYaz));

        //customerMainBody.setFitToWidth(true); // tried to set the node to middle of the screen
        //CustomerMainBody.setFitToHeight(true);
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    public void updateYaz(int yaz){
        currentYaz.set(String.valueOf(yaz));
    }

    public void switchToClientDesktop(){
        synchronized (this) {
            root.setBottom(null);
            root.setCenter(clientDesktop);
            root.setTop(header);
            try {
                createClientDTORequest();
            } catch (IOException e) {
                e.printStackTrace();
            }

            customerMainBodyController.loadData();
        }



    }


    public void NewLoanButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();
        createFileRequest(absolutePath);
    }


    public void createFileRequest(String absolutePath){
        File f = new File(absolutePath);
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", f.getName(), RequestBody.create(f, MediaType.parse("text/xml")))
                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
                        .build();

        String finalUrl = HttpUrl
                .parse(Constants.NEW_LOAN_FROM_FILE)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Unknown Error");
                    alert.showAndWait();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() != 200){
                    Platform.runLater(() ->
                    {
                        try {

                            Alert alert = new Alert(Alert.AlertType.ERROR,response.body().string());
                            alert.showAndWait();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else{
                    Platform.runLater(() ->
                    {
                        try {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,response.body().string());
                            alert.showAndWait();
                            loadData();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                //   return false;
            }
        });
}


/*    public void createClientRequest() throws IOException {
        String finalUrl = HttpUrl
                .parse(Constants.GET_CLIENT)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = HttpClientUtil.execute(request);

        if(response.code() == 200)
        {
                String jsonOfClientString = response.body().string();
                currClient = new Gson().fromJson(jsonOfClientString, Client.class);
        }
        else
        {
            System.out.println("failed to GET CLIENT");
        }

    }*/


    public void createClientDTORequest() throws IOException {
        String finalUrl = HttpUrl
                .parse(Constants.GET_CLIENT_DTO)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = HttpClientUtil.execute(request);

        if(response.code() == 200)
        {
                String jsonOfClientString = response.body().string();
                currClient = new Gson().fromJson(jsonOfClientString, ClientDTOforServlet.class);
        }
        else
        {
            System.out.println("failed to GET CLIENT");
        }

    }

    public ClientDTOforServlet getCurrClient() {
        try {
            createClientDTORequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currClient;
    }

    public void loadData(){
        customerMainBodyController.loadData();
    }
}