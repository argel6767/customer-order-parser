package tactical.blue.parsing.csv_parsing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScrapedDataCSVParserTest {

    private ScrapedDataCSVParser parser;

    @BeforeEach
    void setUp() {
        // Create a spy of ScrapedDataCSVParser to mock inherited methods
        parser = Mockito.spy(new ScrapedDataCSVParser());
    }

    @Test
    void testMapRows_HenryScheinSite() {
        // Arrange
        File mockFile = mock(File.class);
        String siteName = "Henry Schein";
        List<String[]> mockRows = new ArrayList<>();

        // Add test data
        mockRows.add(new String[]{"\"https://product1.com\"", "Product1", "Desc1", "Price1", "Other1"});
        mockRows.add(new String[]{"\"https://product2.com\"", "Product2", "Desc2", "Price2", "Other2"});
        mockRows.add(new String[]{"\"https://product1.com\"", "Product1-Duplicate", "Desc1", "Price1", "Other1"});

        // Mock getCSVRows to return the mockRows
        doReturn(mockRows).when(parser).getCSVRows(mockFile);

        // Act
        HashMap<String, List<String[]>> result = parser.mapRows(mockFile, siteName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "There should be 2 unique product URLs");

        // Check that the keys are correct
        assertTrue(result.containsKey("https://product1.com"));
        assertTrue(result.containsKey("https://product2.com"));

        // Check the values associated with each key
        List<String[]> product1Rows = result.get("https://product1.com");
        assertEquals(2, product1Rows.size(), "Product1 should have 2 entries");

        List<String[]> product2Rows = result.get("https://product2.com");
        assertEquals(1, product2Rows.size(), "Product2 should have 1 entry");

        // Verify the content of the rows
        String[] expectedRow1 = {"\"https://product1.com\"", "Product1", "Desc1", "Price1", "Other1"};
        assertArrayEquals(expectedRow1, product1Rows.get(0));

        String[] expectedRow2 = {"\"https://product1.com\"", "Product1-Duplicate", "Desc1", "Price1", "Other1"};
        assertArrayEquals(expectedRow2, product1Rows.get(1));

        String[] expectedRow3 = {"\"https://product2.com\"", "Product2", "Desc2", "Price2", "Other2"};
        assertArrayEquals(expectedRow3, product2Rows.get(0));
    }

    @Test
    void testMapRows_OtherSite() {
        // Arrange
        File mockFile = mock(File.class);
        String siteName = "Bound Tree";
        List<String[]> mockRows = new ArrayList<>();

        // Add test data
        mockRows.add(new String[]{"Data0", "Product1", "Desc1", "Price1", "Other1", "https://product1.com"});
        mockRows.add(new String[]{"Data0", "Product2", "Desc2", "Price2", "Other2", "https://product2.com"});
        mockRows.add(new String[]{"Data0", "Product1-Duplicate", "Desc1", "Price1", "Other1", "https://product1.com"});

        // Mock getCSVRows to return the mockRows
        doReturn(mockRows).when(parser).getCSVRows(mockFile);

        // Act
        HashMap<String, List<String[]>> result = parser.mapRows(mockFile, siteName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "There should be 2 unique product URLs");

        // Check that the keys are correct
        assertTrue(result.containsKey("https://product1.com"));
        assertTrue(result.containsKey("https://product2.com"));

        // Check the values associated with each key
        List<String[]> product1Rows = result.get("https://product1.com");
        assertEquals(2, product1Rows.size(), "Product1 should have 2 entries");

        List<String[]> product2Rows = result.get("https://product2.com");
        assertEquals(1, product2Rows.size(), "Product2 should have 1 entry");

        // Verify the content of the rows
        String[] expectedRow1 = {"Data0", "Product1", "Desc1", "Price1", "Other1", "https://product1.com"};
        assertArrayEquals(expectedRow1, product1Rows.get(0));

        String[] expectedRow2 = {"Data0", "Product1-Duplicate", "Desc1", "Price1", "Other1", "https://product1.com"};
        assertArrayEquals(expectedRow2, product1Rows.get(1));

        String[] expectedRow3 = {"Data0", "Product2", "Desc2", "Price2", "Other2", "https://product2.com"};
        assertArrayEquals(expectedRow3, product2Rows.get(0));
    }

    @Test
    void testMapRows_RowsWithInsufficientColumns() {
        // Arrange
        File mockFile = mock(File.class);
        String siteName = "Bound Tree";
        List<String[]> mockRows = new ArrayList<>();

        // Add test data with some rows having less than 5 columns
        mockRows.add(new String[]{"Data0", "Product1", "Desc1"}); // Insufficient columns
        mockRows.add(new String[]{"Data0", "Product2", "Desc2", "Price2"}); // Insufficient columns
        mockRows.add(new String[]{"Data0", "Product3", "Desc3", "Price3", "Other3", "https://product3.com"}); // Valid row

        // Mock getCSVRows to return the mockRows
        doReturn(mockRows).when(parser).getCSVRows(mockFile);

        // Act
        HashMap<String, List<String[]>> result = parser.mapRows(mockFile, siteName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size(), "Only rows with sufficient columns should be processed");

        // Check that the key is correct
        assertTrue(result.containsKey("https://product3.com"));

        // Verify the content of the row
        List<String[]> product3Rows = result.get("https://product3.com");
        assertEquals(1, product3Rows.size(), "Product3 should have 1 entry");

        String[] expectedRow = {"Data0", "Product3", "Desc3", "Price3", "Other3", "https://product3.com"};
        assertArrayEquals(expectedRow, product3Rows.get(0));
    }

    @Test
    void testMapRows_EmptyInput() {
        // Arrange
        File mockFile = mock(File.class);
        String siteName = "Bound Tree";
        List<String[]> mockRows = new ArrayList<>(); // Empty list

        // Mock getCSVRows to return the empty list
        doReturn(mockRows).when(parser).getCSVRows(mockFile);

        // Act
        HashMap<String, List<String[]>> result = parser.mapRows(mockFile, siteName);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Resulting map should be empty for empty input");
    }

    @Test
    void testMapRows_NullRows() {
        // Arrange
        File mockFile = mock(File.class);
        String siteName = "Bound Tree";

        // Mock getCSVRows to return null
        doReturn(null).when(parser).getCSVRows(mockFile);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> parser.mapRows(mockFile, siteName), "Should throw NullPointerException when getCSVRows returns null");
    }
}

