package com.ntn.culinary.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String JDBC_URL = dotenv.get("DB_URL");
    private static final String JDBC_USER = dotenv.get("DB_USERNAME");
    private static final String JDBC_PASS = dotenv.get("DB_PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Nạp driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load driver JDBC MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }
}
