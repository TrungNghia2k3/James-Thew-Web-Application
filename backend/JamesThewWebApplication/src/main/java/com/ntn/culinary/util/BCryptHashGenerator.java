package com.ntn.culinary.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptHashGenerator {
    public static void main(String[] args) {
        String adminPassword = "123456";
        String generalPassword = "staffuser2";

        String hashedAdmin = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String hashedGeneral = BCrypt.hashpw(generalPassword, BCrypt.gensalt());

        System.out.println("Hashed Admin Password: " + hashedAdmin);
        System.out.println("Hashed General Password: " + hashedGeneral);
    }
}

