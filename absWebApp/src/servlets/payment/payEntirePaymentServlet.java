package servlets.payment;

import com.google.gson.Gson;
import engine.Engine;
import exceptions.messageException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loan.Loan;
import loan.enums.eLoanStatus;
import servletDTO.Payment.LoanPaymentObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "payEntirePaymentServlet", urlPatterns = "/PayEntirePayment")
public class payEntirePaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get engine and info
        //stam
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream(), "UTF-8");
        String reqBodyAsString = s.hasNext() ? s.next() : null;


        //create tmp variables
        List<Loan> AllLoans = systemEngine.getDatabase().getLoanList();
        ObservableList<Loan> loanList = FXCollections.observableArrayList();
        List<LoanPaymentObj> availableLoansToPay = new ArrayList<>();

        //load list of loans from request body
        String[] loanNameArray = new Gson().fromJson(reqBodyAsString, String[].class);
        List<String> loanNameList = new ArrayList<>(Arrays.asList(loanNameArray));
        for (Loan loan:AllLoans){
            if (loanNameList.contains(loan.getLoanID())){
                loanList.add(loan);
            }
        }
        //pay Entire payment
        try {
            systemEngine.payEntirePaymentForLoanList(loanList);
        } catch (messageException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(e.getMessage());
            e.printStackTrace();
        }


        //return availble loans to to response in order to update the JavaFx Table on good respons
        ObservableList<Loan> allLoansByClientName =  systemEngine.getDatabase().o_getAllLoansByClientName(usernameFromSession);
        for (Loan loan:allLoansByClientName) {
            eLoanStatus status = loan.getStatus();
            if((status== eLoanStatus.RISK)||(status==eLoanStatus.ACTIVE)){
                LoanPaymentObj loanPaymentObj =new LoanPaymentObj(loan.getNextExpectedPaymentAmountDataMember(),loan.getTotalRemainingLoan(),loan.getLoanID(),loan.getNextYazToPay(),loan.getStatus());
                availableLoansToPay.add(loanPaymentObj);
            }
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(availableLoansToPay);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);

    }}
