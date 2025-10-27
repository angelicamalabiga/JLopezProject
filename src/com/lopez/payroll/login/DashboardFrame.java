package com.lopez.payroll.login;





import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

/**
 *
 * Features included (basic, working):
 *  - Add Employee (form: personal left / company & login right)
 *  - Manage Employee (table: view, edit, delete)
 *  - Verify Attendance (list + mark present/absent)
 *  - Automatic Payroll (simple run to compute a payroll record)
 *  - List of Pay Heads (CRUD)
 *  - Check Leave Request (view + approve/deny)
 *  - Cash Advance Checker (view + approve/pay)
 *  - List of Holiday (CRUD)
 *
 * Database: jdbc:mysql://localhost:3306/employee_login_db
 * Update DB_URL/DB_USER/DB_PASS to match your environment.
 *
 * NOTE: Passwords are saved in plaintext here for demonstration; hash in production.
 */

public class DashboardFrame extends JFrame {
    // ---------- DB CONFIG - change these to match your MySQL ----------
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employee_login_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // Theme colors
    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color WHITE = Color.decode("#ffffff");
    private static final Color MUSTARD = Color.decode("#ffe380");
    private static final Color LIGHT_MUSTARD = new Color(255, 240, 180);

    private String userEmail;
    private JPanel contentPanel;

    

    // shared table models

    private DefaultTableModel attendanceModel;
    private JTable attendanceTable;

    //private DefaultTableModel payheadsModel;
    //private JTable payheadsTable;

    //private DefaultTableModel leaveModel;
    //private JTable leaveTable;

    //private DefaultTableModel cashModel;
    //private JTable cashTable;

    //private DefaultTableModel holidayModel;
    //private JTable holidayTable;
    
    
    
//for connection sa mga files
    //ANDITO NGA YON OHHHHHHHHHHHHH
    
    private ADMIN_add_emp addEmployeeModule;
    private ADMIN_manage_emp manageEmployeeModule;
    
    private ADMIN_verify_attendance verifyAttendanceModule; 
    /*
    private ADMIN_automatic_payroll automaticPayrollModule;  
    private ADMIN_list_payheads listPayheadsModule;
    private ADMIN_check_leave checkLeaveModule;
    private ADMIN_advancecash_checker cashAdvanceModule; 
    private ADMIN_list_holiday listHolidayModule; 
    */







    public DashboardFrame(String email) {
        this.userEmail = email;
        this.addEmployeeModule = new ADMIN_add_emp(this);
        this.manageEmployeeModule = new ADMIN_manage_emp(this); 
        
        this.verifyAttendanceModule = new ADMIN_verify_attendance(this); 
        /*
        this.automaticPayrollModule = new ADMIN_automatic_payroll(this);  
        this.listPayheadsModule = new ADMIN_list_payheads(this);  
        this.checkLeaveModule = new ADMIN_check_leave(this);
        this.cashAdvanceModule = new ADMIN_advancecash_checker(this); 
        this.listHolidayModule = new ADMIN_list_holiday(this);  
        */





        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    // ---------------- UI init ----------------
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        JPanel sidebar = createSidebar();
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(WHITE);
        contentPanel.add(createHomePanel(), BorderLayout.CENTER);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Sidebar with all required menu items
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(MUSTARD);
        sidebar.setPreferredSize(new Dimension(260, 900));
        sidebar.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));

        String[] menus = {
                "Dashboard",
                "Add Employee",
                "Manage Employee",
                "Verify Attendance",
                "Automatic Payroll",
                "List of Pay Heads",
                "Check Leave Request",
                "Cash Advance Checker",
                "List of Holiday"
        };

        sidebar.add(Box.createVerticalStrut(70));
        for (String m : menus) {
            JButton btn = new JButton(m);
            btn.setMaximumSize(new Dimension(220, 42));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBackground(MUSTARD);
            btn.setForeground(BLACK);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setBorder(new EmptyBorder(6, 0, 6, 0));
            btn.addActionListener(e -> switchTo(m));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(LIGHT_MUSTARD); }
                public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(MUSTARD); }
            });
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(8));
        }

        sidebar.add(Box.createVerticalGlue());

     // Logout Button
        JButton logout = new JButton("Log Out");
        logout.setMaximumSize(new Dimension(220, 42));
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.setBackground(BLACK);
        logout.setForeground(WHITE);
        logout.setFocusPainted(false);
        logout.setFont(new Font("Arial", Font.BOLD, 14));
        logout.setBorder(new EmptyBorder(8, 0, 8, 0));

        // Hover effect for logout
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logout.setBackground(new Color(51, 51, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logout.setBackground(BLACK);
            }
        });

        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });        

        // Push it to the vertical center
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logout);
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    // Switch content panel
    public void switchTo(String page) {
        contentPanel.removeAll();
        switch (page) {
            case "Dashboard": contentPanel.add(createHomePanel(), BorderLayout.CENTER); break;
            case "Add Employee": contentPanel.add(addEmployeeModule.createPanel(), BorderLayout.CENTER); break;  // CHANGED THIS LINE
            case "Manage Employee":contentPanel.add(manageEmployeeModule.createPanel(), BorderLayout.CENTER); break;
            
            case "Verify Attendance":contentPanel.add(verifyAttendanceModule.createPanel(), BorderLayout.CENTER); break;        
            /*
            case "Automatic Payroll":contentPanel.add(automaticPayrollModule.createPanel(), BorderLayout.CENTER); break;           
            case "List of Pay Heads":contentPanel.add(listPayheadsModule.createPanel(), BorderLayout.CENTER); break;       
            case "Check Leave Request":contentPanel.add(checkLeaveModule.createPanel(), BorderLayout.CENTER); break;      
            case "Cash Advance Checker":contentPanel.add(cashAdvanceModule.createPanel(), BorderLayout.CENTER); break;     
            case "List of Holiday":contentPanel.add(listHolidayModule.createPanel(), BorderLayout.CENTER); break;          
            default: contentPanel.add(new JLabel("Not implemented"), BorderLayout.CENTER);
            */
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Header helper
    private JPanel headerPanel(String titleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLACK);
        header.setPreferredSize(new Dimension(100, 64));
        header.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel title = new JLabel(titleText);
        title.setForeground(MUSTARD);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);

        /*JButton close = new JButton("×");
        close.setFont(new Font("Arial", Font.BOLD, 22));
        close.setForeground(MUSTARD);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.addActionListener(e -> switchTo("Dashboard")); // top-right acts as back-to-dashboard
        header.add(close, BorderLayout.EAST);
*/
        return header;
    }

    // ---------------- Home Panel ----------------
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);

        panel.add(headerPanel("Dashboard"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(WHITE);
        body.setBorder(new EmptyBorder(30,30,30,30));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome, Admin");
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        welcome.setForeground(BLACK);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(welcome);
        body.add(Box.createVerticalStrut(10));

        JLabel info = new JLabel("Use the sidebar to manage employees, attendance, payroll and more.");
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setForeground(Color.DARK_GRAY);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(info);

        panel.add(body, BorderLayout.CENTER);
        return panel;
    }
    
   
  

    
    //NEXTTTTTTTTTTTTTT
 

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {}
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Attendance operations
    private void loadAttendance(String employeeFilter) {
        attendanceModel.setRowCount(0);
        String sql = "SELECT a.id, e.name, a.work_date, a.status FROM attendance a LEFT JOIN employees e ON a.employee_id = e.id";
        if (employeeFilter!=null && !employeeFilter.isEmpty()) sql += " WHERE e.name LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (employeeFilter!=null && !employeeFilter.isEmpty()) ps.setString(1, "%"+employeeFilter+"%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt(1));
                row.add(rs.getString(2));
                row.add(rs.getDate(3)!=null?rs.getDate(3).toString():"");
                row.add(rs.getString(4));
                attendanceModel.addRow(row);
            }
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "DB Error: "+ex.getMessage()); }
    }

    private void updateAttendanceStatus(int id, String status) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE attendance SET status = ? WHERE id = ?");
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "DB Error: "+ex.getMessage()); }
    }

   

    // ---------------- Utility helper methods ----------------
    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.65;
        panel.add(comp, gbc);
    }
    private void stylePrimaryButton(JButton b) {
        b.setBackground(MUSTARD); b.setForeground(BLACK);
        b.setFont(new Font("Arial", Font.BOLD, 13)); b.setFocusPainted(false);
    }
    private String safeDate(String s) {
        if (s==null || s.trim().isEmpty()) return null;
        return s.trim();
    }

    // ---------------- Loading utility wrappers ----------------
    

   

    // ---------------- main ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame("admin@example.com"));
       
       
    }
}