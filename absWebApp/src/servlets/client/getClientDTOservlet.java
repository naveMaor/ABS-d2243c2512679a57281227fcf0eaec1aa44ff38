package servlets.client;

import com.google.gson.Gson;
import customes.Client;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletDTO.ClientDTOforServlet;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getClientDTOservlet", urlPatterns = "/ClientDTO")
public class getClientDTOservlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());



        Client client = systemEngine.getDatabase().getClientByname(usernameFromSession);
        if(client == null)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("The user don't exist ");
            System.out.println("Error, The user don't exist ");
            return;
        }
        ClientDTOforServlet clientDTOforServlet = new ClientDTOforServlet(client);


        Gson gson = new Gson();
        String jsonResponse = gson.toJson(clientDTOforServlet);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
