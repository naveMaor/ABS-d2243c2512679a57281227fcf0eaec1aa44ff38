package client.sub;

import Money.operations.Transaction;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import servletDTO.BuyLoanObj;
import servletDTO.ClientDTOforServlet;
import servletDTO.LoanInformationObj;
import servletDTO.Payment.LoanPaymentObj;
import util.Constants;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class ClientRefresher extends TimerTask {

    private final Consumer<List<LoanInformationObj>> borrowLoanConsumer;
    private final Consumer<List<LoanInformationObj>> lenderLoanConsumer;
    private final Consumer<List<Transaction>> updatedTransactionsData;
    private final Consumer<List<LoanPaymentObj>> loadLoanPaymentTableData;
    private final Consumer<ClientDTOforServlet> getClient;
    private final Consumer<Set<String>> updateScrambleCategories;
    private final Consumer<List<BuyLoanObj>> loadTableBuyData;
    private final BooleanProperty systemInRewindMode = new SimpleBooleanProperty(false);
    private String clientName;

    public ClientRefresher(Consumer<List<LoanInformationObj>> borrowLoanConsumer, Consumer<List<LoanInformationObj>> lenderLoanConsumer, Consumer<List<LoanPaymentObj>> loadLoanPaymentTableData, Consumer<ClientDTOforServlet> getClient, BooleanProperty systemInRewindMode, Consumer<Set<String>> updateScrambleCategories, Consumer<List<BuyLoanObj>> loadTableBuyData, Consumer<List<Transaction>> updatedTransactionsData) {
        this.borrowLoanConsumer = borrowLoanConsumer;
        this.lenderLoanConsumer = lenderLoanConsumer;
        this.loadLoanPaymentTableData = loadLoanPaymentTableData;
        this.getClient = getClient;
        this.updatedTransactionsData = updatedTransactionsData;
        this.updateScrambleCategories = updateScrambleCategories;
        this.loadTableBuyData = loadTableBuyData;
        this.systemInRewindMode.bindBidirectional(systemInRewindMode);

    }


    private void createLoansAsLenderRequest(){
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOANS_AS_LENDER)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url information body controller")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            ObservableList<LoanInformationObj> clientAsLenderLoanList = FXCollections.observableArrayList();
                            clientAsLenderLoanList.clear();
                            String jsonOfClientString = response.body().string();
                            // response.body().close();
                            LoanInformationObj[] loanAsLenderList = new Gson().fromJson(jsonOfClientString, LoanInformationObj[].class);
                            clientAsLenderLoanList.addAll(loanAsLenderList);
                            lenderLoanConsumer.accept(clientAsLenderLoanList);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
    private void createLoansAsBorrowerRequest(){
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOANS_AS_BORROW)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url information body")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            ObservableList<LoanInformationObj> clientAsBorrowLoanList = FXCollections.observableArrayList();
                            clientAsBorrowLoanList.clear();
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            LoanInformationObj[] loanAsBorrowList = new Gson().fromJson(jsonOfClientString, LoanInformationObj[].class);
                            clientAsBorrowLoanList.addAll(loanAsBorrowList);
                            borrowLoanConsumer.accept(clientAsBorrowLoanList);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
    private void createTransactionListRequest(){
        String finalUrl = HttpUrl
                .parse(Constants.GET_TRANSACTION_LIST)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url transaction list")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            Transaction[] TransactionList = new Gson().fromJson(jsonOfClientString, Transaction[].class);
                            updatedTransactionsData.accept(Arrays.asList(TransactionList));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
    public void createClientDTORequest()  {
        String finalUrl = HttpUrl
                .parse(Constants.GET_CLIENT_DTO)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = HttpClientUtil.execute(request);

        if (response.code() == 200) {
            ClientDTOforServlet currClient;
            String jsonOfClientString = null;
            try {
                jsonOfClientString = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            currClient = new Gson().fromJson(jsonOfClientString, ClientDTOforServlet.class);
            getClient.accept(currClient);
            clientName = currClient.getFullName();
        } else {
            getClient.accept(null);
            clientName = "";
            System.out.println("Client doesn't exist");
        }

    }
    private void updatePaymentLoanListRequest(){

        String finalUrl = HttpUrl
                .parse(Constants.GET_PAYMENT_LOAN_LIST)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();


        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url payment load table")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            ObservableList<LoanPaymentObj> loanListForTable =  FXCollections.observableArrayList();
                            loanListForTable.clear();
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            LoanPaymentObj[] tmp = new Gson().fromJson(jsonOfClientString, LoanPaymentObj[].class);
                            loanListForTable.addAll(tmp);
                            loadLoanPaymentTableData.accept(loanListForTable);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
    private void getAllCategories() {

        String finalUrl = HttpUrl
                .parse(Constants.CATEGORIES)
                .newBuilder()
                .addQueryParameter("username", clientName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to get categories from server")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Platform.runLater(() -> {

                    if (response.code() != 200) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Categories Not return from server!");
                        alert.showAndWait();
                    } else {
                        try {
                            Set<String> allCategoriesList = new HashSet<>();
                            String jsonOfClientString = response.body().string();
                            response.body().close();
                            String[] cat = new Gson().fromJson(jsonOfClientString, String[].class);
                            allCategoriesList.addAll(Arrays.asList(cat));
                            updateScrambleCategories.accept(allCategoriesList);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });

            }
        });
    }
    private void buyLoanTableListRequest(){
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.GET_LOAN_TO_BUY_LIST)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failed to call url get loan to buy list")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        if(response.code()==200){
                            ObservableList<BuyLoanObj> LoanToBuyList = FXCollections.observableArrayList();
                            LoanToBuyList.clear();
                            String jsonOfClientString = response.body().string();
                            // response.body().close();
                            Gson gson = new Gson();
                            BuyLoanObj[] BuyLoansArray = new Gson().fromJson(jsonOfClientString, BuyLoanObj[].class);
                            LoanToBuyList.addAll(BuyLoansArray);
                            loadTableBuyData.accept(LoanToBuyList);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }
    private void createRewindRequest(){
        String finalUrl = HttpUrl
                .parse(Constants.GET_REWIND)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

//        HttpClientUtil.runAsync(request, new Callback() {
//
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Platform.runLater(() ->
//                        System.out.println("failed to call url rewind")
//                );
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//                Platform.runLater(() -> {
//                    try {
//                        if(response.code()==200){
//                            String jsonOfClientString = response.body().string();
//                            response.body().close();
//                            boolean rewind = new Gson().fromJson(jsonOfClientString, boolean.class);
//                            systemInRewindMode.setValue(rewind);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//
//        });

        Response response = HttpClientUtil.execute(request);
        if (response.code() == 200) {

            try {
                String jsonOfClientString = response.body().string();
                response.body().close();
                boolean rewind = new Gson().fromJson(jsonOfClientString, boolean.class);
                systemInRewindMode.setValue(rewind);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("failed to call url rewind");
        }
    }



    @Override
    public void run() {
        createRewindRequest();
/*        if (systemInRewindMode.get()) {
            return;
        }*/
        createClientDTORequest();
        createTransactionListRequest();
        createLoansAsLenderRequest();
        createLoansAsBorrowerRequest();
        updatePaymentLoanListRequest();
        getAllCategories();
        buyLoanTableListRequest();
    }
}
