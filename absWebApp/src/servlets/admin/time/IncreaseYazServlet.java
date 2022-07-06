package servlets.admin.time;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import time.Timeline;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "increaseYazServlet", urlPatterns = "/increaseYaz")
public class IncreaseYazServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());
        systemEngine.increaseYaz();
        String jsonResponse = new Gson().toJson(Timeline.getCurrTime());

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}

