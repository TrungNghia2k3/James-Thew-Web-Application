package com.service;

import com.dao.UserDAO;
import com.model.Permission;
import com.model.Role;
import com.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("SECRET_KEY");
    private final UserDAO userDao = new UserDAO();

    @Test
    public void testGenerateJwt() throws SQLException {

        User user = userDao.findUserByUsername("admin_user");

        String token = authServiceTestGenerateJwt(user); // gọi tạm hàm test

        // ✅ In token ra màn hình
        System.out.println("Generated JWT: " + token);

        assertNotNull(token);

        // Verify token
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();

        // ✅ In thông tin bên trong token
        System.out.println("Subject (username): " + claims.getSubject());
        System.out.println("User ID: " + claims.get("user_id"));
        System.out.println("Roles: " + claims.get("roles"));
        System.out.println("Permissions: " + claims.get("permissions"));
    }

    // tách phần generateJwt vì generateJwt là private
    private String authServiceTestGenerateJwt(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        List<String> permissionNames = user.getPermissions().stream()
                .map(Permission::getName)
                .toList();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_id", user.getId())
                .claim("roles", roleNames)
                .claim("permissions", permissionNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }
}
