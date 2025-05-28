package com.service;

import com.dao.UserDAO;
import com.model.Permission;
import com.model.Role;
import com.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AuthService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("SECRET_KEY");
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    private final UserDAO userDao;

    public AuthService() {
        this.userDao = new UserDAO();
    }

    public String authenticate(String username, String password) throws Exception {
        User user = userDao.findUserByUsername(username);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!user.isActive()) {
            throw new Exception("User is inactive");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new Exception("Invalid password");
        }

        return generateJwt(user);
    }

    private String generateJwt(User user) {
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
}