package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEnv() {
        return System.getProperty("env", props.getProperty("env", "test"));
    }

    public static String getBaseUrl() {
        String env = getEnv();  // e.g. "staging"
        String key = env + "BaseUrl";  // e.g. "stagingBaseUrl"
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("No base URL found for environment: " + env);
        }
        return value;
    }
}
