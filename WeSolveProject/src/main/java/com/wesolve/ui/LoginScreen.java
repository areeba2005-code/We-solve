// src/com/wesolve/ui/LoginScreen.java
package com.wesolve.ui;

import com.wesolve.util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginScreen extends JFrame {

    private static final Color BROWN = new Color(178, 147, 91);
    private static final Color LIGHT_BROWN = new Color(205, 133, 63);
    private JTextField txtEmail;
    private JPasswordField txtPass;

    public LoginScreen() {
        setTitle("We Solve – Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // LEFT PANEL – Image or Color
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BROWN);
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(new JLabel("")); // Placeholder

        // RIGHT PANEL – Login Form
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(80, 60, 80, 60));

        // Title
        JLabel lblTitle = new JLabel("Welcome Back!");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 36));
        lblTitle.setForeground(BROWN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        txtEmail.setMaximumSize(new Dimension(340, 50));

        // Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        txtPass = new JPasswordField(20);
        txtPass.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        txtPass.setMaximumSize(new Dimension(340, 50));

        // Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(BROWN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnLogin.setMaximumSize(new Dimension(340, 50));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Sign Up Link
        JLabel lblSignUp = new JLabel("<html><u>Don't have an account? Sign Up</u></html>");
        lblSignUp.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblSignUp.setForeground(BROWN);
        lblSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action: Open SignUp
        lblSignUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new SignUp().setVisible(true);
                dispose();
            }
        });

        // Login Action
        btnLogin.addActionListener(e -> loginUser());

        // Add all
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(40));
        content.add(lblEmail);
        content.add(txtEmail);
        content.add(Box.createVerticalStrut(15));
        content.add(lblPass);
        content.add(txtPass);
        content.add(Box.createVerticalStrut(30));
        content.add(btnLogin);
        content.add(Box.createVerticalStrut(20));
        content.add(lblSignUp);
        content.add(Box.createVerticalGlue());

        rightPanel.add(content, new GridBagConstraints());
        add(leftPanel);
        add(rightPanel);

        setVisible(true);
    }

    private void loginUser() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPass.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT id, name FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String name = rs.getString("name");

                JOptionPane.showMessageDialog(this, "Welcome back, " + name + "!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // DASHBOARD KHOLO (userId, name ke saath)
                new DashboardScreen(userId, name).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MAIN (Optional – for testing)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}