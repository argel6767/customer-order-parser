package tactical.blue.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/*
 * Having to OpenAI Key given by sh file allows for greater secrecy when using API Keys and version control
 */
public class APIConfig {

    /*
     * Grabs OpenAI Key from sh file
     */
    public static String getApiKey() {
        Properties properties = new Properties();
        try (InputStream input = APIConfig.class.getResourceAsStream("/config.properties")) {
            properties.load(input);
            return properties.getProperty("open.ai.key");
        } catch (IOException ioe) {
            System.out.println("Failed to load API Key!\nUsing ENV backup");
            return System.getenv("OPENAI_API_KEY");
        }
    }

}

