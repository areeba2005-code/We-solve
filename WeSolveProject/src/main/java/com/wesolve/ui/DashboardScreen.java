// src/com/wesolve/ui/DashboardScreen.java
package com.wesolve.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardScreen extends JFrame {

    private static final Color BROWN = new Color(178, 147, 91);
    private static final Color LIGHT_BROWN = new Color(245, 220, 180);
    private static final Color HEADER_BROWN = new Color(160, 130, 80);
    private static final Color TEXT_DARK = new Color(51, 51, 51);
    private static final Font FONT_TITLE = new Font("Times New Roman", Font.BOLD, 22);
    private static final Font FONT_SUB = new Font("Times New Roman", Font.PLAIN, 14);
    private static final Font FONT_BODY = new Font("Times New Roman", Font.PLAIN, 13);
    private static final Border BROWN_BORDER = BorderFactory.createLineBorder(BROWN, 1);
    private static final Border HOVER_BORDER = BorderFactory.createLineBorder(BROWN, 2);

    // USER INFO
    private final int currentUserId;
    private final String currentUserName;

    // CONSTRUCTOR WITH USER INFO
    public DashboardScreen(int userId, String userName) {
        this.currentUserId = userId;
        this.currentUserName = userName;

        setTitle("Dashboard – Welcome Back, " + userName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        initUI();
        setVisible(true);
    }

    // Initialize UI
    private void initUI() {
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setDividerLocation(250);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);
    }

    // Left side menu
    private JPanel createLeftPanel() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BROWN);
        left.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));

        JLabel menuLabel = new JLabel("Menu");
        menuLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(menuLabel);
        left.add(Box.createVerticalStrut(30));

        String[] menuItems = {"Dashboard", "Campaigns", "Reports", "Analytics", "Team", "Settings"};
        for (String item : menuItems) {
            JLabel label = new JLabel(item);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Times New Roman", Font.PLAIN, 16));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            label.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { label.setForeground(new Color(255, 230, 180)); }
                public void mouseExited(MouseEvent e) { label.setForeground(Color.WHITE); }
            });
            left.add(label);
            left.add(Box.createVerticalStrut(5));
        }

        left.add(Box.createVerticalGlue());

        // LOGOUT BUTTON
        JLabel logout = new JLabel("Logout");
        logout.setForeground(Color.WHITE);
        logout.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        logout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(DashboardScreen.this,
                        "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new LoginScreen().setVisible(true);
                    dispose();
                }
            }
            public void mouseEntered(MouseEvent e) { logout.setForeground(Color.PINK); }
            public void mouseExited(MouseEvent e) { logout.setForeground(Color.WHITE); }
        });
        left.add(logout);

        return left;
    }

    // Right side content area
    private JPanel createRightPanel() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        searchPanel.setBackground(Color.WHITE);
        JTextField search = new RoundedSearchField();
        search.setText("Search reports, campaigns...");
        search.setFont(FONT_BODY);
        search.setPreferredSize(new Dimension(400, 44));
        searchPanel.add(search);
        right.add(searchPanel, BorderLayout.NORTH);

        // Center content
        JPanel mainContent = createMainContent();
        right.add(mainContent, BorderLayout.CENTER);

        // Report Problem button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setBackground(Color.WHITE);
        RoundedButton btnReport = new RoundedButton("Report Problem", 25);
        btnReport.setBackground(BROWN);
        btnReport.setForeground(Color.WHITE);
        btnReport.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnReport.setPreferredSize(new Dimension(200, 50));

        // OPEN COMPLAINT PORTAL WITH USER INFO
        btnReport.addActionListener(e -> {
            new ComplaintPortal(currentUserId, currentUserName).setVisible(true);
            // dispose(); // Optional: close dashboard
        });

        bottomPanel.add(btnReport);
        right.add(bottomPanel, BorderLayout.SOUTH);

        return right;
    }

    // Dashboard content
    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel pageTitle = new JLabel("Dashboard Overview");
        pageTitle.setFont(FONT_TITLE);
        pageTitle.setForeground(TEXT_DARK);
        JLabel breadcrumb = new JLabel("Home > Dashboard");
        breadcrumb.setFont(FONT_SUB);
        breadcrumb.setForeground(Color.GRAY);
        header.add(pageTitle, BorderLayout.WEST);
        header.add(breadcrumb, BorderLayout.EAST);

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        statsGrid.add(createHoverCard(createStatCard("Total Reports", "142", BROWN)));
        statsGrid.add(createHoverCard(createStatCard("Active Campaigns", "8", new Color(46, 125, 50))));
        statsGrid.add(createHoverCard(createStatCard("Volunteers", "89", new Color(30, 136, 229))));
        statsGrid.add(createHoverCard(createStatCard("Cleanups Done", "67", new Color(123, 31, 162))));

        JPanel bottomGrid = new JPanel(new GridLayout(1, 2, 20, 20));
        bottomGrid.setOpaque(false);
        bottomGrid.add(createHoverCard(createCampaignCard()));
        bottomGrid.add(createHoverCard(createReportCard()));

        content.add(header, BorderLayout.NORTH);
        content.add(statsGrid, BorderLayout.CENTER);
        content.add(bottomGrid, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(content, BorderLayout.NORTH);
        wrapper.add(Box.createVerticalGlue(), BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createHoverCard(JPanel inner) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        wrapper.add(inner);
        wrapper.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                inner.setBorder(HOVER_BORDER);
                wrapper.setBackground(new Color(255, 250, 240));
            }
            public void mouseExited(MouseEvent e) {
                inner.setBorder(BROWN_BORDER);
                wrapper.setBackground(Color.WHITE);
            }
        });
        return wrapper;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BROWN_BORDER);
        card.setPreferredSize(new Dimension(200, 100));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Times New Roman", Font.BOLD, 32));
        lblValue.setForeground(color);
        lblValue.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_BODY);
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 0));
        card.add(lblValue, BorderLayout.CENTER);
        card.add(lblTitle, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createCampaignCard() {
        return createTableCard("Active Campaigns",
                new String[]{"Campaign", "Status", "Vol.", "Progress"},
                new Object[][]{
                        {"Waves of Change", "Active", "45", "78%"},
                        {"Beach Cleanup Drive", "Active", "32", "65%"},
                        {"Urban Green Initiative", "Planning", "28", "40%"}
                },
                "WAVES OF CHANGE\nLocation: Mumbai Beach\nVolunteers: 45\n\n" +
                        "BEACH CLEANUP DRIVE\n15 spots\nVolunteers: 32\n\n" +
                        "URBAN GREEN\n500 trees\nVolunteers: 28");
    }

    private JPanel createReportCard() {
        return createTableCard("Recent Reports",
                new String[]{"Report ID", "Type", "Location", "Status"},
                new Object[][]{
                        {"001", "Garbage Overflow", "Andheri West", "Pending"},
                        {"002", "Illegal Dumping", "Bandra", "In Progress"},
                        {"003", "Water Logging", "Juhu Beach", "Resolved"}
                },
                "REPORT #001\nType: Garbage Overflow\nLocation: Andheri West\nReported: 02 Nov 2025\n\n" +
                        "REPORT #002\nType: Illegal Dumping\nLocation: Bandra\nStatus: Team Assigned\n\n" +
                        "REPORT #003\nType: Water Logging\nLocation: Juhu\nStatus: Resolved");
    }

    private JPanel createTableCard(String title, String[] columns, Object[][] data, String details) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BROWN_BORDER);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(18, 20, 10, 20));
        card.add(lblTitle, BorderLayout.NORTH);

        JTable table = new JTable(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(FONT_BODY);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 13));
        header.setBackground(HEADER_BROWN);
        header.setForeground(Color.WHITE);
        header.setBorder(null);
        header.setPreferredSize(new Dimension(0, 36));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table.setSelectionBackground(LIGHT_BROWN);
        table.setSelectionForeground(Color.BLACK);

        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) table.setRowSelectionInterval(row, row);
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent e) { table.clearSelection(); }
        });

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(header, BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);

        RoundedButton seeMore = new RoundedButton(" See More → ", 12);
        seeMore.setBackground(BROWN);
        seeMore.setForeground(Color.WHITE);
        seeMore.setFont(new Font("Times New Roman", Font.BOLD, 11));
        seeMore.addActionListener(e ->
                JOptionPane.showMessageDialog(card, details, title + " Details", JOptionPane.INFORMATION_MESSAGE));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottom.setBackground(Color.WHITE);
        bottom.add(seeMore);

        card.add(tablePanel, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    // Custom Rounded Search Field
    private static class RoundedSearchField extends JTextField {
        public RoundedSearchField() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            setBackground(new Color(250, 250, 250));
            setForeground(Color.GRAY);
            setCaretColor(Color.GRAY);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            super.paintComponent(g2);
            g2.setColor(BROWN);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
            g2.dispose();
        }
    }

    // Custom Rounded Button
    private static class RoundedButton extends JButton {
        private final int radius;
        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(new Font("Times New Roman", Font.BOLD, 13));
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

    // NO MAIN METHOD — Login se call hoga
}