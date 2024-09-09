package tactical.blue.api;

import java.io.FileInputStream;
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
    public static String getApiKey() throws IOException {
        return System.getenv("OPENAI_API_KEY");
    }

}
