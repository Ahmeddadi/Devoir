package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modeles.Employe;
import modeles.Evaluation;
import utils.Database;


public class EvaluationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeIdStr = request.getParameter("employeId");

        if (employeIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID de l'employé requis.");
            return;
        }

        int employeId;
        try {
            employeId = Integer.parseInt(employeIdStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID invalide.");
            return;
        }

        response.setContentType("application/json");
        StringBuilder json = new StringBuilder("[");
        
        for (Evaluation e : Database.evaluations) {
            if (e.getEmployeId() == employeId) {
                json.append(String.format("{\"commentaire\": \"%s\", \"note\": %d},", e.getCommentaire(), e.getNote()));
            }
        }

        if (json.length() > 1) json.deleteCharAt(json.length() - 1);
        json.append("]");

        response.getWriter().write(json.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeIdStr = request.getParameter("employeId");
        String commentaire = request.getParameter("commentaire");
        String noteStr = request.getParameter("note");

        if (employeIdStr == null || commentaire == null || noteStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Tous les champs sont obligatoires.");
            return;
        }

        int employeId, note;
        try {
            employeId = Integer.parseInt(employeIdStr);
            note = Integer.parseInt(noteStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID ou note invalide.");
            return;
        }

        Employe employe = Database.employes.get(employeId);
        if (employe == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Employé non trouvé.");
            return;
        }

        Evaluation evaluation = new Evaluation(employeId, commentaire, note);
        Database.evaluations.add(evaluation);

        response.getWriter().write("Évaluation ajoutée avec succès.");
    }
}
