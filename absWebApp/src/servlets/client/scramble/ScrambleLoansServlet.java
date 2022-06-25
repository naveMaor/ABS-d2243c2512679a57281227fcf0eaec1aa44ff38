package servlets.client.scramble;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import servletDTO.LoanInformationObj;
import servletDTO.RelevantLoansRequestObj;
import servletDTO.ScrambleRequestObj;
import utils.ServletUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@WebServlet(name = "ScrambleLoansServlet", urlPatterns = "/ScrambleLoans")
public class ScrambleLoansServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");


        response.setContentType("application/json");
        String errorResponse = "";
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next():"" ;
        ScrambleRequestObj scrambleRequest = new Gson().fromJson(reqBodyAsString, ScrambleRequestObj.class);



        if (scrambleRequest == null) {
            errorResponse = "Request didnt send properly please try again";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorResponse);
            response.setStatus(401);
        } else {

            try {
                List<Loan> loans = new ArrayList<>();
                for (LoanInformationObj loan : scrambleRequest.getLoansListToInvest()) {
                    loans.add(systemEngine.getDatabase().getLoanById(loan.getLoanID()));
                }
                if (loans.size() < 1 || loans.contains(null)) {
                    errorResponse = "didnt find loans to invest ";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorResponse);
                } else {
                    systemEngine.investing_according_to_agreed_risk_management_methodology(loans, scrambleRequest.getWantedInvestment(), scrambleRequest.getClientName(), scrambleRequest.getMaxPercentage());
                    response.setStatus(200);

                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

}
