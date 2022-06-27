package servlets.client.newLoan;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import servletDTO.LoanInformationObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "NewLoanFromUserServlet", urlPatterns = "/NewLoanFromUser")
public class NewLoanFromUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get engine and info
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next() : "";

        LoanInformationObj loanInformationObj = new Gson().fromJson(reqBodyAsString, LoanInformationObj.class);
        Loan newLoan = new Loan(loanInformationObj.getLoanID(), loanInformationObj.getBorrowerName(), loanInformationObj.getLoanCategory(), loanInformationObj.getLoanOriginalDepth(), loanInformationObj.getOriginalLoanTimeFrame(), loanInformationObj.getPaymentFrequency(), loanInformationObj.getPaymentFrequency());
        try {
            systemEngine.addNewLoanFromUser(newLoan);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getOutputStream().print(e.getMessage());
            return;
        }
        response.setStatus(200);

    }
}
