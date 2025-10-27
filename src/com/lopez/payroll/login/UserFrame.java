package com.lopez.payroll.login;



import javax.swing.*;
import java.awt.*;

public class UserFrame extends JFrame {
    private String userEmail;
    private String fullName;
    private JPanel contentPanel;

    // Theme colors
    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color WHITE = Color.decode("#ffffff");
    private static final Color MUSTARD = Color.decode("#ffe380");
    private static final Color LIGHT_MUSTARD = new Color(255, 240, 180);
    private static final Color FADED_BLACK = new Color(0, 0, 0, 100); // translucent effect look

    public UserFrame(String email, String fullName) {
        this.userEmail = email;
        this.fullName = fullName;

        setTitle("Employee Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar left
        JPanel sidebar = createSidebar();

        // Content area right
        contentPanel = createContentPanel();

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(MUSTARD);
        sidebar.setPreferredSize(new Dimension(220, 600));

        // Sidebar menu
        String[] menuItems = {
            "Attendance",
            "Report Attachment",
            "Summary of Current Pay Slip",
            "Leave Request",
            "Request Cash Advance",
            "Summary of Pay Slip",
            "Profile",
            "Holiday"
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], i == 0);
            sidebar.add(menuButton);
        }

        sidebar.add(Box.createVerticalGlue());

        // Profile at bottom
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(MUSTARD);
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 10));

        JLabel nameLabel = new JLabel(fullName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(BLACK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel(userEmail);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(BLACK);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        profilePanel.add(nameLabel);
        profilePanel.add(emailLabel);

        sidebar.add(profilePanel);

        return sidebar;
    }

    private JButton createMenuButton(String text, boolean isSelected) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));

        // Style for selected vs faded
        if (isSelected) {
            button.setBackground(LIGHT_MUSTARD);
            button.setForeground(BLACK);
        } else {
            button.setBackground(MUSTARD);
            button.setForeground(FADED_BLACK); // faded text look
        }

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_MUSTARD);
                button.setForeground(BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isSelected) {
                    button.setBackground(MUSTARD);
                    button.setForeground(FADED_BLACK);
                }
            }
        });

        // Click placeholder
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, text + " clicked!");
        });

        return button;
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(WHITE);

        // Header - Changed from violet to LIGHT_MUSTARD
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_MUSTARD);
        header.setPreferredSize(new Dimension(800, 60));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel headerTitle = new JLabel("Employee Dashboard");
        headerTitle.setForeground(BLACK);
        headerTitle.setFont(new Font("Arial", Font.BOLD, 20));

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 24));
        closeButton.setForeground(BLACK);
        closeButton.setBackground(LIGHT_MUSTARD);
        
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(e -> System.exit(0));

        header.add(headerTitle, BorderLayout.WEST);
        header.add(closeButton, BorderLayout.EAST);

        // Body
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(245, 245, 250));

        JLabel welcomeLabel = new JLabel("Welcome, " + fullName + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 0));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainContent.add(welcomeLabel);

        content.add(header, BorderLayout.NORTH);
        content.add(mainContent, BorderLayout.CENTER);

        return content;
    }
}