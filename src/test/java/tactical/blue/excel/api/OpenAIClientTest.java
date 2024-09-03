package tactical.blue.excel.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class OpenAIClientTest {
    @Test
    void testMakeAPICallWithPackagingDetails() {
        OpenAIClient client = new OpenAIClient("big long apples 34/pack"); 
        String packaging = client.makeAPICall();

        assertNotNull(packaging);
        assertEquals("34/pack", packaging);
    }

    @Test
    void testMakeAPICallWithNoPackagingDetails() {
        OpenAIClient client = new OpenAIClient("red wheel screws from Mobey's");
        String packaging = client.makeAPICall();

        assertNotNull(packaging);
        assertEquals("each", packaging);
    }
}
