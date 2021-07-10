package io.muic.ssc.webapp.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationLoader {

    /**
     * Added static method for loading configuration from disk.
     * Default location is 'config.properties'.
     */
    public static ConfigProperties load() {
        try (FileInputStream fin = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(fin);
            /* Get the property value. */
            String driverClassName = prop.getProperty("database.driverClassName");
            String connectionUrl = prop.getProperty("database.connectionUrl");
            String username = prop.getProperty("database.username");
            String password = prop.getProperty("database.password");
            ConfigProperties cp = new ConfigProperties();
            cp.setDatabaseDriverClassName(driverClassName);
            cp.setDatabaseConnectionUrl(connectionUrl);
            cp.setDatabaseUsername(username);
            cp.setDatabasePassword(password);
            return cp;
        } catch (Exception e) {
            return null;
        }
    }
}
