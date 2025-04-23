package com.validator;

import com.dto.request.UserRequest;
import com.exception.ValidationException;

import static java.lang.Double.parseDouble;

public class UserValidator {
    public void validate(UserRequest user) throws ValidationException {
        if (user.getName() == null || user.getName().isEmpty() || isNumeric(user.getName())) {
            throw new ValidationException("Invalid or missing name");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Invalid or missing email");
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
