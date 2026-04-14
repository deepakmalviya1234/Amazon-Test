package utility;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();
    private static String env;

    static {
        try {
            InputStream input = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }

            properties.load(input);

            // Priority: JVM param > config file
            env = System.getProperty("env", properties.getProperty("env", "qa"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    public static String get(String key) {
        String fullKey = env + "." + key;
        String value = properties.getProperty(fullKey);

        if (value == null) {
            throw new RuntimeException("Key not found: " + fullKey);
        }

        return value;
    }
}
