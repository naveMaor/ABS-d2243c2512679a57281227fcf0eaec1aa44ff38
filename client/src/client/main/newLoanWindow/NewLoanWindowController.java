package client.main.newLoanWindow;

import client.main.ClientMainController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import servletDTO.LoanInformationObj;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;

public class NewLoanWindowController {

    private ClientMainController clientMainController;


    @FXML
    private TextField CapitalText;

    @FXML
    private TextField CategoryText;

    @FXML
    private TextField IdText;

    @FXML
    private TextField InterestText;

    @FXML
    private TextField PayEveryYazText;

    @FXML
    private TextField YazTImeText;

    String id;
    String category;
    int capital;
    int interest;
    int payEveryYaz;
    int YazTimeText;

    @FXML
    void CreateNewLoanButton(ActionEvent event) {
        try {
            id = IdText.getText();
            category = CategoryText.getText();

            String capitalTmp = CapitalText.getText();
            if (capitalTmp.matches("[0-9]+"))
                capital = Integer.parseInt(capitalTmp);

            String interestTmp = InterestText.getText();
            if (interestTmp.matches("[0-9]+"))
                interest = Integer.parseInt(interestTmp);

            String payEveryYazTmp = PayEveryYazText.getText();
            if (payEveryYazTmp.matches("[0-9]+"))
                payEveryYaz = Integer.parseInt(payEveryYazTmp);

            String YazTimeTextTmp = YazTImeText.getText();
            if (YazTimeTextTmp.matches("[0-9]+"))
                YazTimeText = Integer.parseInt(YazTimeTextTmp);

            if((capital<=0)||(interest<=0)||(payEveryYaz<=0)||(YazTimeText<=0)){
                Alert alert = new Alert(Alert.AlertType.ERROR, "invalid parameters");
                alert.showAndWait();
                return;
            }


            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "invalid parameters");
                alert.showAndWait();
                return;
            }
        String clientName = clientMainController.getCurrClient().getFullName();
        LoanInformationObj loanInformationObj = new LoanInformationObj(category,id,clientName, (double) capital, (double) interest,payEveryYaz,YazTimeText);
        createNewLoanRequest(loanInformationObj);
    }

    private void createNewLoanRequest(LoanInformationObj loanInformationObj){

        String jsonExistChosenCategories = HttpClientUtil.GSON_INST.toJson(loanInformationObj,LoanInformationObj.class);

        RequestBody body = RequestBody.create(jsonExistChosenCategories, HttpClientUtil.JSON);

        String finalUrl = HttpUrl
                .parse(Constants.NEW_LOAN_FROM_USER)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();


        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url create new loan request from user")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    String jsonOfClientString = null;
                    try {
                        jsonOfClientString = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.body().close();

                    if(response.code()==200){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        //clientMainController.loadData();
                        alert.showAndWait();
                        resetFields();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,jsonOfClientString);
                        alert.showAndWait();
                    }
                });
            }

        });
    }

    public void setMainController(ClientMainController mainController) {
        this.clientMainController = mainController;
    }

    private void resetFields(){
        CapitalText.clear();
        CategoryText.clear();
        IdText.clear();
        InterestText.clear();
        PayEveryYazText.clear();
        YazTImeText.clear();
    }
}
