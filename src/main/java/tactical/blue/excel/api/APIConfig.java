package tactical.blue.excel.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
         // Use ClassLoader to load the resource
        FileInputStream configFile = new FileInputStream("src/main/java/tactical/blue/excel/api/config/config.properties");
            properties.load(configFile);
        
        
        return properties.getProperty("OPENAI_API_KEY");
        }
    }

