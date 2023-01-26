package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static Properties properties;

    static {
        loadProperties();
    }

    private PropertiesUtils() {

    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void setProperties(Properties pr) {
        properties = pr;
    }

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
