package tactical.blue.excel.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * Having to grab OpenAI Key from config allows for greater secrecy when using API Keys and version control
 */
public class APIConfig {

    /*
     * Grabs OpenAI Key from config file
     */
    public static String getApiKey() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/tactical/blue/config.properties");
        properties.load(fileInputStream);
        return properties.getProperty("OPENAI_API_KEY");
    }
}
