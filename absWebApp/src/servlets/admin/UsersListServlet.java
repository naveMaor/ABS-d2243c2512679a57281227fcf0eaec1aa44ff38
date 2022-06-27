package servlets.admin;

import com.google.gson.Gson;
import customes.Client;
import engine.Engine;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletDTO.client.ClientBalanceObj;
import utils.ServletUtils;

public class UsersListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getSystemEngine(getServletContext());
            List<Client> usersList = engine.getDatabase().getClientsList();
            List<ClientBalanceObj> users = new ArrayList<>();
            for (Client c: usersList)
            {
                users.add(new ClientBalanceObj(c.getFullName(),c.getMyAccount().getCurrBalance()));
            }
            String json = gson.toJson(users);
            out.println(json);
            out.flush();
        }
    }

}
