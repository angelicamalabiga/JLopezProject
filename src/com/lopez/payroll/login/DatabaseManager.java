package com.lopez.payroll.login;



import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/payroll_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Connect to database
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Verify login (email + password)
    public boolean verifyLogin(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login using user ID and password
    public UserDetails getUserDetailsById(String userId, String password) {
        String query = """
            SELECT id, email, username, first_name, last_name, middle_name,
                   role, birth_date, gender, nationality,
                   house_no, barangay, city, province, zip_code,
                   contact_no, emergency_no1,
                   position, joining_date, status, basic_salary, photo
            FROM users
            WHERE id = ? AND password = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Get all user data from DB
                return new UserDetails(
                    rs.getString("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("middle_name"),
                    rs.getString("role"),
                    rs.getString("birth_date"),
                    rs.getString("gender"),
                    rs.getString("nationality"),
                    rs.getString("house_no"),
                    rs.getString("barangay"),
                    rs.getString("city"),
                    rs.getString("province"),
                    rs.getString("zip_code"),
                    rs.getString("contact_no"),
                    rs.getString("emergency_no1"),
                    rs.getString("position"),
                    rs.getString("joining_date"),
                    rs.getString("status"),
                    rs.getString("basic_salary"),
                    rs.getString("photo"),
                    "Deactivated".equalsIgnoreCase(rs.getString("status"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check if email already exists
    public boolean emailExists(String email) {
        String query = "SELECT email FROM users WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Save verification code
    public void saveVerificationCode(String email, String code) {
        String query = """
            INSERT INTO verification_codes (email, code, created_at)
            VALUES (?, ?, NOW())
            ON DUPLICATE KEY UPDATE code = ?, created_at = NOW()
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, code);
            pstmt.setString(3, code);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Verify if code is still valid
    public boolean verifyCode(String email, String code) {
        String query = "SELECT created_at FROM verification_codes WHERE email = ? AND code = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, code);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                LocalDateTime codeTime = createdAt.toLocalDateTime();
                LocalDateTime now = LocalDateTime.now();

                long minutesDiff = ChronoUnit.MINUTES.between(codeTime, now);
                if (minutesDiff <= 15) {
                    deleteVerificationCode(email);
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete code after verification
    private void deleteVerificationCode(String email) {
        String query = "DELETE FROM verification_codes WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update password in users table
    public boolean updatePassword(String email, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // User details model class
    public class UserDetails {
        private String id, email, username;
        private String firstName, lastName, middleName;
        private String role, birthDate, gender, nationality;
        private String houseNo, barangay, city, province, zipCode;
        private String contactNo, emergencyNo1;
        private String position, joiningDate, status, basicSalary, photo;
        private boolean isDeactivated;

        public UserDetails(String id, String email, String username,
                           String firstName, String lastName, String middleName,
                           String role, String birthDate, String gender, String nationality,
                           String houseNo, String barangay, String city, String province, String zipCode,
                           String contactNo, String emergencyNo1,
                           String position, String joiningDate, String status,
                           String basicSalary, String photo, boolean isDeactivated) {

            this.id = id;
            this.email = email;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.middleName = middleName;
            this.role = role;
            this.birthDate = birthDate;
            this.gender = gender;
            this.nationality = nationality;
            this.houseNo = houseNo;
            this.barangay = barangay;
            this.city = city;
            this.province = province;
            this.zipCode = zipCode;
            this.contactNo = contactNo;
            this.emergencyNo1 = emergencyNo1;
            this.position = position;
            this.joiningDate = joiningDate;
            this.status = status;
            this.basicSalary = basicSalary;
            this.photo = photo;
            this.isDeactivated = isDeactivated;
        }

        // Getters
        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getUsername() { return username; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getMiddleName() { return middleName; }
        public String getRole() { return role; }
        public String getBirthDate() { return birthDate; }
        public String getGender() { return gender; }
        public String getNationality() { return nationality; }
        public String getHouseNo() { return houseNo; }
        public String getBarangay() { return barangay; }
        public String getCity() { return city; }
        public String getProvince() { return province; }
        public String getZipCode() { return zipCode; }
        public String getContactNo() { return contactNo; }
        public String getEmergencyNo1() { return emergencyNo1; }
        public String getPosition() { return position; }
        public String getJoiningDate() { return joiningDate; }
        public String getStatus() { return status; }
        public String getBasicSalary() { return basicSalary; }
        public String getPhoto() { return photo; }
        public boolean isDeactivated() { return isDeactivated; }

        // Role checks
        public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }
        public boolean isUser() { return "user".equalsIgnoreCase(role); }
    }
}