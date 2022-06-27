package servlets.client.common;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import time.Timeline;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "GetCurrYazServlet", urlPatterns = "/GetCurrYaz")
public class GetCurrYazServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            int yaz = Timeline.getCurrTime();
            String jsonResponse = new Gson().toJson(yaz);
            out.print(jsonResponse);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}

