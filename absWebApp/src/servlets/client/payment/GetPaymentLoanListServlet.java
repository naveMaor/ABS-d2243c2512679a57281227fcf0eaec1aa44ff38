package servlets.client.payment;


import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import loan.enums.eLoanStatus;
import servletDTO.Payment.LoanPaymentObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServlet;


@WebServlet(name = "GetPaymentLoanListServlet", urlPatterns = "/PaymentsLoanList")
public class GetPaymentLoanListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        List<Loan> tmp = systemEngine.getDatabase().o_getAllLoansByClientName(usernameFromSession);

        List<LoanPaymentObj> returnArrayList =new ArrayList<>();


        for (Loan loan:tmp
        ) {
            eLoanStatus status = loan.getStatus();
            if((status== eLoanStatus.RISK)||(status==eLoanStatus.ACTIVE)){
                LoanPaymentObj loanPaymentObj = new LoanPaymentObj(loan);
                returnArrayList.add(loanPaymentObj);
            }
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(returnArrayList);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
