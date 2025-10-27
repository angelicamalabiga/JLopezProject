package com.lopez.payroll.login;


import javax.swing.SwingUtilities;


 /* Main entry point for the Employee Payroll System.
 * This class handles the initial application startup and
 * displays the login screen.
 */
public class Main {
    
    public static void main(String[] args) {
        // Set system look and feel for better UI appearance
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            // If setting look and feel fails, continue with default
            e.printStackTrace();
        }
        
        // Launch the login frame on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
