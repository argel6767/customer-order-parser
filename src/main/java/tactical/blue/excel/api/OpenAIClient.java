package tactical.blue.excel.api;
import java.io.IOException;

public class OpenAIClient {
    private String itemDescription;
    private final String BEGINNING_OF_PROMPT = "";
    private String entirePrompt;
    private static final String API_KEY = getUrlKey();
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";


    public OpenAIClient(String itemDescription) {
        this.itemDescription = itemDescription;
        
    }



    private static String getUrlKey() {
        try {
            return APIConfig.getApiKey();
        }
        catch (IOException ioe) {
            return "";
        }
    }
}
