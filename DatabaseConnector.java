package com.example.winejavasql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/wine";
    private static final String USER = "root";
    private static final String PASSWORD = "lf311";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to MySQL!");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}
