package servlets.client.information;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import loan.enums.eLoanStatus;
import servletDTO.BuyLoanObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "PutLoanOnSaleServlet", urlPatterns = "/PutLoanOnSell")
public class PutLoanOnSaleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get engine and info
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next() : "";

        //load LOAN NAME and price from request body
        String loanName = new Gson().fromJson(reqBodyAsString, String.class);
        Loan loan = systemEngine.getDatabase().getLoanById(loanName);
        BuyLoanObj buyLoanObj = new BuyLoanObj(loan,usernameFromSession);

        if(systemEngine.getDatabase().getLoanById(buyLoanObj.getLoanID()).getStatus()!= eLoanStatus.ACTIVE)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("LOAN HAS TO BE ACTIVE STATUS");
            return;
        }

        //set loan on sale
        try {
            systemEngine.getDatabase().putLoanOnSale(usernameFromSession,buyLoanObj);
        }
        catch (IOException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(e.getMessage());
            return;
        }

        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
