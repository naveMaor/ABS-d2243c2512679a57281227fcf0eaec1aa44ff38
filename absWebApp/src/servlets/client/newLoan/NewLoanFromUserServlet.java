package servlets.client.newLoan;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.scene.control.Alert;
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
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next() : "";




        try {
            LoanInformationObj loanInformationObj = new Gson().fromJson(reqBodyAsString, LoanInformationObj.class);


            if(loanInformationObj!= null) {
                Loan newLoan = new Loan(loanInformationObj.getLoanID(), loanInformationObj.getBorrowerName(), loanInformationObj.getLoanCategory(), loanInformationObj.getLoanOriginalDepth(), loanInformationObj.getOriginalLoanTimeFrame(), loanInformationObj.getPaymentFrequency(), loanInformationObj.getPaymentFrequency());
                systemEngine.addNewLoanFromUser(newLoan);
                response.getOutputStream().print("successfully, I Placed the loan.");
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print("The request didn't send properly we cant convert the loan json the filed type");
                return;
            }
        }
        catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().print(e.toString());
            return;
        }
        response.setStatus(200);

    }
}
