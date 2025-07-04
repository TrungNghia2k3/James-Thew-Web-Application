package com.ntn.culinary.service;

import com.ntn.culinary.model.Permission;
import com.ntn.culinary.model.Role;
import com.ntn.culinary.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("SECRET_KEY");
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    private static final JwtService jwtService = new JwtService();

    private JwtService() {
        // Private constructor to prevent instantiation
    }

    public static JwtService getInstance() {
        return jwtService;
    }

    public String generateJwt(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        List<String> permissionNames = user.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_id", user.getId())
                .claim("roles", roleNames)
                .claim("permissions", permissionNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

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
        if (uri.startsWith("/api/protected/admin/**")) {
            return "admin";
        } else if (uri.startsWith("/api/protected/staff")) {
            return "staff";
        } else if (uri.startsWith("/api/protected/writer")) {
            return "writer";
        } else if (uri.startsWith("/api/protected/subscriber")) {
            return "subscriber";
        } else if (uri.startsWith("/api/protected/general")) {
            return "general";
        }
        return null;
    }
}
