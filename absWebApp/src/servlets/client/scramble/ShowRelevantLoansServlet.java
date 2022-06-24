package servlets.client.scramble;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.collections.ObservableList;
import loan.Loan;
import servletDTO.RelevantLoansRequestObj;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet(name = "ShowRelevantLoansServlet", urlPatterns = "/RelevantLoans")
public class ShowRelevantLoansServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream(), "UTF-8");
        String reqBodyAsString = s.hasNext() ? s.next() : null;
        RelevantLoansRequestObj scrambleRequest = new Gson().fromJson(reqBodyAsString, RelevantLoansRequestObj.class);
        if (scrambleRequest != null) {

            ObservableList<Loan> filterLoans = systemEngine.O_getLoansToInvestList(scrambleRequest.getClientName(), scrambleRequest.getMinInterest(), scrambleRequest.getMinYaz(), scrambleRequest.getMaxOpenLoans(), scrambleRequest.getChosenCategories(), scrambleRequest.getMaxOwnership());
            if (filterLoans.size() != 0) {
                String jsonResponse = new Gson().toJson(filterLoans);

                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse);
                    out.flush();
                }
                System.out.println("On login, request URI is: " + request.getRequestURI());
            } else {
                System.out.println("There is No relevant loans");
                response.getOutputStream().print("There is No relevant loans");

            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("ERROR12312312312312312 \n");

        }
    }
}

