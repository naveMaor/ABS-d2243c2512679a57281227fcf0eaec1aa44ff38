package servlets.client.buyLoan;

import com.google.gson.Gson;
import engine.Engine;
import exceptions.BalanceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import servletDTO.BuyLoanObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "BuyLoanServlet", urlPatterns = "/BuyLoan")
public class BuyLoanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get engine and info
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next() : "";

        //load LOAN NAME from request body
        BuyLoanObj buyLoanObj = new Gson().fromJson(reqBodyAsString, BuyLoanObj.class);
        Loan loan =  systemEngine.getDatabase().getLoanById(buyLoanObj.getLoanID());

        //create buy
        try {
            systemEngine.createLoanBuy(loan,usernameFromSession,buyLoanObj.getSellerName());
        } catch (BalanceException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(e.getMessage());
            e.printStackTrace();
            return;
        }


        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
