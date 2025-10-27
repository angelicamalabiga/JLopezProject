package com.lopez.payroll.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import javax.imageio.ImageIO;

public class ADMIN_manage_emp {
    
    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color WHITE = Color.decode("#ffffff");
    private static final Color MUSTARD = Color.decode("#ffe380");
    private static final Color HEADER_COLOR = Color.decode("#DAA520");
    
    private DashboardFrame parentFrame;
    private DatabaseManager dbManager;
    private DefaultTableModel manageModel;
    private JTable manageTable;
    
    public ADMIN_manage_emp(DashboardFrame parent) {
        this.parentFrame = parent;
        this.dbManager = new DatabaseManager();
    }
    
    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.setBackground(WHITE);
        northContainer.add(createHeader(), BorderLayout.NORTH);
        
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(WHITE);
        
        JButton refresh = new JButton("Refresh");
        stylePrimaryButton(refresh);
        
        JTextField search = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        stylePrimaryButton(searchBtn);
        
        top.add(refresh);
        top.add(new JLabel("Search (name/email/id):"));
        top.add(search);
        top.add(searchBtn);
        
        northContainer.add(top, BorderLayout.CENTER);
        panel.add(northContainer, BorderLayout.NORTH);

        manageModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        
        manageModel.setColumnIdentifiers(new Object[]{"Employee ID", "Name", "Email", "Position", "Status", "Action"});
        manageTable = new JTable(manageModel);
        
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(manageTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);

        refresh.addActionListener(e -> loadEmployeesToTable(null));
        searchBtn.addActionListener(e -> loadEmployeesToTable(search.getText().trim()));

        loadEmployeesToTable(null);
        
        return panel;
    }
    
    private void styleTable() {
        manageTable.setRowHeight(45);
        
        JTableHeader header = manageTable.getTableHeader();
        header.setBackground(HEADER_COLOR);
        header.setForeground(WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        manageTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        manageTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        manageTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        manageTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        manageTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        manageTable.getColumnModel().getColumn(5).setPreferredWidth(250);
        
        manageTable.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        manageTable.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor(new JCheckBox()));
        
        manageTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(250, 250, 250));
                    }
                }
                
                if (column == 0) {
                    setHorizontalAlignment(CENTER);
                } else {
                    setHorizontalAlignment(LEFT);
                }
                
                return c;
            }
        });
        
        manageTable.setSelectionBackground(new Color(255, 240, 180));
        manageTable.setSelectionForeground(BLACK);
        manageTable.setFont(new Font("Arial", Font.PLAIN, 13));
    }
    
    class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton viewBtn;
        private JButton editBtn;
        
        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);
            
            viewBtn = createStyledButton("View", new Color(23, 162, 184));
            editBtn = createStyledButton("Edit", new Color(0, 123, 255));
            
            add(viewBtn);
            add(editBtn);
        }
        
        private JButton createStyledButton(String text, Color bgColor) {
            JButton btn = new JButton(text);
            btn.setBackground(bgColor);
            btn.setForeground(WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(70, 30));
            return btn;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (row % 2 == 0) {
                setBackground(Color.WHITE);
            } else {
                setBackground(new Color(250, 250, 250));
            }
            return this;
        }
    }
    
    class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton viewBtn;
        private JButton editBtn;
        private int currentRow;
        
        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setOpaque(true);
            
            viewBtn = createStyledButton("View", new Color(23, 162, 184));
            editBtn = createStyledButton("Edit", new Color(0, 123, 255));
            
            viewBtn.addActionListener(e -> {
                fireEditingStopped();
                viewEmployee(currentRow);
            });
            
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                editEmployee(currentRow);
            });
            
            panel.add(viewBtn);
            panel.add(editBtn);
        }
        
        private JButton createStyledButton(String text, Color bgColor) {
            JButton btn = new JButton(text);
            btn.setBackground(bgColor);
            btn.setForeground(WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(70, 30));
            return btn;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            if (row % 2 == 0) {
                panel.setBackground(Color.WHITE);
            } else {
                panel.setBackground(new Color(250, 250, 250));
            }
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
    
    private void viewEmployee(int row) {
        String id = manageModel.getValueAt(row, 0).toString();
        viewEmployeeDialog(id);
    }
    
    private void editEmployee(int row) {
        String id = manageModel.getValueAt(row, 0).toString();
        openEditDialog(id);
    }
    
    private void viewEmployeeDialog(String id) {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(parentFrame, "Database connection failed!");
                return;
            }
            
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (!rs.next()) return;

            JDialog dialog = new JDialog((Frame) null, "View Employee Details", true);
            dialog.setSize(600, 750);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setLayout(new BorderLayout());

            JPanel mainContainer = new JPanel(new BorderLayout());
            mainContainer.setBackground(WHITE);
            mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

            JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            photoPanel.setBackground(WHITE);
            photoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            
            String photoPath = rs.getString("photo");
            JLabel photoLabel = new JLabel();
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setBorder(BorderFactory.createLineBorder(MUSTARD, 3));
            
            if (photoPath != null && !photoPath.trim().isEmpty()) {
                try {
                    File photoFile = new File(photoPath);
                    if (photoFile.exists()) {
                        BufferedImage img = ImageIO.read(photoFile);
                        Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        photoLabel.setIcon(new ImageIcon(scaledImg));
                        photoLabel.setPreferredSize(new Dimension(150, 150));
                    } else {
                        photoLabel.setText("Photo Not Found");
                        photoLabel.setPreferredSize(new Dimension(150, 150));
                        photoLabel.setOpaque(true);
                        photoLabel.setBackground(new Color(240, 240, 240));
                    }
                } catch (Exception ex) {
                    photoLabel.setText("Error Loading Photo");
                    photoLabel.setPreferredSize(new Dimension(150, 150));
                    photoLabel.setOpaque(true);
                    photoLabel.setBackground(new Color(240, 240, 240));
                }
            } else {
                photoLabel.setText("No Photo");
                photoLabel.setPreferredSize(new Dimension(150, 150));
                photoLabel.setOpaque(true);
                photoLabel.setBackground(new Color(240, 240, 240));
            }
            
            photoPanel.add(photoLabel);
            mainContainer.add(photoPanel, BorderLayout.NORTH);

            JPanel detailsPanel = new JPanel(new GridBagLayout());
            detailsPanel.setBackground(WHITE);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            String fullName = rs.getString("first_name") + " " + 
                            (rs.getString("middle_name") != null ? rs.getString("middle_name") + " " : "") + 
                            rs.getString("last_name");
            String fullAddress = rs.getString("house_no") + ", " + rs.getString("barangay") + ", " + 
                               rs.getString("city") + ", " + rs.getString("province") + " " + rs.getString("zip_code");

            int y = 0;
            addViewField(detailsPanel, gbc, y++, "Employee ID:", rs.getString("id"));
            addViewField(detailsPanel, gbc, y++, "Full Name:", fullName);
            addViewField(detailsPanel, gbc, y++, "Email:", rs.getString("email"));
            addViewField(detailsPanel, gbc, y++, "Username:", rs.getString("username"));
            addViewField(detailsPanel, gbc, y++, "Position:", rs.getString("position"));
            addViewField(detailsPanel, gbc, y++, "Contact:", rs.getString("contact_no"));
            addViewField(detailsPanel, gbc, y++, "Date of Birth:", rs.getString("birth_date"));
            addViewField(detailsPanel, gbc, y++, "Gender:", rs.getString("gender"));
            addViewField(detailsPanel, gbc, y++, "Nationality:", rs.getString("nationality"));
            addViewField(detailsPanel, gbc, y++, "Address:", fullAddress);
            addViewField(detailsPanel, gbc, y++, "Emergency Contact 1:", rs.getString("emergency_no1"));
            addViewField(detailsPanel, gbc, y++, "Joining Date:", rs.getString("joining_date"));
            addViewField(detailsPanel, gbc, y++, "Status:", rs.getString("status"));
            addViewField(detailsPanel, gbc, y++, "Salary:", rs.getString("basic_salary"));

            JScrollPane scrollPane = new JScrollPane(detailsPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            mainContainer.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(WHITE);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JButton closeBtn = new JButton("Close");
            stylePrimaryButton(closeBtn);
            closeBtn.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeBtn);
            
            mainContainer.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(mainContainer);
            dialog.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "DB Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
    
    private void addViewField(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel valLabel = new JLabel(value != null ? value : "N/A");
        valLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(valLabel, gbc);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLACK);
        header.setPreferredSize(new Dimension(100, 64));
        header.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel title = new JLabel("Manage Employee");
        title.setForeground(MUSTARD);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);

        JButton close = new JButton("×");
        close.setFont(new Font("Arial", Font.BOLD, 22));
        close.setForeground(MUSTARD);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.addActionListener(e -> parentFrame.switchTo("Dashboard"));
        header.add(close, BorderLayout.EAST);

        return header;
    }
    
    private void loadEmployeesToTable(String keyword) {
        manageModel.setRowCount(0);
        String sql = "SELECT id, CONCAT(first_name, ' ', IFNULL(middle_name, ''), ' ', last_name) AS full_name, " +
                     "email, position, status FROM users WHERE role = 'user'";
        
        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND (CONCAT(first_name, ' ', last_name) LIKE ? OR email LIKE ? OR id LIKE ?)";
        }

        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(parentFrame, "Database connection failed!");
                return;
            }
            
            PreparedStatement ps = conn.prepareStatement(sql);
            
            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
                ps.setString(2, "%" + keyword + "%");
                ps.setString(3, "%" + keyword + "%");
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                manageModel.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("position"),
                    rs.getString("status"),
                    ""
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "DB Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
    
    private void openEditDialog(String id) {
        Connection conn = null;
        try {
            conn = dbManager.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(parentFrame, "Database connection failed!");
                return;
            }
            
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (!rs.next()) return;

            JDialog dialog = new JDialog((Frame) null, "Edit Employee", true);
            dialog.setSize(800, 750);
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setLayout(new BorderLayout());

            JPanel mainContainer = new JPanel(new BorderLayout());
            mainContainer.setBackground(WHITE);
            mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Photo section
            JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            photoPanel.setBackground(WHITE);
            
            String currentPhotoPath = rs.getString("photo");
            final String[] updatedPhotoPath = {currentPhotoPath};
            
            JLabel photoLabel = new JLabel();
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setBorder(BorderFactory.createLineBorder(MUSTARD, 3));
            photoLabel.setPreferredSize(new Dimension(150, 150));
            
            if (currentPhotoPath != null && !currentPhotoPath.trim().isEmpty()) {
                try {
                    File photoFile = new File(currentPhotoPath);
                    if (photoFile.exists()) {
                        BufferedImage img = ImageIO.read(photoFile);
                        Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        photoLabel.setIcon(new ImageIcon(scaledImg));
                    } else {
                        photoLabel.setText("No Photo");
                        photoLabel.setOpaque(true);
                        photoLabel.setBackground(new Color(240, 240, 240));
                    }
                } catch (Exception ex) {
                    photoLabel.setText("No Photo");
                    photoLabel.setOpaque(true);
                    photoLabel.setBackground(new Color(240, 240, 240));
                }
            } else {
                photoLabel.setText("No Photo");
                photoLabel.setOpaque(true);
                photoLabel.setBackground(new Color(240, 240, 240));
            }
            
            JButton changePhotoBtn = new JButton("Change Photo");
            stylePrimaryButton(changePhotoBtn);
            changePhotoBtn.setPreferredSize(new Dimension(130, 35));
            
            changePhotoBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "gif"));
                int result = fileChooser.showOpenDialog(dialog);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    updatedPhotoPath[0] = selectedFile.getAbsolutePath();
                    
                    try {
                        BufferedImage img = ImageIO.read(selectedFile);
                        Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        photoLabel.setIcon(new ImageIcon(scaledImg));
                        photoLabel.setText("");
                        photoLabel.setOpaque(false);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Error loading image: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            photoPanel.add(photoLabel);
            photoPanel.add(changePhotoBtn);
            mainContainer.add(photoPanel, BorderLayout.NORTH);

            JPanel content = new JPanel(new GridLayout(1, 2, 24, 0));
            content.setBackground(WHITE);

            // LEFT COLUMN - Personal Info
            JPanel left = new JPanel(new GridBagLayout());
            left.setBackground(WHITE);
            left.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MUSTARD, 2), 
                "Personal Information"
            ));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField firstNameFld = new JTextField(rs.getString("first_name"));
            JTextField middleNameFld = new JTextField(rs.getString("middle_name"));
            JTextField lastNameFld = new JTextField(rs.getString("last_name"));
            JTextField dobFld = new JTextField(rs.getString("birth_date"));
            JComboBox<String> genderFld = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            genderFld.setSelectedItem(rs.getString("gender"));
            JTextField nationalityFld = new JTextField(rs.getString("nationality"));
            JTextField houseNoFld = new JTextField(rs.getString("house_no"));
            JTextField barangayFld = new JTextField(rs.getString("barangay"));
            JTextField cityFld = new JTextField(rs.getString("city"));
            JTextField provinceFld = new JTextField(rs.getString("province"));
            JTextField zipCodeFld = new JTextField(rs.getString("zip_code"));
            JTextField contactFld = new JTextField(rs.getString("contact_no"));
            JTextField emergency1Fld = new JTextField(rs.getString("emergency_no1"));

            int y = 0;
            addLabelAndComponent(left, gbc, y++, "First Name:", firstNameFld);
            addLabelAndComponent(left, gbc, y++, "Middle Name:", middleNameFld);
            addLabelAndComponent(left, gbc, y++, "Last Name:", lastNameFld);
            addLabelAndComponent(left, gbc, y++, "Date of Birth (YYYY-MM-DD):", dobFld);
            addLabelAndComponent(left, gbc, y++, "Gender:", genderFld);
            addLabelAndComponent(left, gbc, y++, "Nationality:", nationalityFld);
            addLabelAndComponent(left, gbc, y++, "House No./Street:", houseNoFld);
            addLabelAndComponent(left, gbc, y++, "Barangay:", barangayFld);
            addLabelAndComponent(left, gbc, y++, "City:", cityFld);
            addLabelAndComponent(left, gbc, y++, "Province:", provinceFld);
            addLabelAndComponent(left, gbc, y++, "Zip Code:", zipCodeFld);
            addLabelAndComponent(left, gbc, y++, "Contact No:", contactFld);
            addLabelAndComponent(left, gbc, y++, "Emergency Contact 1:", emergency1Fld);

            // RIGHT COLUMN - Company & Login
            JPanel right = new JPanel(new GridBagLayout());
            right.setBackground(WHITE);
            right.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MUSTARD, 2), 
                "Company & Login"
            ));
            
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.insets = new Insets(8, 8, 8, 8);
            gbc2.fill = GridBagConstraints.HORIZONTAL;

            JTextField empIdField = new JTextField(rs.getString("id"));
            empIdField.setEditable(false);
            JTextField positionFld = new JTextField(rs.getString("position"));
            JTextField joiningFld = new JTextField(rs.getString("joining_date"));
            JComboBox<String> statusFld = new JComboBox<>(new String[]{"Active", "Deactivated"});
            statusFld.setSelectedItem(rs.getString("status"));
            JTextField emailFld = new JTextField(rs.getString("email"));
            JTextField usernameFld = new JTextField(rs.getString("username"));
            JPasswordField passFld = new JPasswordField(rs.getString("password"));
            JPasswordField confirmFld = new JPasswordField(rs.getString("password"));
            JTextField salaryFld = new JTextField(rs.getString("basic_salary"));

            int r = 0;
            addLabelAndComponent(right, gbc2, r++, "Employee ID (Read-only):", empIdField);
            addLabelAndComponent(right, gbc2, r++, "Position:", positionFld);
            addLabelAndComponent(right, gbc2, r++, "Date of Joining (YYYY-MM-DD):", joiningFld);
            addLabelAndComponent(right, gbc2, r++, "Status:", statusFld);
            addLabelAndComponent(right, gbc2, r++, "Email:", emailFld);
            addLabelAndComponent(right, gbc2, r++, "Username:", usernameFld);
            addLabelAndComponent(right, gbc2, r++, "Password:", passFld);
            addLabelAndComponent(right, gbc2, r++, "Confirm Password:", confirmFld);
            addLabelAndComponent(right, gbc2, r++, "Salary (monthly):", salaryFld);

            content.add(left);
            content.add(right);

            JScrollPane scrollPane = new JScrollPane(content);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            mainContainer.add(scrollPane, BorderLayout.CENTER);

            JButton save = new JButton("Update Employee");
            stylePrimaryButton(save);
            save.addActionListener(ev -> {
                String firstName = firstNameFld.getText().trim();
                String middleName = middleNameFld.getText().trim();
                String lastName = lastNameFld.getText().trim();
                String email = emailFld.getText().trim();
                String username = usernameFld.getText().trim();
                String post = positionFld.getText().trim();
                String join = joiningFld.getText().trim();
                String pass = new String(passFld.getPassword());
                String conf = new String(confirmFld.getPassword());

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || post.isEmpty() || join.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please fill required fields.", 
                        "Validation", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!pass.equals(conf)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Passwords do not match.", 
                        "Validation", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection updateConn = null;
                try {
                    updateConn = dbManager.getConnection();
                    if (updateConn == null) {
                        JOptionPane.showMessageDialog(dialog, "Database connection failed!");
                        return;
                    }
                    updateConn.setAutoCommit(false);
                    
                    PreparedStatement psu = updateConn.prepareStatement(
                        "UPDATE users SET first_name=?, middle_name=?, last_name=?, birth_date=?, gender=?, " +
                        "nationality=?, house_no=?, barangay=?, city=?, province=?, zip_code=?, " +
                        "contact_no=?, emergency_no1=?, emergency_no2=?, position=?, joining_date=?, " +
                        "status=?, email=?, username=?, password=?, basic_salary=?, photo=?, updated_at=NOW() WHERE id=?");
                    
                    psu.setString(1, firstName);
                    psu.setString(2, middleName.isEmpty() ? null : middleName);
                    psu.setString(3, lastName);
                    psu.setString(4, safeDate(dobFld.getText().trim()));
                    psu.setString(5, (String) genderFld.getSelectedItem());
                    psu.setString(6, nationalityFld.getText().trim());
                    psu.setString(7, houseNoFld.getText().trim());
                    psu.setString(8, barangayFld.getText().trim());
                    psu.setString(9, cityFld.getText().trim());
                    psu.setString(10, provinceFld.getText().trim());
                    psu.setString(11, zipCodeFld.getText().trim());
                    psu.setString(12, contactFld.getText().trim());
                    psu.setString(13, emergency1Fld.getText().trim());
                    psu.setString(15, post);
                    psu.setString(16, safeDate(join));
                    psu.setString(17, (String) statusFld.getSelectedItem());
                    psu.setString(18, email);
                    psu.setString(19, username.isEmpty() ? null : username);
                    psu.setString(20, pass);
                    psu.setString(21, salaryFld.getText().trim());
                    psu.setString(22, updatedPhotoPath[0]);
                    psu.setString(23, id);
                    psu.executeUpdate();
                    
                    updateConn.commit();
                    
                    JOptionPane.showMessageDialog(dialog, "Employee updated successfully.");
                    loadEmployeesToTable(null);
                    dialog.dispose();
                } catch (SQLException ex) {
                    if (updateConn != null) {
                        try {
                            updateConn.rollback();
                        } catch (SQLException e2) {
                            e2.printStackTrace();
                        }
                    }
                    JOptionPane.showMessageDialog(dialog, "DB Error: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    if (updateConn != null) {
                        try {
                            updateConn.setAutoCommit(true);
                            updateConn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnWrap.setBackground(WHITE);
            btnWrap.add(save);

            mainContainer.add(btnWrap, BorderLayout.SOUTH);
            dialog.add(mainContainer);
            dialog.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "DB Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
    
    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.65;
        panel.add(comp, gbc);
    }
    
    private void stylePrimaryButton(JButton b) {
        b.setBackground(MUSTARD);
        b.setForeground(BLACK);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
    }
    
    private String safeDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return s.trim();
    }
}