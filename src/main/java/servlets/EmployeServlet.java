package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modeles.Departement;
import modeles.Employe;
import modeles.Role;
import utils.Database;
import java.io.IOException;
import java.util.stream.Collectors;



public class EmployeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        System.out.println("[GET /employes] Requête reçue");

        // 🔹 Retourne la liste des employés sous format JSON
        String json = Database.employes.values().stream()
                .map(e -> String.format(
                        "{\"id\": %d, \"nom\": \"%s\", \"prenom\": \"%s\", \"email\": \"%s\", \"role\": \"%s\"}",
                        e.getId(), e.getNom(), e.getPrenom(), e.getEmail(), e.getRole()))
                .collect(Collectors.joining(",", "[", "]"));

        response.getWriter().write(json);
        System.out.println("[SUCCESS] Employés envoyés.");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr=request.getParameter("id");
        String adresse =request.getParameter("adresse");
        String telephone = request.getParameter("telephone");

        System.out.println("\n[PUT /employes] Requête reçue");
        System.out.println("[DEBUG] id: " + idStr + ", adresse: " + adresse + ", telephone: " + telephone);

        if (idStr == null || adresse == null || telephone == null || idStr.trim().isEmpty() || adresse.trim().isEmpty() || telephone.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID, adresse et téléphone sont obligatoires.\"}");
            System.out.println("[ERROR] Paramètres manquants ou vides.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID invalide.\"}");
            System.out.println("[ERROR] ID invalide.");
            return;
        }

        Employe employe = Database.employes.get(id);
        if (employe == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Employé non trouvé.\"}");
            System.out.println("[ERROR] Employé non trouvé.");
            return;
        }

        // Mise à jour des informations de l'employé
        employe.setAdresse(adresse.trim());
        employe.setTelephone(telephone.trim());

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"success\": \"Employé mis à jour avec succès.\"}");
        System.out.println("[SUCCESS] Employé " + id + " mis à jour.");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Récupération des paramètres
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String numSecu = request.getParameter("numSecu");
            String adresse = request.getParameter("adresse");
            String telephone = request.getParameter("telephone");
            String email = request.getParameter("email");
            String motDePasse = request.getParameter("motDePasse");
            String departementIdStr = request.getParameter("departementId");
            String roleStr = request.getParameter("role");

            System.out.println("\n[EMPLOYE] Tentative d'ajout d'un employé : " + nom + " " + prenom);

            // Vérification des champs obligatoires
            if (nom == null || prenom == null || numSecu == null || adresse == null || telephone == null ||
                email == null || motDePasse == null || departementIdStr == null || roleStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Tous les champs sont obligatoires.");
                System.out.println("[EMPLOYE ERROR] Informations manquantes.");
                return;
            }
            

            // Vérification et conversion du département ID
            int departementId;
            try {
                departementId = Integer.parseInt(departementIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("ID du département invalide.");
                System.out.println("[EMPLOYE ERROR] ID du département non valide.");
                return;
            }

            // Vérification de l'existence du département
            Departement departement = Database.departements.get(departementId);
            if (departement == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Le département n'existe pas.");
                System.out.println("[EMPLOYE ERROR] Département introuvable.");
                return;
            }

            // Vérification du rôle
            Role role;
            try {
                role = Role.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Rôle invalide. Utilisez ADMIN, RESPONSABLE ou EMPLOYE.");
                System.out.println("[EMPLOYE ERROR] Rôle invalide.");
                return;
            }

            // Génération d'un ID unique
            int id = Database.generateEmployeId();

            // Création du nouvel employé
            Employe nouvelEmploye = new Employe(id, nom, prenom, numSecu, adresse, telephone, email, motDePasse, departement, role);

            // Ajout à la base de données
            Database.employes.put(id, nouvelEmploye);

            // Réponse de succès
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("Employé ajouté avec succès. ID: " + id);
            System.out.println("[EMPLOYE SUCCESS] Employé ajouté avec ID : " + id);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erreur serveur.");
            e.printStackTrace();
        }
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Récupération de l'ID de l'employé à supprimer
            String idStr = request.getParameter("id");

            System.out.println("\n[EMPLOYE] Tentative de suppression de l'employé ID : " + idStr);

            // Vérification si l'ID est fourni
            if (idStr == null || idStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("L'ID de l'employé est obligatoire.");
                System.out.println("[EMPLOYE ERROR] ID manquant.");
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("ID invalide.");
                System.out.println("[EMPLOYE ERROR] Format d'ID invalide.");
                return;
            }

            // Vérification si l'employé existe
            if (!Database.employes.containsKey(id)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Employé non trouvé.");
                System.out.println("[EMPLOYE ERROR] Employé introuvable.");
                return;
            }

            // Suppression de l'employé
            Database.employes.remove(id);

            // Réponse de succès
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Employé supprimé avec succès.");
            System.out.println("[EMPLOYE SUCCESS] Employé supprimé : ID " + id);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erreur serveur.");
            e.printStackTrace();
        }
    }

}
