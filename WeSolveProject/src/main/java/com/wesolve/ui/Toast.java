// src/main/java/com/wesolve/ui/Toast.java
package com.wesolve.ui;

import javax.swing.*;
import java.awt.*;

class Toast {
    private static JWindow window;
    private static JLabel label;

    public static void init(JFrame parent) {
        window = new JWindow(parent);
        label = new JLabel();
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, 18));
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        panel.add(label);
        window.add(panel);
        window.pack();
    }

    private static void show(String msg, Color bg) {
        label.setText(msg);
        label.setBackground(bg);
        label.setForeground(Color.WHITE);
        window.pack();
        window.setLocationRelativeTo(window.getOwner());
        window.setLocation(window.getX(), window.getY() - 100);
        window.setVisible(true);
        new Timer(3500, e -> window.setVisible(false)).start();
    }

    public static void success(String msg) { show("✓ " + msg, new Color(34, 197, 94)); }
    public static void error(String msg)   { show("✗ " + msg, new Color(220, 53, 69)); }
}