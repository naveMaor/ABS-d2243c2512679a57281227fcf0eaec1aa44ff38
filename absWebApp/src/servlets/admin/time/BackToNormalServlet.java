package servlets.admin.time;


import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import time.Timeline;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "BackToNormalServlet", urlPatterns = "/loadNormal")
public class BackToNormalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        systemEngine.loadNormalData();
        int currTime = Timeline.getCurrTime();
        String jsonResponse = new Gson().toJson(currTime);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }

        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);

        System.out.println("request URI is: " + request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
