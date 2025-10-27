package com.lopez.payroll.login;



import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class VerifyCodeFrame extends JFrame {
    private JTextField codeField;
    private JButton verifyButton;
    private String email;
    
    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color MUSTARD = Color.decode("#ffe380");
    
    public VerifyCodeFrame(String email) {
        this.email = email;
        setTitle("Verify Code");
        setSize(400, 250);
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
        JLabel titleLabel = new JLabel("Enter Verification Code");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(BLACK);
        titleLabel.setBounds(40, 20, 320, 25);
        mainPanel.add(titleLabel);
        
        // Info label
        JLabel infoLabel = new JLabel("Check your email for the 6-digit code");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(BLACK);
        infoLabel.setBounds(40, 50, 320, 20);
        mainPanel.add(infoLabel);
        /*
        JLabel codeLabel = new JLabel("Code:");
        codeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        codeLabel.setForeground(BLACK);
        codeLabel.setBounds(40, 80, 60, 20);
        mainPanel.add(codeLabel);
        */
        // Rounded Code input
        codeField = new JTextField() {
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
        codeField.setOpaque(false);
        codeField.setFont(new Font("Arial", Font.PLAIN, 16));
        codeField.setForeground(BLACK);
        codeField.setCaretColor(BLACK);
        codeField.setBorder(new EmptyBorder(5, 10, 5, 10));
        codeField.setBounds(40, 100, 320, 30);
        codeField.setHorizontalAlignment(JTextField.CENTER);
        mainPanel.add(codeField);
        
        // Verify button
        verifyButton = new JButton("Verify Code");
        verifyButton.setFont(new Font("Arial", Font.BOLD, 14));
        verifyButton.setBackground(BLACK);
        verifyButton.setForeground(MUSTARD);
        verifyButton.setFocusPainted(false);
        verifyButton.setBorder(new LineBorder(MUSTARD, 2, true));
        verifyButton.setBounds(120, 165, 140, 35);
        verifyButton.addActionListener(e -> verifyCode());
        
        mainPanel.add(verifyButton);
        add(mainPanel);
    }
    
    private void verifyCode() {
        String code = codeField.getText().trim();
        
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the verification code.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (code.length() != 6) {
            JOptionPane.showMessageDialog(this, "Verification code must be 6 digits.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DatabaseManager dbManager = new DatabaseManager();
        if (dbManager.verifyCode(email, code)) {
            JOptionPane.showMessageDialog(this,
                    "Code verified! Please enter your new password.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new ResetPasswordFrame(email).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid or expired verification code.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}