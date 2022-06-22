package servlets;

import com.google.gson.Gson;
import data.Database;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "LoansPerCustomerServlet", urlPatterns = "/LoansPerCustomer")
public class LoansPerCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //todo:change response content type
        response.setContentType("application/json");
        //response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        List<Loan> loanList = systemEngine.getDatabase().getClientByname(usernameFromSession).getClientAsLenderLoanList();
        Gson gson = new Gson();
        //todo might need to remez to gson that you want a loan list type of json
        String jsonResponse = gson.toJson(loanList);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("On login, request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
