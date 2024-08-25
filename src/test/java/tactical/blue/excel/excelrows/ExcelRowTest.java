package tactical.blue.excel.excelrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ExcelRowTest {
    @Test
    void testGetCostOfGoods() {
        // Creating an ExcelRow object
        ExcelRow row = new ExcelRow("Big ole laptop for games","Laptop", "Dell", "SKU123", 5, "5/Box", 999.99, 500.00, "https://example.com/laptop");
        
        // Calculate the expected cost of goods
        double expectedCostOfGoods = row.getQuantityNeeded() * row.getWholeSalePrice();
        
        // Assert that the calculated cost of goods is correct
        assertEquals(expectedCostOfGoods, row.getCostOfGoods(), 0.01);  // delta of 0.01 for floating-point comparison
    }

    @Test
    void testGetQuantityNeeded() {
        // Creating an ExcelRow object
        ExcelRow row = new ExcelRow("Logitech mouse","Mouse", "Logitech", "SKU456", 10, "10/Pack", 49.99, 20.00, "https://example.com/mouse");
        
        // In this example, quantityRequested = 10, packaging = "10/Pack", so quantityNeeded should be 1
        int expectedQuantityNeeded = 1;

        // Assert that the quantity needed is calculated correctly
        assertEquals(expectedQuantityNeeded, row.getQuantityNeeded());
    }
    
    @Test
    void testGetQuantityRequested() {
        // Creating an ExcelRow object using unformatted data constructor
        ExcelRow row = new ExcelRow("Keyboard", "Microsoft", "SKU789", "15", "15/Box", "79.99", "30.00", "https://example.com/keyboard");
        
        // Assert that the quantity requested is correctly set
        assertEquals(15, row.getQuantityRequested());
    }
    
    @Test
    void testCalculateQuantityNeeded_ValidPackagingWithNumber() {
        // Test valid packaging with a number (e.g., 5/Box)
        ExcelRow row = new ExcelRow("46 per box of laptops","Laptop", "Dell", "SKU123", 5, "5/Box", 999.99, 500.00, "https://example.com/laptop");
        
        // Expected quantity needed: ceil(5 / 5) = 1
        assertEquals(46, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_ValidPackagingWithEach() {
        // Test valid packaging with "each" (singular item packaging)
        ExcelRow row = new ExcelRow("apple mouse","Mouse", "Logitech", "SKU456", 7, "Each", 49.99, 20.00, "https://example.com/mouse");
        
        // Expected quantity needed: ceil(7 / 1) = 7
        assertEquals(7, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_EmptyPackaging() {
        // Test case with empty packaging (treating as each item)
        ExcelRow row = new ExcelRow("Red switches keyboard","Keyboard", "Microsoft", "SKU789", 3, "", 79.99, 30.00, "https://example.com/keyboard");
        
        // Expected quantity needed: ceil(3 / 1) = 3
        assertEquals(3, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_InvalidPackagingNoNumber() {
        // Test case where packaging has no number and is not "each"
        ExcelRow row = new ExcelRow("Montitor: red","Monitor", "Samsung", "SKU101", 10, "Box", 199.99, 100.00, "https://example.com/monitor");
        
        // Expected quantity needed: fallback to treating it as "each"
        assertEquals(10, row.getQuantityNeeded());
    }

    @Test
    void testCalculateQuantityNeeded_InvalidPackagingFormat() {
        // Test case with an invalid packaging format
        ExcelRow row = new ExcelRow("","Phone", "Apple", "SKU555", 8, "invalid", 699.99, 400.00, "https://example.com/phone");
        
        // Expected quantity needed: fallback to treating it as "each"
        assertEquals(8, row.getQuantityNeeded());
    }

    // Tests for setting the source based on productURL
     
    @Test
    void testSourceBoundtree() {
        // Create ExcelRow object with Boundtree URL
        ExcelRow row = new ExcelRow("Lap top for work","Laptop", "Dell", "SKU123", 5, "5/Box", 999.99, 500.00, "https://www.boundtree.com/product/123");

        // Assert the source is correctly identified as Boundtree
        assertEquals("Boundtree", row.getSource());
    }

    @Test
    void testSourceHenrySchein() {
        // Create ExcelRow object with Henry Schein URL
        ExcelRow row = new ExcelRow("Gaming mouse 6/pack","Mouse", "Logitech", "SKU456", 10, "10/Pack", 49.99, 20.00, "https://www.henryschein.com/product/456");

        // Assert the source is correctly identified as Henry Schein
        assertEquals("Henry Schein", row.getSource());
    }

    @Test
    void testSourceNorthAmericanRescue() {
        // Create ExcelRow object with North American Rescue URL
        ExcelRow row = new ExcelRow("Large bandages 66/box","Bandage", "Medline", "SKU789", 7, "Each", 19.99, 10.00, "https://www.narescue.com/product/789");

        // Assert the source is correctly identified as North American Rescue
        assertEquals("North American Rescue", row.getSource());
    }

    @Test
    void testSourceDynarex() {
        // Create ExcelRow object with Dynarex URL
        ExcelRow row = new ExcelRow("High tech neon guaze","Gauze", "Medline", "SKU101", 12, "12/Pack", 29.99, 15.00, "https://www.dynarex.com/product/101");

        // Assert the source is correctly identified as Dynarex
        assertEquals("Dynarex", row.getSource());
    }

    @Test
    void testSourceMedcoSportsMedicine() {
        // Create ExcelRow objchsatect with Medco Sports Medicine URL
        ExcelRow row = new ExcelRow("Electric tape for apple pie 16 per pack","Tape", "3M", "SKU555", 8, "8/Pack", 5.99, 3.00, "https://www.medco-athletics.com/product/555");

        // Assert the source is correctly identified as Medco Sports Medicine
        assertEquals("Medco Sports Medicine", row.getSource());
    }

    @Test
    void testSourceLoginRequired() {
        // Create ExcelRow object with 'Login Required' in the product URL
        ExcelRow row = new ExcelRow("small gloves for clean, box of 36","Gloves", "Ansell", "SKU666", 20, "20/Box", 15.99, 8.00, "Login required to access");

        // Assert the source is identified as Login Required
        assertEquals("Login Required", row.getSource());
    }

    @Test
    void testSourceUnknown() {
        // Create ExcelRow object with an unknown product URL
        ExcelRow row = new ExcelRow("","Unknown Item", "Unknown Manufacturer", "SKU999", 1, "Each", 1.99, 0.99, "https://www.unknown.com/product/999");

        // Assert the source is identified as Unknown
        assertEquals("Unknown", row.getSource());
    }
    
    // Test for manufacturer and SKU extraction
    @Test
    void testSetManufactuerAndSKUHenrySchein() {
        // Creating an ExcelRow object with manufacturer info for Henry Schein
        ExcelRow row = new HenryScheinExcelRow("Product", "1190394\n\n PDI Professional Disposables\n\n H04082", 10, "10/Pack", 49.99, 20.00, "https://www.henryschein.com/product/456");

        // Assert that the manufacturer is set correctly
        assertEquals("PDI Professional Disposables", row.getManufacturer());

        // Assert that the SKU is set correctly
        assertEquals("H04082", row.getSku());
    }

    @Test
    void testCalculateRawQuantityWithItemDescriptionContainingPackaging() {
        ExcelRow excelRow = new ExcelRow("pens, black ink, 0.7mm tip, packaged 12/box", "Black Ink Pens", "PenCorp", "PEN123", 36, "12/box", 15.99, 10.99, "https://example.com/product/pen123");
        assertEquals(432, excelRow.getQuantityRequested());
    }

    @Test 
    void testCalculateRawQuantityWithItemDescriptionHavingNoPackaging() {
        ExcelRow excelRow = new ExcelRow("Black ink pen, 0.7mm tip", "Black Ink Pen", "PenCorp", "PEN123", 36, "12/box", 15.99, 10.99, "https://example.com/product/pen123");
        assertEquals(36, excelRow.getQuantityRequested());
    }
    
    @Test
    void testCalculateRawQuantityWithNoItemDescription() {
        ExcelRow excelRow = new ExcelRow("", "Black Ink Pen", "PenCorp", "PEN123", 53, "each", 15.99, 10.99, "https://example.com/product/pen123");
        assertEquals(53, excelRow.getQuantityRequested());
    }
}
