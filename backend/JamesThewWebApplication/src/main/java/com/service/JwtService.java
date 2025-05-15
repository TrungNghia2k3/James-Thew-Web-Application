package com.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;

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

    public boolean hasRequiredRole(HttpServletRequest request, String role) {
        String uri = request.getRequestURI();
        String requiredRole = getRequiredRole(uri);
        return requiredRole == null || role.equals(requiredRole);
    }

    private String getRequiredRole(String uri) {
        if (uri.startsWith("/api/protected/admin")) {
            return "admin";
        } else if (uri.startsWith("/api/protected/member")) {
            return "member";
        }
        return null;
    }
}