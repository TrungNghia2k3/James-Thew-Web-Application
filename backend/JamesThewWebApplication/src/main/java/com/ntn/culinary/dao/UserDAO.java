package com.ntn.culinary.dao;

import com.ntn.culinary.utils.DatabaseUtils;
import com.ntn.culinary.constant.RoleType;
import com.ntn.culinary.model.Permission;
import com.ntn.culinary.model.Role;
import com.ntn.culinary.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class UserDAO {

    private static final UserDAO userDAO = new UserDAO();

    private UserDAO() {
        // Private constructor to prevent instantiation
    }

    public static UserDAO getInstance() {
        return userDAO;
    }

    // ------------------- CRUD OPERATIONS -------------------
    private static final String SELECT_ALL_USERS_QUERY =
            "SELECT u.id, u.username, u.email, u.first_name, u.last_name, u.phone, " +
                    "u.created_at, u.is_active, u.avatar, u.location, u.school, " +
                    "u.highlights, u.experience, u.education, u.social_links, " +
                    "r.id AS role_id, r.name AS role_name, " +
                    "p.id AS permission_id, p.name AS permission_name " +
                    "FROM users u " +
                    "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                    "LEFT JOIN roles r ON ur.role_id = r.id " +
                    "LEFT JOIN staff_permissions sp ON u.id = sp.user_id " +
                    "LEFT JOIN permissions p ON sp.permission_id = p.id";

    private static final String SELECT_USER_BY_ID_QUERY =
            "SELECT u.id, u.username, u.email, u.first_name, u.last_name, u.phone, u.created_at, " +
                    "u.is_active, u.avatar, u.location, u.school, u.highlights, u.experience, u.education, u.social_links, " +
                    "r.id AS role_id, r.name AS role_name, " +
                    "p.id AS permission_id, p.name AS permission_name " +
                    "FROM users u " +
                    "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                    "LEFT JOIN roles r ON ur.role_id = r.id " +
                    "LEFT JOIN staff_permissions sp ON u.id = sp.user_id " +
                    "LEFT JOIN permissions p ON sp.permission_id = p.id " +
                    "WHERE u.id = ?";

    // ------------------- INSERT QUERIES -------------------
    private static final String INSERT_USER_QUERY = "INSERT INTO users (username, password, created_at, is_active) VALUES (?, ?, NOW(), true)";
    private static final String GET_USER_ID = "SELECT id FROM users WHERE username = ?";
    private static final String GET_ROLE_ID = "SELECT id FROM roles WHERE name = ?";
    private static final String INSERT_USER_ROLE = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

    private static final String UPDATE_GENERAL_USER_QUERY = "UPDATE users " +
            "SET email = ?, first_name = ?, last_name = ?, phone = ?, avatar = ? " +
            "WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_USER_BY_USERNAME_QUERY =
            "SELECT u.id, u.username, u.password, u.is_active, " +
                    "r.id AS role_id, r.name AS role_name, " +
                    "p.id AS permission_id, p.name AS permission_name " +
                    "FROM users u " +
                    "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                    "LEFT JOIN roles r ON ur.role_id = r.id " +
                    "LEFT JOIN staff_permissions sp ON u.id = sp.user_id " +
                    "LEFT JOIN permissions p ON sp.permission_id = p.id " +
                    "WHERE u.username = ?";
    private static final String SELECT_SUBSCRIPTION_QUERY = "SELECT subscription_end_date FROM users WHERE id = ?";
    private static final String CHECK_USER_EXISTS_QUERY = "SELECT 1 FROM users WHERE id = ?";
    private static final String CHECK_USER_ID_EXISTS_QUERY = "SELECT 1 FROM users WHERE id = ? LIMIT 1";

    private static final String UPDATE_USER_ACTIVE_STATUS_QUERY = "UPDATE users " +
            "SET is_active = CASE WHEN is_active = 0 THEN 1 ELSE 0 END " +
            "WHERE id = ?";

    public boolean existsById(int id) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_USER_ID_EXISTS_QUERY)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true nếu tồn tại user có id đó
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        Map<Integer, User> userMap = new HashMap<>(); // map userId -> User

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_USERS_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("id");
                User user = userMap.get(userId);

                // Nếu chưa có User, tạo mới
                if (user == null) {
                    user = new User();
                    user.setId(userId);
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setAvatar(rs.getString("avatar"));
                    user.setLocation(rs.getString("location"));
                    user.setSchool(rs.getString("school"));
                    user.setHighlights(rs.getString("highlights"));
                    user.setExperience(rs.getString("experience"));
                    user.setEducation(rs.getString("education"));
                    user.setSocialLinks(rs.getString("social_links"));
                    userMap.put(userId, user);
                }

                // Lấy role nếu có
                int roleId = rs.getInt("role_id");
                String roleName = rs.getString("role_name");
                if (roleId > 0 && roleName != null) {
                    Role role = new Role();
                    role.setId(roleId);
                    role.setName(roleName);
                    user.getRoles().add(role); // Thêm role vào set
                }

                // Lấy permission nếu có
                int permissionId = rs.getInt("permission_id");
                String permissionName = rs.getString("permission_name");
                if (permissionId > 0 && permissionName != null) {
                    Permission permission = new Permission();
                    permission.setId(permissionId);
                    permission.setName(permissionName);
                    user.getPermissions().add(permission); // Thêm permission vào set
                }
            }
        }

        // Trả về danh sách users
        return new ArrayList<>(userMap.values());
    }

    public User getUserById(int id) throws SQLException {
        Map<Integer, User> userMap = new HashMap<>();

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID_QUERY)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("id");
                    User user = userMap.get(userId);

                    if (user == null) {
                        user = new User();
                        user.setId(userId);
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setFirstName(rs.getString("first_name"));
                        user.setLastName(rs.getString("last_name"));
                        user.setPhone(rs.getString("phone"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                        user.setActive(rs.getBoolean("is_active"));
                        user.setAvatar(rs.getString("avatar"));
                        user.setLocation(rs.getString("location"));
                        user.setSchool(rs.getString("school"));
                        user.setHighlights(rs.getString("highlights"));
                        user.setExperience(rs.getString("experience"));
                        user.setEducation(rs.getString("education"));
                        user.setSocialLinks(rs.getString("social_links"));
                        userMap.put(userId, user);
                    }

                    // Lấy role nếu có
                    int roleId = rs.getInt("role_id");
                    String roleName = rs.getString("role_name");
                    if (roleId > 0 && roleName != null) {
                        Role role = new Role();
                        role.setId(roleId);
                        role.setName(roleName);
                        user.getRoles().add(role);
                    }

                    // Lấy permission nếu có
                    int permissionId = rs.getInt("permission_id");
                    String permissionName = rs.getString("permission_name");
                    if (permissionId > 0 && permissionName != null) {
                        Permission permission = new Permission();
                        permission.setId(permissionId);
                        permission.setName(permissionName);
                        user.getPermissions().add(permission);
                    }
                }
            }
        }

        // Vì chỉ có 1 user, trả về hoặc null nếu không tìm thấy
        return userMap.values().stream().findFirst().orElse(null);
    }

    public void addUser(User user) throws SQLException {

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try (Connection conn = DatabaseUtils.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Insert user
            try (PreparedStatement insertUserStmt = conn.prepareStatement(INSERT_USER_QUERY)) {
                insertUserStmt.setString(1, user.getUsername());
                insertUserStmt.setString(2, hashedPassword);

                insertUserStmt.executeUpdate();
            }

            // 2. Get user_id
            int userId;
            try (PreparedStatement getUserIdStmt = conn.prepareStatement(GET_USER_ID)) {
                getUserIdStmt.setString(1, user.getUsername());
                try (ResultSet rs = getUserIdStmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("id");
                    } else {
                        conn.rollback();
                        throw new SQLException("Cannot find newly inserted user");
                    }
                }
            }

            // 3. Get role_id for GENERAL
            int roleId;
            try (PreparedStatement getRoleIdStmt = conn.prepareStatement(GET_ROLE_ID)) {
                getRoleIdStmt.setString(1, String.valueOf(RoleType.GENERAL));
                try (ResultSet rs = getRoleIdStmt.executeQuery()) {
                    if (rs.next()) {
                        roleId = rs.getInt("id");
                    } else {
                        conn.rollback();
                        throw new SQLException("Role 'GENERAL' not found");
                    }
                }
            }

            // 4. Insert user_roles
            try (PreparedStatement insertUserRoleStmt = conn.prepareStatement(INSERT_USER_ROLE)) {
                insertUserRoleStmt.setInt(1, userId);
                insertUserRoleStmt.setInt(2, roleId);
                insertUserRoleStmt.executeUpdate();
            }

            conn.commit(); // Commit nếu mọi thứ thành công
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("users.username")) {
                throw new SQLException("Username '" + user.getUsername() + "' already exists");
            } else if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("users.email")) {
                throw new SQLException("Email '" + user.getEmail() + "' already exists");
            } else {
                throw new SQLException("Error adding user and assigning role: " + e.getMessage(), e);
            }
        }
    }

    public void editGeneralUser(User user) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("UPDATE users SET ");
        List<Object> params = new ArrayList<>();

        // Chỉ thêm vào field nào không null (và có dữ liệu hợp lệ)
        if (user.getEmail() != null) {
            queryBuilder.append("email = ?, ");
            params.add(user.getEmail());
        }
        if (user.getFirstName() != null) {
            queryBuilder.append("first_name = ?, ");
            params.add(user.getFirstName());
        }
        if (user.getLastName() != null) {
            queryBuilder.append("last_name = ?, ");
            params.add(user.getLastName());
        }
        if (user.getPhone() != null) {
            queryBuilder.append("phone = ?, ");
            params.add(user.getPhone());
        }
        if (user.getAvatar() != null) {
            queryBuilder.append("avatar = ?, ");
            params.add(user.getAvatar());
        }

        // Nếu không có field nào cần update thì bỏ qua
        if (params.isEmpty()) {
            throw new SQLException("No fields to update for user id: " + user.getId());
        }

        // Xóa dấu phẩy cuối cùng
        int lastComma = queryBuilder.lastIndexOf(", ");
        if (lastComma != -1) {
            queryBuilder.delete(lastComma, lastComma + 2);
        }

        queryBuilder.append(" WHERE id = ?");
        params.add(user.getId());

        String finalQuery = queryBuilder.toString();
        System.out.println("SQL UPDATE query: " + finalQuery);

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(finalQuery)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Error while updating user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(int id) throws SQLException {

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER_QUERY)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while deleting user: " + e.getMessage(), e);
        }
    }

    public void toggleUserActiveStatus(int id) throws SQLException {

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_ACTIVE_STATUS_QUERY)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while toggling user active status: " + e.getMessage(), e);
        }
    }

    // ------------------- AUTHENTICATION & SUBSCRIPTION -------------------

    public User findUserByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_USERNAME_QUERY)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            User user = null;
            Set<Integer> roleIds = new HashSet<>();
            Set<Integer> permIds = new HashSet<>();

            while (rs.next()) {
                if (user == null) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setActive(rs.getBoolean("is_active"));
                }

                int roleId = rs.getInt("role_id");
                if (!rs.wasNull() && roleIds.add(roleId)) {
                    Role role = new Role();
                    role.setId(roleId);
                    role.setName(rs.getString("role_name"));
                    user.getRoles().add(role);
                }

                int permId = rs.getInt("permission_id");
                if (!rs.wasNull() && permIds.add(permId)) {
                    Permission perm = new Permission();
                    perm.setId(permId);
                    perm.setName(rs.getString("permission_name"));
                    user.getPermissions().add(perm);
                }
            }

            return user;
        } catch (SQLException e) {
            throw new SQLException("Error while fetching user by username: " + e.getMessage());
        }
    }

    public boolean isSubscriptionValid(int userId) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SUBSCRIPTION_QUERY)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date endDate = rs.getDate("subscription_end_date");
                return endDate != null && !endDate.before(new Date());
            }

            return false;
        } catch (SQLException e) {
            throw new SQLException("Error while checking subscription validity: " + e.getMessage(), e);
        }
    }

    // ------------------- UTILITIES -------------------

    private boolean userExists(int id) {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_USER_EXISTS_QUERY)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next(); // true if user exists
            }

        } catch (SQLException e) {
            return true; // If there's an error, we assume the user doesn't exist
        }
    }
}


