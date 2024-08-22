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
        
     
   
}
