package tactical.blue.excel.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class OpenAIClientTest {
    @Test
    void testMakeAPICall() {
        OpenAIClient client = new OpenAIClient("big long apples 34/pack"); 
        String packaging = client.makeAPICall();

        System.err.println(packaging);
        assertNotNull(packaging);

    }
}
