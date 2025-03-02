package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modeles.Employe;
import utils.Database;


public class AuthentificationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String motDePasse = request.getParameter("motDePasse");

            System.out.println("\n[LOGIN] Tentative de connexion avec : " + email);

            // 🔹 Vérifier si les valeurs sont nulles ou contiennent des espaces invisibles
            if (email == null || motDePasse == null || email.trim().isEmpty() || motDePasse.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Email et mot de passe requis.");
                System.out.println("[LOGIN ERROR] Paramètres manquants ou vides.");
                return;
            }

            email = email.trim(); // Supprime les espaces autour
            motDePasse = motDePasse.trim();

            // 🔹 Affichage des utilisateurs en mémoire
            System.out.println("[LOGIN DEBUG] Liste des utilisateurs enregistrés :");
            for (Employe e : Database.employes.values()) {
                System.out.println("  - Email: " + e.getEmail() + " | Mot de passe: " + e.getMotDePasse() + " | Rôle: " + e.getRole());
            }

            // 🔹 Vérification des utilisateurs
            for (Employe e : Database.employes.values()) {
                if (e.getEmail().trim().equalsIgnoreCase(email)) { // Comparaison insensible à la casse
                    if (e.getMotDePasse().trim().equals(motDePasse)) { // Comparaison stricte du mot de passe
                        HttpSession session = request.getSession();
                        session.setAttribute("user", e);
                        response.getWriter().write("Connexion réussie !");
                        System.out.println("[LOGIN SUCCESS] Utilisateur connecté : " + email);
                        return;
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Mot de passe incorrect.");
                        System.out.println("[LOGIN ERROR] Mot de passe incorrect pour : " + email);
                        return;
                    }
                }
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Utilisateur non trouvé.");
            System.out.println("[LOGIN ERROR] Aucun utilisateur trouvé avec cet email : " + email);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erreur serveur.");
            e.printStackTrace();
        }
    }
}
