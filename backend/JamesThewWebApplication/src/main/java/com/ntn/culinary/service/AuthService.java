package com.ntn.culinary.service;

import com.ntn.culinary.dao.UserDao;
import com.ntn.culinary.exception.BadRequestException;
import com.ntn.culinary.exception.ForbiddenException;
import com.ntn.culinary.exception.NotFoundException;
import com.ntn.culinary.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserDao userDao;
    private final JwtService jwtService;

    // service giao tiếp với service là bình thường.
    // Miễn là:
    // Trách nhiệm rõ ràng.
    // Không tạo dependency vòng tròn.
    // Không để service "chồng chất" logic không liên quan.

    public AuthService(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public String authenticate(String username, String password) {
        User user = userDao.findUserByUsername(username);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!user.isActive()) {
            throw new ForbiddenException("User is inactive");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
        return jwtService.generateJwt(user);
    }
}