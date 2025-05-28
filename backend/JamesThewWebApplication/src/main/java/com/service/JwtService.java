package com.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class JwtService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("SECRET_KEY");



    public Claims validateJwt(String jwt) throws JwtException {
        assert SECRET_KEY != null;
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean hasRequiredRole(HttpServletRequest request, List<String> userRoles) {
        String requiredRole = getRequiredRole(request.getRequestURI());
        return requiredRole == null || userRoles.contains(requiredRole.toUpperCase());
    }

    public boolean hasPermission(Claims claims, String permission) {
        List<String> permissions = claims.get("permissions", List.class);
        return permissions != null && permissions.contains(permission);
    }

    private String getRequiredRole(String uri) {
        if (uri.startsWith("/api/protected/admin")) {
            return "admin";
        } else if (uri.startsWith("/api/protected/staff")) {
            return "staff";
        } else if (uri.startsWith("/api/protected/writer")) {
            return "writer";
        } else if (uri.startsWith("/api/protected/subscriber")) {
            return "subscriber";
        }
        return null;
    }
}
