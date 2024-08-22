package tactical.blue.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ExcelRowTest {

    @Test
    void testGetCostOfGoods() {
        // Creating an ExcelRow object
        ExcelRow row = new ExcelRow("Laptop", "SKU123", 5, "5/Box", 999.99, 500.00, "https://example.com/laptop");
        
        // Calculate the expected cost of goods
        double expectedCostOfGoods = row.getQuantityNeeded() * row.getWholeSalePrice();
        
        // Assert that the calculated cost of goods is correct
        assertEquals(expectedCostOfGoods, row.getCostOfGoods(), 0.01);  // delta of 0.01 for floating-point comparison
    }

    @Test
    void testGetQuantityNeeded() {
        // Creating an ExcelRow object
        ExcelRow row = new ExcelRow("Mouse", "SKU456", 10, "10/Pack", 49.99, 20.00, "https://example.com/mouse");
        
        // In this example, quantityRequested = 10, packaging = "10/Pack", so quantityNeeded should be 1
        int expectedQuantityNeeded = 1;

        // Assert that the quantity needed is calculated correctly
        assertEquals(expectedQuantityNeeded, row.getQuantityNeeded());
    }
    
    @Test
    void testGetQuantityRequested() {
        // Creating an ExcelRow object
        ExcelRow row = new ExcelRow("Keyboard", "SKU789", "15", "15/Box", "79.99", "30.00", "https://example.com/keyboard");
        
        // Assert that the quantity requested is correctly set
        assertEquals(15, row.getQuantityRequested());
    }
    
    @Test
    void testCalculateQuantityNeeded_ValidPackagingWithNumber() {
        // Test valid packaging with a number (e.g., 5/Box)
        ExcelRow row = new ExcelRow("Laptop", "SKU123", 5, "5/Box", 999.99, 500.00, "https://example.com/laptop");
        
        // Expected quantity needed: ceil(5 / 5) = 1
        assertEquals(1, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_ValidPackagingWithEach() {
        // Test valid packaging with "each" (singular item packaging)
        ExcelRow row = new ExcelRow("Mouse", "SKU456", 7, "Each", 49.99, 20.00, "https://example.com/mouse");
        
        // Expected quantity needed: ceil(7 / 1) = 7
        assertEquals(7, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_EmptyPackaging() {
        // Test case with empty packaging (treating as each item)
        ExcelRow row = new ExcelRow("Keyboard", "SKU789", 3, "", 79.99, 30.00, "https://example.com/keyboard");
        
        // Expected quantity needed: ceil(3 / 1) = 3
        assertEquals(3, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_InvalidPackagingNoNumber() {
        // Test case where packaging has no number and is not "each"
        ExcelRow row = new ExcelRow("Monitor", "SKU101", 10, "Box", 199.99, 100.00, "https://example.com/monitor");
        
        // Expected quantity needed: fallback to treating it as "each"
        assertEquals(10, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_InvalidPackagingFormat() {
        // Test case with an invalid packaging format
        ExcelRow row = new ExcelRow("Phone", "SKU555", 8, "InvalidFormat", 699.99, 400.00, "https://example.com/phone");
        
        // Expected quantity needed: fallback to treating it as "each"
        assertEquals(8, row.getQuantityNeeded());
    }
     
   
}
