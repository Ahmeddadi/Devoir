package filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modeles.Employe;
import modeles.Role;
import utils.Database;


public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        System.out.println("[FILTER] Accès à l'URL : " + uri);

        // 🔹 Liste des URL accessibles SANS connexion (ajout de /test-database)
        List<String> publicUrls = Arrays.asList("/Devoir/login", "/Devoir/logout", "/Devoir/test-database");

        if (publicUrls.contains(uri)) {
            System.out.println("[FILTER] URL publique, accès autorisé.");
            chain.doFilter(request, response);
            return;
        }

        // Vérifier si l'utilisateur est connecté
        Employe user = (Employe) req.getSession().getAttribute("user");

        if (user == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Accès refusé. Connectez-vous.");
            System.out.println("[FILTER ERROR] Aucun utilisateur connecté.");
            return;
        }
        if (user.getRole() == Role.EMPLOYE) {
            // 🔹 Bloquer l'accès aux fonctionnalités d'administration
            List<String> adminUrls = Arrays.asList("/Devoir/employes", "/Devoir/departements", "/Devoir/evaluations");

            for (String url : adminUrls) {
                if (uri.startsWith(url)) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().write("Accès refusé. Seuls les administrateurs et responsables peuvent accéder à cette ressource.");
                    System.out.println("[FILTER ERROR] Accès interdit pour un employé à l'URL : " + uri);
                    return;
                }
            }
        }
        if (user.getRole() == Role.RESPONSABLE) {
            // 🔹 Vérifier si le responsable essaie d'accéder aux employés
            if (uri.startsWith("/Devoir/employes")) {
                String employeIdStr = req.getParameter("id");

                if (employeIdStr != null) {
                    try {
                        int employeId = Integer.parseInt(employeIdStr);
                        Employe employe = Database.employes.get(employeId);

                        if (employe == null || employe.getDepartement().getId() != user.getDepartement().getId()) {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.getWriter().write("Accès refusé. Vous ne pouvez voir que les employés de votre département.");
                            System.out.println("[FILTER ERROR] Accès interdit pour un responsable à l'employé ID : " + employeId);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        res.getWriter().write("ID invalide.");
                        return;
                    }
                }
            }
        }

     // 🔹 Empêcher l'ADMIN d'accéder aux évaluations
        if (user.getRole() == Role.ADMIN) {
            List<String> evaluationUrls = Arrays.asList("/Devoir/evaluations");

            for (String url : evaluationUrls) {
                if (uri.startsWith(url)) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().write("Accès refusé. Seuls les responsables peuvent gérer les évaluations.");
                    System.out.println("[FILTER ERROR] Accès interdit pour un administrateur à l'URL : " + uri);
                    return;
                }
            }
        }




        System.out.println("[FILTER SUCCESS] Utilisateur authentifié : " + user.getEmail() + " (" + user.getRole() + ")");

        chain.doFilter(request, response);
    }
}