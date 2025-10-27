package com.lopez.payroll.login;



import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    //CONFIGURE THESE WITH GMAIL CREDENTIALS
    private static final String SENDER_EMAIL = "j.slopezconsultant@gmail.com";
    private static final String APP_PASSWORD = "sjiczrokztjyadff";
    
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    
    public static boolean sendVerificationEmail(String recipientEmail, String verificationCode) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Password Reset - Verification Code");
            
            String emailContent = String.format(
                "<html><body style='font-family: Arial, sans-serif;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f5f5f5;'>" +
                "<div style='background-color: white; padding: 30px; border-radius: 10px;'>" +
                "<h2 style='color: #333; text-align: center;'>Password Reset Request</h2>" +
                "<p style='color: #666; font-size: 16px;'>Hello,</p>" +
                "<p style='color: #666; font-size: 16px;'>You have requested to reset your password. " +
                "Please use the verification code below:</p>" +
                "<div style='background-color: #ffe380; color: white; padding: 20px; text-align: center; " +
                "border-radius: 5px; margin: 20px 0;'>" +
                "<h1 style='margin: 0; letter-spacing: 5px;'>%s</h1>" +
                "</div>" +
                "<p style='color: #666; font-size: 14px;'>This code will expire in 15 minutes.</p>" +
                "<p style='color: #666; font-size: 14px;'>If you didn't request this, please ignore this email.</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>" +
                "<p style='color: #999; font-size: 12px; text-align: center;'>Employee Login System</p>" +
                "</div></div></body></html>",
                verificationCode
            );
            
            message.setContent(emailContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            System.out.println("✓ Verification email sent successfully to: " + recipientEmail);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("✗ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}