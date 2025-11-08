// src/com/wesolve/ui/SignUp.java
package com.wesolve.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUp extends JFrame {

    private static final Color BROWN = new Color(178, 147, 91);

    // Fields class level pe
    private JTextField txtName, txtEmail, txtBirthday;
    private JPasswordField txtPass;

    public SignUp() {
        setTitle("We Solve Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BROWN);
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(new JLabel(""));

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JLabel lblTitle = new JLabel("Sign Up");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 36));
        lblTitle.setForeground(BROWN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields with Placeholder
        JLabel lblName = createLabel("Full Name *");
        txtName = new PlaceholderTextField("Maria Ali", 30);
        txtName.setMaximumSize(new Dimension(340, 50));

        JLabel lblEmail = createLabel("Enter Email *");
        txtEmail = new PlaceholderTextField("Customer.info@gmail.com", 30);
        txtEmail.setMaximumSize(new Dimension(340, 50));

        JLabel lblPass = createLabel("Enter Password *");
        txtPass = new PlaceholderPasswordField("Enter your password", 30);
        txtPass.setMaximumSize(new Dimension(340, 50));

        JLabel lblBirthday = createLabel("Birthday");
        txtBirthday = new PlaceholderTextField("21/04/2005", 30);
        txtBirthday.setMaximumSize(new Dimension(340, 50));

        RoundedButton btnCreate = new RoundedButton("Create Account", 30);
        btnCreate.setBackground(BROWN);
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnCreate.setMaximumSize(new Dimension(280, 50));

        btnCreate.addActionListener(e -> signUpUser());

        // Add all components
        addCentered(content, lblTitle, 0);
        addCentered(content, Box.createVerticalStrut(30), 0);
        addCentered(content, lblName, 10);
        addCentered(content, txtName, 20);
        addCentered(content, lblEmail, 10);
        addCentered(content, txtEmail, 20);
        addCentered(content, lblPass, 10);
        addCentered(content, txtPass, 20);
        addCentered(content, lblBirthday, 10);
        addCentered(content, txtBirthday, 20);
        addCentered(content, Box.createVerticalStrut(30), 0);
        addCentered(content, btnCreate, 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(content, gbc);

        add(leftPanel);
        add(rightPanel);
    }

    // FIXED: createLabel
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // FIXED: addCentered (Overloaded)
    private void addCentered(JPanel panel, JComponent comp, int topMargin) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (topMargin > 0) panel.add(Box.createVerticalStrut(topMargin));
        panel.add(comp);
    }

    private void addCentered(JPanel panel, Component comp, int topMargin) {
        comp.setMinimumSize(new Dimension(0, 0)); // Allow strut
        addCentered(panel, (JComponent) comp, topMargin);
    }

    // Database Save
    private void signUpUser() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPass.getPassword()).trim();

        if (name.equals("Maria Ali") || email.equals("Customer.info@gmail.com") || 
            password.equals("Enter your password") || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'user')";
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/wesolve_db?useSSL=false&serverTimezone=UTC", "root", "");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginScreen().setVisible(true);
                dispose();
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ================== PlaceholderTextField ==================
    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;
        private final int radius;

        public PlaceholderTextField(String placeholder, int radius) {
            super(placeholder);
            this.placeholder = placeholder;
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
            setFont(new Font("Times New Roman", Font.PLAIN, 15));
            setForeground(Color.GRAY);

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override public void focusGained(java.awt.event.FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }
                @Override public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().trim().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.setColor(BROWN);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    // ================== PlaceholderPasswordField ==================
    private static class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;
        private final int radius;
        private boolean isPlaceholder = true;

        public PlaceholderPasswordField(String placeholder, int radius) {
            super(placeholder);
            this.placeholder = placeholder;
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
            setFont(new Font("Times New Roman", Font.PLAIN, 15));
            setForeground(Color.GRAY);
            setEchoChar((char) 0);

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override public void focusGained(java.awt.event.FocusEvent e) {
                    if (isPlaceholder) {
                        setText("");
                        setEchoChar('•');
                        setForeground(Color.BLACK);
                        isPlaceholder = false;
                    }
                }
                @Override public void focusLost(java.awt.event.FocusEvent e) {
                    if (String.valueOf(getPassword()).trim().isEmpty()) {
                        setText(placeholder);
                        setEchoChar((char) 0);
                        setForeground(Color.GRAY);
                        isPlaceholder = true;
                    }
                }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.setColor(BROWN);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    // ================== RoundedButton ==================
    private static class RoundedButton extends JButton {
        private final int radius;
        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(new Font("Times New Roman", Font.BOLD, 16));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = getModel().isArmed() ? getBackground().darker()
                     : getModel().isRollover() ? getBackground().brighter()
                     : getBackground();
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
        @Override public boolean contains(int x, int y) {
            return new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius).contains(x, y);
        }
    }

    // ================== MAIN ==================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }
}