package tactical.blue.parsing;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UrlSearchQueryNormalizer {

    public static String normalizeSearchQuery(String url) {
        String query = extractSearchQuery(url);
        return normalizeQuery(query);
    }

    private static String extractSearchQuery(String url) {
        String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        int query = decodedUrl.indexOf("=");
        return decodedUrl.substring(query);
    }

    private static String normalizeQuery(String query) {
         return query.toLowerCase().replaceAll("®", " ")                 // Replace ® with a space
                .replaceAll("[^a-z0-9.\\s]", "")    // Keep alphanumeric, space, and period
                .replaceAll("\\s+", " ")              // Normalize multiple spaces to a single space
                .trim();
    }

}
