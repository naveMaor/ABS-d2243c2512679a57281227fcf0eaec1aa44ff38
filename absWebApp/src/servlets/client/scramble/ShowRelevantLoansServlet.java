package servlets.client.scramble;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import engine.scrambleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletDTO.RelevantLoansRequestObj;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "ShowRelevantLoansServlet", urlPatterns = "/RelevantLoans")
public class ShowRelevantLoansServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");

        Scanner s = new Scanner(request.getInputStream(), "UTF-8");
        String reqBodyAsString = s.hasNext() ? s.next() : null;

        RelevantLoansRequestObj scrambleRequest = new Gson().fromJson(reqBodyAsString, RelevantLoansRequestObj.class);

        scrambleService service = new scrambleService(scrambleRequest.getClientName(), scrambleRequest.getMinInterest(), scrambleRequest.getMinYaz(), scrambleRequest.getMaxOpenLoans(), scrambleRequest.getChosenCategories(), scrambleRequest.getMaxOwnership());

        Gson gsonResponse = new Gson();
        String jsonResponse = gsonResponse.toJson(service);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("On login, request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private List<String> parseBody(String reqBodyAsString) {
        if (reqBodyAsString != null || !reqBodyAsString.isEmpty()) {
            JsonArray jsonArray = new JsonParser().parse(reqBodyAsString).getAsJsonArray();

            List<String> list = new ArrayList<>();
            jsonArray.forEach(jsonElement -> {
                list.add(jsonElement.getAsString());
            });
            return list;
        }
        return null;
    }
}

