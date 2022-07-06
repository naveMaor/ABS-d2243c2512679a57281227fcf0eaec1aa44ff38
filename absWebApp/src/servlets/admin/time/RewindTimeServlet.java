package servlets.admin.time;

import com.google.gson.Gson;
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
import java.util.Scanner;

@WebServlet(name = "RewindTimeServlet", urlPatterns = "/rewindTime")
public class RewindTimeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String reqBodyAsString = s.hasNext() ? s.next() : "";

        int yaz = new Gson().fromJson(reqBodyAsString, int.class);
        systemEngine.loadRewindData(yaz);

        String jsonResponse = new Gson().toJson(yaz);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }

        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
