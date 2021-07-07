package io.muic.ssc.webapp.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationLoader {

    /* Added static method for loading configuration from disk
     * Default location is 'config.properties' in the same folder */
    public static ConfigProperties load() {
        try (FileInputStream fin = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(fin);
            /* Get the property value and print it out */
            String driverClassName = prop.getProperty("database.driverClassName");
            String connectionUrl = prop.getProperty("database.connectionUrl");
            String username = prop.getProperty("database.username");
            String password = prop.getProperty("database.password");
            return new ConfigProperties.ConfigPropertiesBuilder()
                    .databaseDriverClassName(driverClassName)
                    .databaseConnectionUrl(connectionUrl)
                    .databaseUsername(username)
                    .databasePassword(password)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

}
