package servlets.client.newLoan;

import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "CreateNewLoanFromFileServlet", urlPatterns = "/CreateNewLoanFromFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CreateNewLoanFromFileServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        Engine systemEngine = ServletUtils.getSystemEngine(getServletContext());

        Collection<Part> parts = request.getParts();

        //out.println("Total parts : " + parts.size());

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            //to write the content of the file to a string
            fileContent.append(readFromInputStream(part.getInputStream()));
        }
        InputStream file = new ByteArrayInputStream(fileContent.toString().getBytes(StandardCharsets.UTF_8));


        try {
            systemEngine.createNewLoanFromInputStream(file,usernameFromSession);
        }catch (JAXBException e){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getOutputStream().print(e.getMessage());
            return;
        }catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getOutputStream().print(e.getMessage());
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().print(e.getMessage());
            return;
        }
        response.setStatus(200);

    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}
