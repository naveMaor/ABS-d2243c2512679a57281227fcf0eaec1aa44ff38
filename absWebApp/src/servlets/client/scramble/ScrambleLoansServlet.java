package servlets.client.scramble;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import servletDTO.ScrambleRequestObj;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;


@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "ScrambleLoansServlet", urlPatterns = "/ScrambleLoans")
public class ScrambleLoansServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        Gson gson = new Gson();
        Part jsonResponse = request.getPart("body");
        ScrambleRequestObj scrambleRequest = new Gson().fromJson(jsonResponse.getInputStream().toString(), ScrambleRequestObj.class);
        //todo add logic test like name ==null
        try {
            systemEngine.investing_according_to_agreed_risk_management_methodology(scrambleRequest.getLoansListToInvest(), scrambleRequest.getWantedInvestment(), scrambleRequest.getClientName(), scrambleRequest.getMaxPercentage());
            response.setStatus(200);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
