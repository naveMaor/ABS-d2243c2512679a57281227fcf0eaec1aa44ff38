package servlets.admin;

import com.google.gson.Gson;
import customes.Client;
import data.Database;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import old.ClientObj;
import servletDTO.client.ClientLoansObj;
import time.Timeline;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name = "AdminClientInfoServlet", urlPatterns = "/AdminClientInfo")
public class AdminClientInfoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getSystemEngine(getServletContext());

            List<Client> clientsObjList = engine.getDatabase().getClientsList();
            ObservableList<ClientLoansObj> clientLoansObservableList = FXCollections.observableArrayList();
            for (Client clientObj:clientsObjList){
                ClientLoansObj clientLoans = new ClientLoansObj(clientObj);
                clientLoansObservableList.add(clientLoans);
            }
            String json = gson.toJson(clientLoansObservableList);
            out.println(json);
            out.flush();
        }
    }

}
