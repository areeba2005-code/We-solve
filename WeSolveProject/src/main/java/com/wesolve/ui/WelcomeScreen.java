// src/com/wesolve/ui/WelcomeScreen.java
package com.wesolve.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class WelcomeScreen extends JFrame {

    private static final int CORNER_RADIUS = 40;

    public WelcomeScreen() {
        setTitle("We Solve - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // LEFT PANEL – ROUNDED IMAGE
        JPanel leftPanel = new JPanel() {
            private final Image backgroundImage = new ImageIcon("src/main/java/resources/images/img 1.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                RoundRectangle2D clip = new RoundRectangle2D.Float(0, 0, w, h, CORNER_RADIUS, CORNER_RADIUS);
                g2.setClip(clip);

                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, w, h, this);
                } else {
                    g2.setColor(new Color(200, 200, 200));
                    g2.fillRoundRect(0, 0, w, h, CORNER_RADIUS, CORNER_RADIUS);
                }

                g2.setClip(null);
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(1));
                g2.draw(clip);
                g2.dispose();
            }
        };
        leftPanel.setLayout(null);

        // RIGHT PANEL – SOLID COLOR
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.decode("#b2935b"));
        rightPanel.setLayout(new GridBagLayout());

        // CONTENT PANEL
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        // Title
        JLabel title = new JLabel("<html><div style='text-align: center; color: white;'>Welcome To We Solve</div></html>");
        title.setFont(new Font("Times New Roman", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel(
            "<html><div style='text-align: center; color: #EEEEEE;'>Cleaner spaces, healthier communities —<br>because every problem deserves a shared solution.</div></html>");
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Get Started Button
        RoundedButton startButton = new RoundedButton("Get Started", 30);
        startButton.setBackground(new Color(122, 78, 45));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // FIXED ACTION LISTENER
        startButton.addActionListener(e -> {
            // Safely dispose current frame
            Window window = SwingUtilities.getWindowAncestor(startButton);
            if (window != null) {
                window.dispose();
            }

            // Open LoginScreen on EDT
            SwingUtilities.invokeLater(() -> {
                try {
                    new LoginScreen().setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error opening Login Screen!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        // Assemble
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(subtitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(startButton);
        contentPanel.add(Box.createVerticalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(contentPanel, gbc);

        // SPLIT PANE
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(0);
        splitPane.setBorder(null);
        splitPane.setEnabled(false);

        add(splitPane, BorderLayout.CENTER);
    }

    // CUSTOM ROUNDED BUTTON
    private static class RoundedButton extends JButton {
        private final int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
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

        @Override
        public boolean contains(int x, int y) {
            return new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius).contains(x, y);
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception ignored) {}
            new WelcomeScreen().setVisible(true);
        });
    }
}