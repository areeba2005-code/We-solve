package com.wesolve.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/wesolve_db";
    private static final String USER = "root"; // XAMPP default
    private static final String PASSWORD = ""; // agar password nahi lagaya

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
