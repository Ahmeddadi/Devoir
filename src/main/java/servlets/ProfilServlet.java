package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modeles.Employe;


public class ProfilServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Employe user = (Employe) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Veuillez vous connecter.");
            return;
        }

        response.setContentType("application/json");
        response.getWriter().write(String.format(
                "{\"nom\": \"%s\", \"prenom\": \"%s\", \"email\": \"%s\", \"adresse\": \"%s\", \"telephone\": \"%s\"}",
                user.getNom(), user.getPrenom(), user.getEmail(), user.getAdresse(), user.getTelephone()
        ));
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Employe user = (Employe) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Veuillez vous connecter.");
            return;
        }

        String adresse = request.getParameter("adresse");
        String telephone = request.getParameter("telephone");

        if (adresse != null) user.setAdresse(adresse);
        if (telephone != null) user.setTelephone(telephone);

        response.getWriter().write("Informations mises à jour.");
    }
}
