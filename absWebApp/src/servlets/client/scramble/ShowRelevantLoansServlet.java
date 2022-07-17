package servlets.client.scramble;

import com.google.gson.Gson;
import customes.Client;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.collections.ObservableList;
import loan.Loan;
import servletDTO.LoanInformationObj;
import servletDTO.RelevantLoansRequestObj;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "ShowRelevantLoansServlet", urlPatterns = "/RelevantLoans")
public class ShowRelevantLoansServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String errorResponse = "";
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next() : "";
        RelevantLoansRequestObj scrambleRequest = new Gson().fromJson(reqBodyAsString, RelevantLoansRequestObj.class);


        if (scrambleRequest != null) {
            try {

                Client client = systemEngine.getDatabase().getClientByname(scrambleRequest.getClientName());
                if(client == null)
                {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("Error, The user don't exist ");
                    System.out.println("Error, The user don't exist ");
                    return;
                }
                Double balanceOfUser = client.getMyAccount().getCurrBalance();

                if (systemEngine.getDatabase().getClientByname(scrambleRequest.getClientName()).getMyAccount().getCurrBalance() < scrambleRequest.getAmountToInvest()) {
                    errorResponse = "You cant invest more then current balance, your current balance is: " + String.valueOf(balanceOfUser);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorResponse);
                } else if (scrambleRequest.getAmountToInvest() < 1) {
                    errorResponse = "Error, you must insert positive number of money";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorResponse);
                } else {

                    ObservableList<Loan> filterLoans = systemEngine.O_getLoansToInvestList(scrambleRequest.getClientName(), scrambleRequest.getMinInterest(), scrambleRequest.getMinYaz(), scrambleRequest.getMaxOpenLoans(), scrambleRequest.getChosenCategories(), scrambleRequest.getMaxOwnership());
                    if (filterLoans.size() != 0) {
                        List<LoanInformationObj> loans = new ArrayList<>();
                        for (Loan l : filterLoans) {
                            loans.add(new LoanInformationObj(l.getLoanCategory(), l.getStatus(), l.getLoanID(), l.getBorrowerName(), l.getLoanOriginalDepth(), l.getTotalLoanCostInterestPlusOriginalDepth(), l.getInterestPercentagePerTimeUnit(), l.getSelect(), l.getPaymentFrequency(), l.getOriginalLoanTimeFrame()));
                        }

                        String jsonResponse = new Gson().toJson(loans);
                        try (PrintWriter out = response.getWriter()) {
                            out.print(jsonResponse);
                            out.flush();
                        }
                        System.out.println("send the relevant loans to invest, request URI is: " + request.getRequestURI());
                    } else {
                        System.out.println("There is No relevant loans");
                        response.getOutputStream().print("There is No relevant loans");

                    }
                    response.getOutputStream().print("Here is the List of Loans:");
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().print(e.toString());

            }

        }
    }
}

