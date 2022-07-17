package servlets.client.information;

import com.google.gson.Gson;
import customes.Client;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import servletDTO.LoanInformationObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoansAsBorrowServlet", urlPatterns = "/LoansAsBorrow")
public class LoansAsBorrowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Gson gson = new Gson();


        List<LoanInformationObj> loanInformationObjList = new ArrayList<>();
        Client client = systemEngine.getDatabase().getClientByname(usernameFromSession);
        if(client == null)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("The user don't exist ");
            System.out.println("Error, The user don't exist ");
            return;
        }
        List<Loan> loanList = client.getClientAsBorrowLoanList();
        for (Loan loan : loanList) {
            double AsSellerLoanPrice = loan.getTotalRemainingFund() * (loan.calculateClientLoanOwningPercentage(usernameFromSession) / 100);
            loanInformationObjList.add(new LoanInformationObj(loan.getLoanID(), loan.getBorrowerName(), loan.getLoanCategory(), loan.getStatus(), AsSellerLoanPrice));
        }


        String jsonResponse = gson.toJson(loanInformationObjList);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}