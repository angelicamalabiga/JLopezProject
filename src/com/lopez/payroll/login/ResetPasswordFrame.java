package com.lopez.payroll.login;



import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ResetPasswordFrame extends JFrame {
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetButton;
    private JButton eyeButton1;
    private JButton eyeButton2;
    private String email;
    
    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color MUSTARD = Color.decode("#ffe380");
    private boolean showPassword1 = false;
    private boolean showPassword2 = false;
    
    public ResetPasswordFrame(String email) {
        this.email = email;
        setTitle("Reset Password");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(MUSTARD);
        
        // Title
        JLabel titleLabel = new JLabel("Create New Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(BLACK);
        titleLabel.setBounds(40, 20, 320, 25);
        mainPanel.add(titleLabel);
        
        // Info label
        JLabel infoLabel = new JLabel("Enter your new password below");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(BLACK);
        infoLabel.setBounds(40, 50, 320, 20);
        mainPanel.add(infoLabel);
        
        // New Password icon
        JLabel passwordIcon1 = new JLabel("🔑");
        passwordIcon1.setFont(new Font("SansSerif", Font.PLAIN, 20));
        passwordIcon1.setBounds(10, 90, 30, 30);
        mainPanel.add(passwordIcon1);
        
        // New Password field
        newPasswordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
            
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        newPasswordField.setOpaque(false);
        newPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        newPasswordField.setForeground(BLACK);
        newPasswordField.setCaretColor(BLACK);
        newPasswordField.setBorder(new EmptyBorder(5, 10, 5, 10));
        newPasswordField.setBounds(40, 90, 250, 30);
        mainPanel.add(newPasswordField);
        
        // Eye button for new password
        eyeButton1 = new JButton("👁");
        eyeButton1.setBounds(295, 90, 50, 30);
        eyeButton1.setContentAreaFilled(false);
        eyeButton1.setBorderPainted(false);
        eyeButton1.setFocusPainted(false);
        eyeButton1.setOpaque(false);
        eyeButton1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeButton1.addActionListener(e -> {
            showPassword1 = !showPassword1;
            if (showPassword1) {
                newPasswordField.setEchoChar((char) 0);
            } else {
                newPasswordField.setEchoChar('•');
            }
        });
        mainPanel.add(eyeButton1);
        
        // Confirm Password icon
        JLabel passwordIcon2 = new JLabel("🔐");
        passwordIcon2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        passwordIcon2.setBounds(10, 140, 30, 30);
        mainPanel.add(passwordIcon2);
        
        // Confirm Password field
        confirmPasswordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
            
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        confirmPasswordField.setOpaque(false);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setForeground(BLACK);
        confirmPasswordField.setCaretColor(BLACK);
        confirmPasswordField.setBorder(new EmptyBorder(5, 10, 5, 10));
        confirmPasswordField.setBounds(40, 140, 250, 30);
        mainPanel.add(confirmPasswordField);
        
        // Eye button for confirm password
        eyeButton2 = new JButton("👁");
        eyeButton2.setBounds(295, 140, 50, 30);
        eyeButton2.setContentAreaFilled(false);
        eyeButton2.setBorderPainted(false);
        eyeButton2.setFocusPainted(false);
        eyeButton2.setOpaque(false);
        eyeButton2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeButton2.addActionListener(e -> {
            showPassword2 = !showPassword2;
            if (showPassword2) {
                confirmPasswordField.setEchoChar((char) 0);
            } else {
                confirmPasswordField.setEchoChar('•');
            }
        });
        mainPanel.add(eyeButton2);
        
        // Reset button
        resetButton = new JButton("Reset Password");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(BLACK);
        resetButton.setForeground(MUSTARD);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(new LineBorder(MUSTARD, 2, true));
        resetButton.setBounds(110, 205, 160, 35);
        resetButton.addActionListener(e -> resetPassword());
        
        mainPanel.add(resetButton);
        add(mainPanel);
    }
    
    private void resetPassword() {
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DatabaseManager dbManager = new DatabaseManager();
        if (dbManager.updatePassword(email, newPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Password reset successfully! Please login with your new password.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update password.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}