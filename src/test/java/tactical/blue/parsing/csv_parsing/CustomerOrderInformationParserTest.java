package tactical.blue.parsing.csv_parsing;

import org.junit.jupiter.api.Test;

import tactical.blue.excel.excelrows.ExcelRow;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.LinkedHashMap;

public class CustomerOrderInformationParserTest {

    @Test
    public void testGetItemDescriptionsWithTemporaryFile() throws Exception {
        // Create a temporary CSV file
        File tempFile = File.createTempFile("testData", ".csv");
        tempFile.deleteOnExit();

        // Write test data to the file
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("ItemDescription\n"); // CSV header
            writer.write("Item1,45,www.nah.com\n");
            writer.write("Item2,55,storefront.come\n");
            writer.write("Item3,33,notarealsite.net\n");
        }

        // Instantiate your parser and call the method
        CustomerOrderInformationParser parser = new CustomerOrderInformationParser();
        LinkedHashMap<String, List<ExcelRow>> result = parser.getItemDescriptions(tempFile);

        // Perform assertions
        assertEquals(3, result.size());
        assertTrue(result.containsKey("Item1"));
        assertTrue(result.containsKey("Item2"));
        assertTrue(result.containsKey("Item3"));
    }
}

