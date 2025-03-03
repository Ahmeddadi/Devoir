package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modeles.Departement;
import utils.Database;


public class DepartementServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        StringBuilder json = new StringBuilder("[");

        for (Departement d : Database.departements.values()) {
            json.append(String.format("{\"id\": %d, \"nom\": \"%s\"},", d.getId(), d.getNom()));
        }

        if (json.length() > 1) json.deleteCharAt(json.length() - 1);
        json.append("]");
        
        response.getWriter().write(json.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nom = request.getParameter("nom");

        if (nom == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Le nom du département est obligatoire.");
            return;
        }

        int id = Database.generateDepartementId();
        Departement departement = new Departement(id, nom);
        Database.departements.put(id, departement);

        response.getWriter().write("Département ajouté avec succès. ID: " + id);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");

        if (idStr == null || nom == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID et nom sont obligatoires.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID invalide.");
            return;
        }

        Departement departement = Database.departements.get(id);
        if (departement == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Département non trouvé.");
            return;
        }

        departement = new Departement(id, nom);
        Database.departements.put(id, departement);
        response.getWriter().write("Département mis à jour.");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");

        if (idStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID requis.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID invalide.");
            return;
        }

        if (Database.departements.remove(id) == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Département non trouvé.");
        } else {
            response.getWriter().write("Département supprimé avec succès.");
        }
    }
}
