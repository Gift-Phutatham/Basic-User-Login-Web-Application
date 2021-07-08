package io.muic.ooc.webapp.service;

import io.muic.ooc.webapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserService is used in too many places and we only need one instance of it so we will make it singleton.
 */
public class UserService {

    private static UserService service;
    private DatabaseConnectionService databaseConnectionService;
    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user (username, password, display_name) VALUES (?, ?, ?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?;";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM tbl_user;";
    private static final String DELETE_USER_SQL = "DELETE FROM tbl_user WHERE username = ?;";

    private UserService() {
    }

    public static UserService getInstance() {
        if (service == null) {
            service = new UserService();
            service.setDatabaseConnectionService(DatabaseConnectionService.getInstance());
        }
        return service;
    }

    public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    /**
     * Create new user.
     */
    public void createUser(String username, String password, String displayName) throws UserServiceException {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);
        ) {
            /* Setting username column 1. */
            ps.setString(1, username);
            /* Setting password column 2. */
            /* Password needs to be hashed and salted so bcrypt library is needed. */
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            /* Setting display name column 3. */
            ps.setString(3, displayName);
            ps.executeUpdate();
            /* So need to be manually commit the change. */
            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format("Username %s has already been taken.", username));
        } catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

    /**
     * Find user by username.
     */
    public User findByUsername(String username) {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);
        ) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    /* This is hashed password. */
                    resultSet.getString("password"),
                    resultSet.getString("display_name")
            );
        } catch (SQLException throwables) {
            return null;
        }
    }

    /**
     * List all users in the database.
     *
     * @return list of users, never return null
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERS_SQL);
        ) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("display_name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    /**
     * Delete user.
     *
     * @return true if successful
     */
    public boolean deleteUserByUsername(String username) {
        try (
                Connection connection = databaseConnectionService.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL);
        ) {
            ps.setString(1, username);
            int deleteCount = ps.executeUpdate();
            connection.commit();
            return deleteCount > 0;
        } catch (SQLException throwables) {
            return false;
        }
    }

    /**
     * Update user by user id.
     * Users can only change their display name when updating profile.
     *
     * @param id
     * @param displayName
     */
    public void updateUserById(long id, String displayName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Change password method is separated from update user method because
     * user normally never change password and update profile at the same time.
     *
     * @param newPassword
     */
    public void changePassword(String newPassword) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        try {
            userService.createUser("admin", "123456", "Admin");
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
//        List<User> users = userService.findAll();
//        for (User user : users) {
//            System.out.println(user.getUsername());
//        }
    }

}
