package tactical.blue.parsing;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * holds the logic for generating urls that will be used for scraping sites
 */
public class UrlCreator {

    /*
     * generates the entire url
     */
    public static String createUrl(String siteName, String itemDescription) {
        return determineBaseUrl(siteName) + generateSearchQuery(itemDescription);
    }

    /*
     * generates the search query using the URLEncoder class
     */
    private static String generateSearchQuery(String itemDescription) {
        return URLEncoder.encode(lowercase(itemDescription), StandardCharsets.UTF_8);
    }

    /*
     * makes letter all lower case in case of misrepresentation
     */
    private static String lowercase(String input) {
        return input.toLowerCase();
    }

    /*
     * determines which base URL to use dependent on the site name
     * each has their own search query routing
     */
    private static String determineBaseUrl(String siteName) {
        String baseUrl = "";
        switch (siteName) {
            case "BoundTree" -> baseUrl = "https://boundtree.com/search/?text=";
            case "Henry Schein" -> baseUrl = "https://www.henryschein.com/us-en/Search.aspx?searchkeyWord=";
            case "Medco" -> baseUrl = "https://www.medco-athletics.com/catalogsearch/result/?q=";
        }
        return baseUrl;
    }
}
