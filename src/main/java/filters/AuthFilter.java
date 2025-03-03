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
            // 🔹 Bloquer l'accès aux fonctionnalités réservées à l'ADMIN
            List<String> adminOnlyUrls = Arrays.asList("/Devoir/employes", "/Devoir/departements");

            for (String url : adminOnlyUrls) {
                if (uri.startsWith(url)) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().write("Accès refusé. Seuls les administrateurs peuvent accéder à cette ressource.");
                    System.out.println("[FILTER ERROR] Accès interdit pour un responsable à l'URL : " + uri);
                    return;
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
