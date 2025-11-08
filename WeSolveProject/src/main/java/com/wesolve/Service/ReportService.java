// src/com/wesolve/service/ReportService.java
package com.wesolve.Service;

import com.wesolve.model.Report;
import com.wesolve.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private static ReportService instance;
    private static final String URL = "jdbc:mysql://localhost:3306/wesolve_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    private ReportService() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found!");
            e.printStackTrace();
        }
    }

    public static synchronized ReportService getInstance() {
        if (instance == null) instance = new ReportService();
        return instance;
    }

    public boolean saveReport(Report report) {
        String sql = "INSERT INTO complaints (user_id, title, description, image_path, location, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getUserId());
            ps.setString(2, report.getTitle());
            ps.setString(3, report.getDescription());
            ps.setString(4, report.getImagePath());
            ps.setString(5, report.getLocation());
            ps.setString(6, report.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Save failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Report> getAllReports() {
        List<Report> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints ORDER BY created_at DESC";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Report r = new Report();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setTitle(rs.getString("title"));
                r.setDescription(rs.getString("description"));
                r.setImagePath(rs.getString("image_path"));
                r.setLocation(rs.getString("location"));
                r.setStatus(rs.getString("status"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Report> getReportsByUserId(int userId) {
    List<Report> list = new ArrayList<>();
    String sql = "SELECT * FROM complaints WHERE user_id = ? ORDER BY created_at DESC";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Report r = new Report();
            r.setId(rs.getInt("id"));
            r.setUserId(rs.getInt("user_id"));
            r.setTitle(rs.getString("title"));
            r.setDescription(rs.getString("description"));
            r.setImagePath(rs.getString("image_path"));
            r.setLocation(rs.getString("location"));
            r.setStatus(rs.getString("status"));
            r.setCreatedAt(rs.getTimestamp("created_at"));
            list.add(r);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;

    }
    public boolean isDuplicate(String title, String location) {
    String sql = "SELECT COUNT(*) FROM complaints WHERE LOWER(title) = LOWER(?) AND LOWER(location) = LOWER(?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, title.trim());
        ps.setString(2, location.trim());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}