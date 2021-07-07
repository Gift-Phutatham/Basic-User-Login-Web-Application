package io.muic.ssc.webapp.service;

import com.zaxxer.hikari.HikariDataSource;
import io.muic.ssc.webapp.config.ConfigProperties;
import io.muic.ssc.webapp.config.ConfigurationLoader;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionService {
    private final HikariDataSource ds;

    public DatabaseConnectionService() {
        ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ConfigProperties configProperties = ConfigurationLoader.load();
        if (configProperties == null) {
            throw new RuntimeException("Unable to read the config.properties.");
        }
        ds.setDriverClassName(configProperties.getDatabaseDriverClassName());
        ds.setJdbcUrl(configProperties.getDatabaseConnectionUrl());
        ds.addDataSourceProperty("user", configProperties.getDatabaseUsername());
        ds.addDataSourceProperty("password", configProperties.getDatabasePassword());
        ds.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

//    public static void main(String[] args) {
//        try {
//            Connection connection = ds.getConnection();
//            String sql = "INSERT INTO tbl_user (username, password, display_name) VALUES (?, ?, ?);";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            /* Setting username column 1 */
//            ps.setString(1, "my_username");
//            /* Setting password column 2 */
//            ps.setString(2, "my_password");
//            /* Setting display name column 3 */
//            ps.setString(3, "my_display_name");
//            ps.executeUpdate();
//            /* So need to be manually commit the change */
//            connection.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}
