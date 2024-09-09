package tactical.blue.api;
import java.io.IOException;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class OpenAIClient {
    private String itemDescription;
    private final String BEGINNING_OF_PROMPT = "Using the following item description, indentify what its packaging is. It will typicaly be in the form of XX/package, XX/box, X per case, etc. If no packaging is found it is safe to assume it is just each, and so return each. Only return the value and packaging type. Here is the item description: ";
    private String entirePrompt;
    private final String API_KEY = getAPIKey();
    private final String API_URL = "https://api.openai.com/v1/chat/completions";


    public OpenAIClient(String itemDescription) {
        this.itemDescription = itemDescription;
        entirePrompt = BEGINNING_OF_PROMPT + itemDescription;
    }

    //makes the API Call to OpenAI gpt-4o-mini to get packaging
    public String makeAPICall() {
        String jsonBody = createJSONBody().toString();
        
        HttpResponse<JsonNode> response = Unirest.post(this.API_URL)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + this.API_KEY)  // Use API key grabbed by APIConfig
            .body(jsonBody)
            .asJson();

        // return the response if successful
        if (response.getStatus() == 200) {
            String packaging = response.getBody().getObject().getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"); //grabbing the response from the entire json object that is returned
            return packaging;
        } else {
            System.out.println("Request failed with status: " + response.getStatus());
            return "";
        }
    }


    //utility method that calls APIConfig.getApiKey() for API_KEY value
    private static String getAPIKey() {
        try {
            return APIConfig.getApiKey();
        }
        catch (IOException ioe) {
            return "";
        }
    }

    //creates the JSON request body for api call
    private JSONObject createJSONBody() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o-mini");
        JSONArray messagesArray = new JSONArray();
        JSONObject messageObject = new JSONObject();
        messageObject.put("role", "user");
        messageObject.put("content", this.entirePrompt);
        messagesArray.put(messageObject);
        requestBody.put("messages", messagesArray);
        return requestBody;
    }

    
}
