package com.service;

import com.dao.UserDao;
import com.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;

public class AuthService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("SECRET_KEY");
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public String authenticate(String username, String password) throws Exception {
        User user = userDao.findUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return generateJwt(user);
        }
        return null;
    }

    private String generateJwt(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_id", user.getUserId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }
}