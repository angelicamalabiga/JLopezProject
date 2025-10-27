package com.lopez.payroll.login;



import com.lopez.payroll.login.DatabaseManager.UserDetails;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import javax.swing.border.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class LoginFrame extends JFrame {
    private JTextField employeeIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton eyeButton;

    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color WHITE = Color.decode("#ffffff");
    private static final Color MUSTARD = Color.decode("#ffe380");
    private boolean showPassword = false;

    public LoginFrame() {
        setTitle("JS Lopez Consultancy Login System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setLayout(null);

        // LEFT PANEL (White background with logo)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBounds(0, 0, 250, 400);
        leftPanel.setLayout(null);

        try {
            URL logoUrl = new URL("https://scontent.fmnl17-1.fna.fbcdn.net/v/t1.15752-9/462560537_873311798290796_4767011135127192733_n.png?_nc_cat=100&ccb=1-7&_nc_sid=9f807c&_nc_ohc=57RUxsVQDuIQ7kNvwGDzRF1&_nc_oc=AdkNcYlr1jZFTMK_kXaL4GVDAiphjQYezLrM3k7rJm5Ho3iG9quYAynXkjJPihuzUqs&_nc_zt=23&_nc_ht=scontent.fmnl17-1.fna&oh=03_Q7cD3gEqIISXt-G3su4zKr_iWAehifS8TKMeMKFXLBoJYy0Jig&oe=69153C99");
            Image logoImage = ImageIO.read(logoUrl);
            Image scaled = logoImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon logoIcon = new ImageIcon(scaled);

            JLabel logoLabel = new JLabel(logoIcon, SwingConstants.CENTER);
            logoLabel.setBounds(50, 120, 150, 150);
            leftPanel.add(logoLabel);
            
        } catch (IOException e) {
            e.printStackTrace();
            JLabel fallback = new JLabel("JS Lopez Consultancy", SwingConstants.CENTER);
            fallback.setFont(new Font("SansSerif", Font.BOLD, 28));
            fallback.setForeground(Color.BLACK);
            fallback.setBounds(25, 160, 200, 40);
            leftPanel.add(fallback);
        }

        add(leftPanel);

        // RIGHT PANEL (Mustard background)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(MUSTARD);
        rightPanel.setBounds(250, 0, 450, 400);
        rightPanel.setLayout(null);

        // Employee ID Icon + Field
        JLabel employeeIdIcon = new JLabel("🆔"); 
        employeeIdIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        employeeIdIcon.setForeground(BLACK);
        employeeIdIcon.setBounds(70, 110, 30, 30);
        rightPanel.add(employeeIdIcon);

        employeeIdField = new RoundedTextField(15); 
        employeeIdField.setText("Enter Employee ID");  
        employeeIdField.setBounds(100, 110, 250, 30);
        employeeIdField.setForeground(Color.GRAY);

        employeeIdField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (employeeIdField.getText().equals("Enter Employee ID")) {
                    employeeIdField.setText("");
                    employeeIdField.setForeground(BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (employeeIdField.getText().isEmpty()) {
                    employeeIdField.setText("Enter Employee ID");
                    employeeIdField.setForeground(Color.GRAY);
                }
            }
        });

        rightPanel.add(employeeIdField);

        // Password Icon + Field
        JLabel passwordIcon = new JLabel("🔑");
        passwordIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        passwordIcon.setForeground(BLACK);
        passwordIcon.setBounds(70, 160, 30, 30);
        rightPanel.add(passwordIcon);

        passwordField = new RoundedPasswordField(15);
        passwordField.setText("Password");
        passwordField.setBounds(100, 160, 250, 30);
        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(Color.GRAY);

        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password");
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
        rightPanel.add(passwordField);

        // Eye button
        eyeButton = new JButton("👁");
        eyeButton.setBounds(355, 160, 50, 30);
        eyeButton.setContentAreaFilled(false);
        eyeButton.setBorderPainted(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setOpaque(false);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        eyeButton.addActionListener(e -> {
            showPassword = !showPassword;
            if (showPassword) {
                passwordField.setEchoChar((char) 0);
            } else {
                if (!String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setEchoChar('•');
                }
            }
        });
        rightPanel.add(eyeButton);

        // Sign In button
        loginButton = new JButton("Sign In");
        loginButton.setBounds(165, 230, 120, 35);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setBackground(BLACK);
        loginButton.setForeground(MUSTARD);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.addActionListener(e -> performLogin());

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(WHITE);
                loginButton.setForeground(BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(BLACK);
                loginButton.setForeground(MUSTARD);
            }
        });

        rightPanel.add(loginButton);

        // Forgot Password Label
        JLabel forgotPasswordLabel = new JLabel("Forgot Password");
        forgotPasswordLabel.setBounds(175, 275, 160, 30);
        forgotPasswordLabel.setForeground(Color.BLACK);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Font originalFont = forgotPasswordLabel.getFont();
        Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        forgotPasswordLabel.setFont(originalFont.deriveFont(attributes));

        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openForgotPassword();
            }
        });

        rightPanel.add(forgotPasswordLabel);

        add(rightPanel);
        setVisible(true);

        SwingUtilities.invokeLater(() -> loginButton.requestFocusInWindow());
    }
    
    private void performLogin() {
        String employeeId = employeeIdField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (employeeId.equals("Enter Employee ID") || employeeId.isEmpty() || 
            password.equals("Password") || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Employee ID and password.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DatabaseManager dbManager = new DatabaseManager();
        UserDetails userDetails = dbManager.getUserDetailsById(employeeId, password);
        
        if (userDetails != null) {
            // Check if account is deactivated
            if (userDetails.isDeactivated()) {
                JOptionPane.showMessageDialog(this, 
                    "Account deactivated. Please contact admin.",
                    "Account Deactivated", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Login successful - check role and redirect
            this.dispose();
            
            if (userDetails.isAdmin()) {
                // Redirect to Admin Dashboard
                new DashboardFrame(userDetails.getEmail()).setVisible(true);
            } else if (userDetails.isUser()) {
                // Redirect to User Dashboard
                String fullName = userDetails.getFirstName() + " " + 
                                (userDetails.getMiddleName() != null ? userDetails.getMiddleName() + " " : "") + 
                                userDetails.getLastName();
                new UserFrame(userDetails.getEmail(), fullName).setVisible(true);
            } else {
                // Unknown role
                JOptionPane.showMessageDialog(this, 
                    "Unknown user role. Please contact administrator.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                new LoginFrame().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID or password.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void openForgotPassword() {
        new ForgotPasswordFrame().setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

/* ---------- Rounded Components ---------- */
class RoundedTextField extends JTextField {
    private final int radius;

    public RoundedTextField(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}

class RoundedPasswordField extends JPasswordField {
    private final int radius;

    public RoundedPasswordField(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}