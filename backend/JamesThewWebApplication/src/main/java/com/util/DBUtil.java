package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/demo_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "root";

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
