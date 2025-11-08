package com.wesolve;

import com.wesolve.ui.WelcomeScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
    }
}
