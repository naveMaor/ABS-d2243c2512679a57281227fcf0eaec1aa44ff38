package servlets.client.scramble;

import com.google.gson.Gson;
import engine.scrambleService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.USERNAME;

@WebServlet(name = "ShowRelevantLoansServlet", urlPatterns = "/relevantLoans")
public class ShowRelevantLoansServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");


        String usernameFromParameter = request.getParameter(USERNAME);
        int minInterestParameter = Integer.parseInt(request.getParameter("minInterest"));
        int minYazParameter = Integer.parseInt(request.getParameter("minYaz"));
        int maxOpenLoansParameter = Integer.parseInt(request.getParameter("maxOpenLoans"));
        String jsonExistChosenCategories = request.getParameter("existChoosenCategories");
        String[] ChoosenCategoriesString = new Gson().fromJson(jsonExistChosenCategories, String[].class);
        ObservableList<String> existChoosenCategories = FXCollections.observableArrayList();
        existChoosenCategories.addAll(ChoosenCategoriesString);
        int maxOwnership = Integer.parseInt(request.getParameter("maxOwnership"));

        if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);

        } else {
            usernameFromParameter = usernameFromParameter.trim();
        }
        scrambleService service = new scrambleService(usernameFromParameter, minInterestParameter, minYazParameter, maxOpenLoansParameter, existChoosenCategories, maxOwnership);

        Gson gson = new Gson();
        //todo might need to remez to gson that you want a loan list type of json
        String jsonResponse = gson.toJson(service);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("On login, request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
