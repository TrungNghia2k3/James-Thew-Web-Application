//package com.dao;
//
//import com.model.User;
//import com.request.UserRequest;
//import com.config.DatabaseConfig;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserDAOTest {
//
//    private static UserDAO userDAO;
//
//    @BeforeAll
//    public static void setup() {
//        userDAO = new UserDAO();
//    }
//
//    @Test
//    public void testFindUserByUsername() throws SQLException {
//        String username = "admin_user"; // phải có user này trong DB
//
//        User user = userDAO.findUserByUsername(username);
//
//        System.out.println("User: " + user);
//        System.out.println("Username: " + user.getUsername());
//        System.out.println("Roles: " + user.getRoles());
//        System.out.println("Permissions: " + user.getPermissions());
//
//        assertNotNull(user);
//        assertEquals(username, user.getUsername());
//        assertNotNull(user.getRoles());
//        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN")));
//        assertNotNull(user.getPermissions());
//    }
//
//    @Test
//    public void testAddUser() {
//        UserDAO userDAO = new UserDAO();
//        UserRequest user = new UserRequest();
//
//        user.setUsername("staff_test");
//        user.setPassword("123456");
//        user.setEmail("staff_test@example.com");
//        user.setFirstName("Staff");
//        user.setLastName("Tester");
//        user.setPhone("0123456789");
//
//        try {
//            // Gọi hàm addUser
//            userDAO.addUser(user);
//
//            // Kết nối DB để kiểm tra user có tồn tại không
//            try (Connection conn = DatabaseConfig.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
//
//                stmt.setString(1, "staff_test");
//
//                try (ResultSet rs = stmt.executeQuery()) {
//                    assertTrue(rs.next(), "User should be added to the database");
//                    assertEquals("staff_test@example.com", rs.getString("email"));
//                    assertEquals("Staff", rs.getString("first_name"));
//
//                    System.out.println("User added successfully: " + rs.getString("username"));
//                    System.out.println("Email: " + rs.getString("email"));
//                    System.out.println("First Name: " + rs.getString("first_name"));
//                    System.out.println("Last Name: " + rs.getString("last_name"));
//                    System.out.println("Phone: " + rs.getString("phone"));
//                    System.out.println("User ID: " + rs.getInt("id"));
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail("Exception thrown during testAddUser: " + e.getMessage());
//        }
//    }
//}
