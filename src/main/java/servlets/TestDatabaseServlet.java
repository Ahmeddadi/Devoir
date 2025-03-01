package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modeles.Employe;
import utils.Database;


public class TestDatabaseServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        StringBuilder json = new StringBuilder("[");
        for (Employe e : Database.employes.values()) {
            json.append(String.format(
                "{\"id\": %d, \"nom\": \"%s\", \"email\": \"%s\", \"motDePasse\": \"%s\", \"role\": \"%s\"},",
                e.getId(), e.getNom(), e.getEmail(), e.getMotDePasse(), e.getRole()
            ));
        }
        if (json.length() > 1) json.deleteCharAt(json.length() - 1);
        json.append("]");
        
        response.getWriter().write(json.toString());
    }
}
