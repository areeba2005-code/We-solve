// src/com/wesolve/ui/ComplaintPortal.java
package com.wesolve.ui;

import com.wesolve.Service.ReportService;
import com.wesolve.model.Report;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ComplaintPortal extends JFrame {

    private final ReportService reportService = ReportService.getInstance();
    private final JPanel mainPanel = new JPanel(new CardLayout());
    private final JPanel submitPanel = new JPanel(new GridBagLayout());
    private final JPanel viewPanel = new JPanel(new BorderLayout());
    private final DefaultListModel<Report> listModel = new DefaultListModel<>();
    private final JList<Report> complaintList = new JList<>(listModel);

    // USER INFO
    private int currentUserId;
    private String currentUserName;

    // UI Components (for clearing form)
    private PlaceholderTextField txtTitle;
    private PlaceholderTextArea txtDesc;
    private PlaceholderTextField txtLocation;
    private JLabel lblImagePath;

    // FIXED CONSTRUCTOR
    public ComplaintPortal(int userId, String userName) {
        this.currentUserId = userId;
        this.currentUserName = userName;

        setTitle("WeSolve – Welcome, " + userName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        Color brown = new Color(178, 147, 91);
        Font mainFont = new Font("Times New Roman", Font.PLAIN, 16);

        // Tabs
        JButton submitTab = createTabButton("Submit Complaint", brown, mainFont);
        JButton viewTab = createTabButton("View All", brown, mainFont);

        JPanel tabPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tabPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabPanel.add(submitTab);
        tabPanel.add(viewTab);
        tabPanel.setBackground(Color.WHITE);

        createSubmitPanel(mainFont, brown);
        createViewPanel();

        mainPanel.add(submitPanel, "submit");
        mainPanel.add(viewPanel, "view");

        submitTab.addActionListener(e -> showCard("submit"));
        viewTab.addActionListener(e -> {
            loadComplaintsFromDB();
            showCard("view");
        });

        setLayout(new BorderLayout());
        add(tabPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
    }

    private JButton createTabButton(String text, Color bg, Font font) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        return btn;
    }

    private void createSubmitPanel(Font mainFont, Color brown) {
        submitPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        submitPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitleLabel = new JLabel("Title:");
        lblTitleLabel.setFont(mainFont);
        txtTitle = new PlaceholderTextField("Enter complaint title", 20, brown, mainFont);

        JLabel lblDesc = new JLabel("Description:");
        lblDesc.setFont(mainFont);
        txtDesc = new PlaceholderTextArea("Describe the issue...", 4, 20, brown, mainFont);

        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setFont(mainFont);
        txtLocation = new PlaceholderTextField("Enter location", 20, brown, mainFont);

        JLabel lblImage = new JLabel("Image (optional):");
        lblImage.setFont(mainFont);
        JButton btnChooseImage = new JButton("Choose File");
        lblImagePath = new JLabel("No image selected");
        lblImagePath.setFont(mainFont);

        btnChooseImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                lblImagePath.setText(f.getAbsolutePath());
            }
        });

        JButton btnSubmit = new JButton("Submit Complaint");
        btnSubmit.setBackground(new Color(46, 204, 113));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setFont(new Font("Times New Roman", Font.BOLD, 16));

        // SUBMIT ACTION WITH DUPLICATE CHECK
        btnSubmit.addActionListener(e -> {
            String title = txtTitle.getText().trim();
            String desc = txtDesc.getText().trim();
            String location = txtLocation.getText().trim();
            String image = lblImagePath.getText().equals("No image selected") ? null : lblImagePath.getText();

            // Validation
            if (title.equals("Enter complaint title") || desc.equals("Describe the issue...") ||
                location.equals("Enter location") || title.isEmpty() || desc.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // DUPLICATE CHECK
            if (reportService.isDuplicate(title, location)) {
                JOptionPane.showMessageDialog(this,
                    "<html><center><b style='color:#e74c3c; font-size:16px;'>Duplicate Complaint!</b><br><br>" +
                    "A complaint with the same <b>Title</b> and <b>Location</b> already exists.<br>" +
                    "Please check 'View All' or provide different details.</center></html>",
                    "Duplicate Detected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Report newReport = new Report(title, desc, location, image, "Pending");
            newReport.setUserId(currentUserId);

            if (reportService.saveReport(newReport)) {
                JOptionPane.showMessageDialog(this,
                    "<html><center><b style='font-size:16px; color:#27ae60;'>Complaint Submitted Successfully!</b><br><br>" +
                    "<span style='color:#555;'>Admin team will review your complaint<br>and respond within <b>2-3 days</b>.</span><br><br>" +
                    "Thank you for making our community better!</center></html>",
                    "WeSolve – Success", JOptionPane.INFORMATION_MESSAGE);

                clearForm();
                loadComplaintsFromDB();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving complaint!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add to panel
        gbc.gridx = 0; gbc.gridy = 0; submitPanel.add(lblTitleLabel, gbc);
        gbc.gridx = 1; submitPanel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1; submitPanel.add(lblDesc, gbc);
        gbc.gridx = 1;
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());
        submitPanel.add(scrollDesc, gbc);

        gbc.gridx = 0; gbc.gridy = 2; submitPanel.add(lblLocation, gbc);
        gbc.gridx = 1; submitPanel.add(txtLocation, gbc);

        gbc.gridx = 0; gbc.gridy = 3; submitPanel.add(lblImage, gbc);
        gbc.gridx = 1;
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(btnChooseImage);
        imagePanel.add(lblImagePath);
        submitPanel.add(imagePanel, gbc);

        gbc.gridx = 1; gbc.gridy = 4; submitPanel.add(btnSubmit, gbc);
    }

    private void createViewPanel() {
        complaintList.setCellRenderer(new ComplaintRenderer());
        JScrollPane scroll = new JScrollPane(complaintList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        viewPanel.add(scroll, BorderLayout.CENTER);
    }

    // FILTERED: Sirf user ki complaints
    private void loadComplaintsFromDB() {
        listModel.clear();
        List<Report> reports = reportService.getAllReports();
        for (Report r : reports) {
            if (r.getUserId() == currentUserId) {
                listModel.addElement(r);
            }
        }
    }

    private void showCard(String name) {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, name);
    }

    private void clearForm() {
        txtTitle.setText("");
        txtDesc.setText("");
        txtLocation.setText("");
        lblImagePath.setText("No image selected");
    }

    // PlaceholderTextField & TextArea
    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;
        public PlaceholderTextField(String placeholder, int cols, Color placeholderColor, Font font) {
            super(cols);
            this.placeholder = placeholder;
            setForeground(Color.GRAY);
            setText(placeholder);
            setFont(font);
            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override public void focusGained(java.awt.event.FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }
                @Override public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }

    private static class PlaceholderTextArea extends JTextArea {
        private final String placeholder;
        public PlaceholderTextArea(String placeholder, int rows, int cols, Color placeholderColor, Font font) {
            super(rows, cols);
            this.placeholder = placeholder;
            setForeground(Color.GRAY);
            setText(placeholder);
            setFont(font);
            setLineWrap(true);
            setWrapStyleWord(true);
            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override public void focusGained(java.awt.event.FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }
                @Override public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }

    // Custom Renderer
    static class ComplaintRenderer extends JLabel implements ListCellRenderer<Report> {
        public ComplaintRenderer() {
            setOpaque(true);
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setFont(new Font("Times New Roman", Font.PLAIN, 16));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Report> list, Report value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            String statusColor = value.getStatus().equals("Pending") ? "#e74c3c" :
                                 value.getStatus().equals("In Progress") ? "#f39c12" : "#27ae60";

            setText(String.format(
                "<html><b style='font-size:18px'>%s</b><br/>" +
                "<span style='color:#555'>%s</span><br/>" +
                "<i style='color:#777'>%s</i><br/>" +
                "<b style='color:%s'>Status: %s</b></html>",
                value.getTitle(),
                value.getDescription().length() > 100 ? value.getDescription().substring(0, 100) + "..." : value.getDescription(),
                value.getLocation(),
                statusColor,
                value.getStatus()
            ));

            setBackground(isSelected ? new Color(230, 240, 255) : Color.WHITE);
            setForeground(Color.BLACK);
            setBorder(BorderFactory.createLineBorder(new Color(178, 147, 91), 1));
            return this;
        }
    }
}