package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modeles.Employe;
import modeles.Role;
import utils.Database;


public class AuthentificationServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
	        String email = request.getParameter("email");
	        String motDePasse = request.getParameter("motDePasse");

	        System.out.println("\n[LOGIN] Tentative de connexion avec : " + email);

	        if (email == null || motDePasse == null || email.trim().isEmpty() || motDePasse.trim().isEmpty()) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("Email et mot de passe requis.");
	            System.out.println("[LOGIN ERROR] Paramètres manquants ou vides.");
	            return;
	        }

	        email = email.trim();
	        motDePasse = motDePasse.trim();

	        for (Employe e : Database.employes.values()) {
	            if (e.getEmail().trim().equalsIgnoreCase(email) && e.getMotDePasse().trim().equals(motDePasse)) {
	                HttpSession session = request.getSession();
	                session.setAttribute("user", e);

	                // 🔹 Ajout de vérification du rôle et redirection selon l'autorisation
	                if (e.getRole() == Role.ADMIN) {
	                    response.getWriter().write("{\"message\": \"Connexion réussie en tant qu'Admin.\", \"role\": \"ADMIN\"}");
	                } else if (e.getRole() == Role.RESPONSABLE) {
	                    response.getWriter().write("{\"message\": \"Connexion réussie en tant que Responsable.\", \"role\": \"RESPONSABLE\"}");
	                } else {
	                    response.getWriter().write("{\"message\": \"Connexion réussie en tant qu'Employé.\", \"role\": \"EMPLOYE\"}");
	                }

	                System.out.println("[LOGIN SUCCESS] Utilisateur connecté : " + email + " (" + e.getRole() + ")");
	                return;
	            }
	        }

	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Utilisateur non trouvé ou mot de passe incorrect.");
	        System.out.println("[LOGIN ERROR] Aucun utilisateur trouvé avec cet email : " + email);

	    } catch (Exception e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("Erreur serveur.");
	        e.printStackTrace();
	    }
	}

}
