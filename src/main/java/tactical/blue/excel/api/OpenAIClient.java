package tactical.blue.excel.api;
import java.io.IOException;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class OpenAIClient {
    private String itemDescription;
    private final String BEGINNING_OF_PROMPT = "Using the following item description, indentify what its packaging is. It will typicaly be in the form of XX/package, XX/box, X per case, etc. If no packaging is found it is safe to assume it is just each and so return so. Here is the item description: ";
    private String entirePrompt;
    private static final String API_KEY = getUrlKey();
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";


    public OpenAIClient(String itemDescription) {
        this.itemDescription = itemDescription;
        entirePrompt = BEGINNING_OF_PROMPT + itemDescription;
    }

    public String makeAPICall() {
         @SuppressWarnings("static-access")
        HttpResponse<JsonNode> response = Unirest.post("https://api.openai.com/v1/completions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + this.API_KEY)  // Use API key grabbed by APIConfig
            .body("{\"model\":\"gpt-4o-mini\",\"prompt\":\"" + this.entirePrompt + "\",\"max_tokens\":100}")
            .asJson();

        // Print the response
        if (response.getStatus() == 200) {
            String packaging = response.getBody().getObject().getJSONArray("choices").getJSONObject(0).getString("text"); //grabbing the response from the entire json object that is returned
            return packaging;
        } else {
            System.out.println("Request failed with status: " + response.getStatus());
            return "";
        }
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
