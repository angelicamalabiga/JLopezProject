package com.lopez.payroll.login;



import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ForgotPasswordFrame extends JFrame {
    private JTextField emailField;
    private JButton sendCodeButton;

    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color MUSTARD = Color.decode("#ffe380");

    public ForgotPasswordFrame() {
        setTitle("Forgot Password");
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

        //  Title
        JLabel titleLabel = new JLabel("Password Recovery");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(BLACK);
        titleLabel.setBounds(40, 20, 320, 25);
        mainPanel.add(titleLabel);

        // Info label
        JLabel infoLabel = new JLabel("Enter your email to receive verification code");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(BLACK);
        infoLabel.setBounds(40, 50, 320, 20);
        mainPanel.add(infoLabel);

        // 📧 Email label
        JLabel emailIcon = new JLabel("📧");
        emailIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        emailIcon.setBounds(10, 100, 30, 30);
        mainPanel.add(emailIcon);

        // ✉️ Rounded Email input 
        emailField = new JTextField() {
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
        emailField.setOpaque(false);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setForeground(BLACK);
        emailField.setCaretColor(BLACK);
        emailField.setBorder(new EmptyBorder(5, 10, 5, 10)); 
        emailField.setBounds(40, 100, 320, 30);
        mainPanel.add(emailField);

        //  Send button (black with mustard text)
        sendCodeButton = new JButton("Send Verification Code");
        sendCodeButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendCodeButton.setBackground(BLACK);
        sendCodeButton.setForeground(MUSTARD);
        sendCodeButton.setFocusPainted(false);
        sendCodeButton.setBorder(new LineBorder(MUSTARD, 2, true));
        sendCodeButton.setBounds(90, 165, 200, 35);
        sendCodeButton.addActionListener(e -> sendVerificationCode());

        mainPanel.add(sendCodeButton);
        add(mainPanel);
    }

    private void sendVerificationCode() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your email.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DatabaseManager dbManager = new DatabaseManager();
        if (!dbManager.emailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email not found in system.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String verificationCode = EmailService.generateVerificationCode();
        dbManager.saveVerificationCode(email, verificationCode);

        sendCodeButton.setEnabled(false);
        sendCodeButton.setText("Sending...");

        new Thread(() -> {
            boolean sent = EmailService.sendVerificationEmail(email, verificationCode);
            SwingUtilities.invokeLater(() -> {
                sendCodeButton.setEnabled(true);
                sendCodeButton.setText("Send Verification Code");

                if (sent) {
                    JOptionPane.showMessageDialog(this,
                            "Verification code sent to your email!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    new VerifyCodeFrame(email).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to send email. Please check your email configuration.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }
}