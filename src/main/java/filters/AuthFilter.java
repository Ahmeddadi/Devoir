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


public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        System.out.println("[FILTER] Accès à l'URL : " + uri);

        // 🔹 Liste des URL accessibles SANS connexion (ajout de /test-database)
        List<String> publicUrls = Arrays.asList("/projetGrh/login", "/projetGrh/logout", "/projetGrh/test-database");

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

        System.out.println("[FILTER SUCCESS] Utilisateur authentifié : " + user.getEmail() + " (" + user.getRole() + ")");

        chain.doFilter(request, response);
    }
}
