package servlets;

import com.google.gson.Gson;
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

@WebServlet(name = "LoansAsLenderServlet", urlPatterns = "/LoansAsLender")
public class LoansAsLenderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        List<Loan> loanList = systemEngine.getDatabase().getClientByname(usernameFromSession).getClientAsLenderLoanList();
        List<LoanInformationObj> loanInformationObjList =new ArrayList<>();

        for(Loan loan:loanList){
            loanInformationObjList.add(new LoanInformationObj(loan.getLoanID(),loan.getBorrowerName(),loan.getLoanCategory(),loan.getStatus()));
        }
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(loanInformationObjList);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
