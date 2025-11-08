// src/com/wesolve/ui/AdminDashboard.java
package com.wesolve.ui;

import com.wesolve.model.Report;
import com.wesolve.Service.ReportService;
import com.wesolve.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminDashboard extends JFrame {

    private final ReportService reportService = ReportService.getInstance();
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public AdminDashboard() {
        setTitle("WeSolve – Admin Dashboard");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadReports();
        startAutoRefresh();
        setVisible(true);
    }

    private void initializeUI() {
        Color brown = new Color(178, 147, 91);
        Color lightBrown = new Color(205, 133, 63);
        Font mainFont = new Font("Times New Roman", Font.PLAIN, 16);
        Font headerFont = new Font("Times New Roman", Font.BOLD, 18);

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(brown);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Admin Dashboard – Manage Complaints", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // SEARCH BAR
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(brown);
        searchField = new JTextField(20);
        searchField.setFont(mainFont);
        searchField.setToolTipText("Search by Title, Location, or User");
        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(lightBrown);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(mainFont);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        searchBtn.addActionListener(e -> filterTable());
        searchField.addActionListener(e -> filterTable());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        // TABLE
        String[] columns = {"ID", "Title", "User", "Location", "Status", "Date", "Image"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only Status editable
            }
        };

        reportTable = new JTable(tableModel);
        reportTable.setFont(mainFont);
        reportTable.setRowHeight(50);
        reportTable.getTableHeader().setFont(headerFont);
        reportTable.getTableHeader().setBackground(brown);
        reportTable.getTableHeader().setForeground(Color.WHITE);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.setGridColor(new Color(200, 200, 200));

        // Status ComboBox Editor
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "In Progress", "Resolved"});
        statusCombo.setFont(mainFont);
        reportTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));
        reportTable.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(mainFont);
            Color bg = value.equals("Pending") ? new Color(231, 76, 60) :
                       value.equals("In Progress") ? new Color(243, 156, 18) : new Color(46, 204, 113);
            label.setBackground(isSelected ? new Color(230, 240, 255) : bg);
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            return label;
        });

        // Image Preview Click
        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = reportTable.rowAtPoint(e.getPoint());
                int col = reportTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 6) {
                    String imagePath = (String) tableModel.getValueAt(row, 6);
                    if (imagePath != null && !imagePath.isEmpty()) {
                        showImagePreview(imagePath);
                    }
                }
            }
        });

        sorter = new TableRowSorter<>(tableModel);
        reportTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // FOOTER
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton refreshBtn = new JButton("Refresh Reports");
        refreshBtn.setBackground(new Color(46, 204, 113));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadReports());

        footerPanel.add(refreshBtn);

        // Layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(Color.WHITE);
    }

    private void loadReports() {
        tableModel.setRowCount(0);
        List<Report> reports = reportService.getAllReports();
        for (Report r : reports) {
            String userName = getUserName(r.getUserId());
            Object[] row = {
                r.getId(),
                r.getTitle(),
                userName != null ? userName : "Unknown",
                r.getLocation(),
                r.getStatus(),
                r.getCreatedAt() != null ? r.getCreatedAt().toString().substring(0, 16) : "N/A",
                r.getImagePath() != null ? "View Image" : "No Image"
            };
            tableModel.addRow(row);
        }
        if (reports.isEmpty()) {
            tableModel.addRow(new Object[]{"", "No complaints submitted yet.", "", "", "", "", ""});
        }
    }

    private String getUserName(int userId) {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void showImagePreview(String imagePath) {
        JFrame preview = new JFrame("Image Preview");
        preview.setSize(600, 500);
        preview.setLocationRelativeTo(this);

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(580, 460, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(img));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        preview.add(new JScrollPane(label));
        preview.setVisible(true);
    }

    private void startAutoRefresh() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> loadReports());
            }
        }, 30000, 30000); // Every 30 seconds
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception ignored) {}
            new AdminDashboard();
        });
    }
}