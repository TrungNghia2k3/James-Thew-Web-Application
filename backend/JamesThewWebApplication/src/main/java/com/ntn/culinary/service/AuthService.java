package com.ntn.culinary.service;

import com.ntn.culinary.dao.UserDAO;
import com.ntn.culinary.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private static final AuthService authService = new AuthService();

    private AuthService() {
        // Private constructor to prevent instantiation
    }

    public static AuthService getInstance() {
        return authService;
    }

    private final UserDAO userDao = UserDAO.getInstance();
    private final JwtService jwtService = JwtService.getInstance();

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

        return jwtService.generateJwt(user);
    }


}