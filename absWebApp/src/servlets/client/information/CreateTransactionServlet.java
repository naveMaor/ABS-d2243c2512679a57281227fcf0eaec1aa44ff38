package servlets.client.information;

import Money.operations.Transaction;
import com.google.gson.Gson;
import engine.Engine;
import exceptions.BalanceException;
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

@WebServlet(name = "CreateTransactionServlet", urlPatterns = "/CreateTransaction")
public class CreateTransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        int amount = Integer.parseInt(request.getParameter("amount"));

        try {
            systemEngine.AccountTransaction(amount,usernameFromSession);
        } catch (BalanceException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Transaction> TransactionList = systemEngine.getClientTransactionsList(usernameFromSession);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(TransactionList);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("request URI is: " + request.getRequestURI());

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
