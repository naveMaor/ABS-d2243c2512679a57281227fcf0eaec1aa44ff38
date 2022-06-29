package servlets.admin.loan;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loan.Loan;
import servletDTO.admin.InnerTableObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "GetInnerTableServlet", urlPatterns = "/innerTable")
public class GetInnerTableServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get engine and info
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next() : "";



        //create tmp variables
        List<Loan> AllLoans = systemEngine.getDatabase().getLoanList();

        //load list of loans from request body
        String loanName = new Gson().fromJson(reqBodyAsString, String.class);

        InnerTableObj innerTableObj =null;
        for(Loan loan:AllLoans){
            if (loan.getLoanID().equals(loanName)){
                innerTableObj = new InnerTableObj(loan);
            }
        }


/*        InnerTableObj innerTableObj =
                (InnerTableObj) AllLoans
                        .stream()
                        .filter(loan -> loan.getLoanID().equals(loanName))
                        .map(InnerTableObj::new);*/

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(innerTableObj);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
