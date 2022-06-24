package servlets.payment;

import engine.Engine;
import exceptions.messageException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loan.Loan;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class payEntirePaymentServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        List<Loan> PayLoanstmp = new ArrayList<>();
        ObservableList<Loan> loanList = FXCollections.observableArrayList();

        List<String> loanNameList = null;

        for (Loan loan:PayLoanstmp){
            if (loanNameList.contains(loan.getLoanID())){
                loanList.add(loan);
            }
        }

        try {
            systemEngine.payEntirePaymentForLoanList(loanList);
        } catch (messageException e) {
            e.printStackTrace();
        }

    }}
