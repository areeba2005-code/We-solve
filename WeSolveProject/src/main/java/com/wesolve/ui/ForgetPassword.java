package com.wesolve.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.BasicStroke;

public class ForgetPassword extends JFrame {

    private static final Color BROWN = new Color(178, 147, 91); // #b2935b

    public ForgetPassword() {
        setTitle("We Solve – Forgot Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // ===================== LEFT PANEL – BROWN =====================
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BROWN);
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(new JLabel(""));

        // ===================== RIGHT PANEL – WHITE =====================
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(80, 60, 60, 60));

        // --- Forgot Password Title ---
        JLabel lblTitle = new JLabel("Forgot Password");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 36));
        lblTitle.setForeground(BROWN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Subtitle ---
        JLabel lblSubtitle = new JLabel("Recover Your Account Password");
        lblSubtitle.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblSubtitle.setForeground(Color.DARK_GRAY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Email Label ---
        JLabel lblEmail = createLabel("Enter Email *");
        JTextField txtEmail = new PlaceholderTextField("Customer.info@gmail.com", 30);
        txtEmail.setMaximumSize(new Dimension(340, 50));

        // --- Continue Button ---
        RoundedButton btnContinue = new RoundedButton("Continue", 30);
        btnContinue.setBackground(BROWN);
        btnContinue.setForeground(Color.WHITE);
        btnContinue.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnContinue.setMaximumSize(new Dimension(280, 50));

        // --- Add to content ---
        addCentered(content, lblTitle, 0);
        addCentered(content, (JComponent) Box.createVerticalStrut(15), 0);
        addCentered(content, lblSubtitle, 30);
        addCentered(content, lblEmail, 20);
        addCentered(content, txtEmail, 20);
        addCentered(content, (JComponent) Box.createVerticalStrut(40), 0);
        addCentered(content, btnContinue, 0);

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.gridwidth = GridBagConstraints.REMAINDER;
        rightGbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(content, rightGbc);

        add(leftPanel);
        add(rightPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void addCentered(JPanel panel, JComponent comp, int topMargin) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (topMargin > 0) panel.add(Box.createVerticalStrut(topMargin));
        panel.add(comp);
    }

    // ===================== FIXED BORDER TEXT FIELD =====================
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

    // ===================== ROUNDED BUTTON =====================
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

    // ===================== MAIN =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception ignored) {}
            new ForgetPassword().setVisible(true);
        });
    }
}