package tactical.blue.parsing.row_parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tactical.blue.excel.excelrows.BoundTreeExcelRow;
import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.parsing.row_parsing.BoundTreeRowParser;

public class BoundTreeRowParserTest {
     private BoundTreeRowParser parser;
    private Map<String, List<String[]>> webScrapedMap;
    private HashMap<String, Integer> columnHeaderIndex;
    
    @BeforeEach
    public void setUp() {
        parser = new BoundTreeRowParser();
        webScrapedMap = new HashMap<>();
        columnHeaderIndex = new HashMap<>();

        // Setup the column header index mappings
        columnHeaderIndex.put("Product", 0);
        columnHeaderIndex.put("Manufacturer", 1);
        columnHeaderIndex.put("SKU", 2);
        columnHeaderIndex.put("Packaging", 3);
        columnHeaderIndex.put("List_Price", 4);
        columnHeaderIndex.put("Wholesale", 5);
        columnHeaderIndex.put("Whole_Sale_Bulk", 6);
        columnHeaderIndex.put("List_Price_Bulk", 7);
        columnHeaderIndex.put("Bulk_Packaging", 8);

        // Example scraped data with valid regular and bulk pricing
        List<String[]> webScrapedData1 = new ArrayList<>();
        webScrapedData1.add(new String[] { "Product 1", "Manufacturer A", "SKU001", "Box of 10", "100.00", "80.00", "75.00", "90.00", "Box of 50" });
        webScrapedMap.put("http://example.com/product1", webScrapedData1);
        
        // Example scraped data with no bulk pricing
        List<String[]> webScrapedData2 = new ArrayList<>();
        webScrapedData2.add(new String[] { "Product 2", "Manufacturer B", "SKU002", "Each", "50.00", "40.00", null, null, null });
        webScrapedMap.put("http://example.com/product2", webScrapedData2);
    }

    @Test
    public void testParseRowWithBulkData() {
        String[] currItemArray = { "Customer Product A", "5", "http://example.com/product1" };
        
        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Check the regular row
        BoundTreeExcelRow row1 = (BoundTreeExcelRow) result.get(0);
        assertEquals("Product 1", row1.getItemName());
        assertEquals("Manufacturer A", row1.getManufacturer());
        assertEquals("SKU001", row1.getSku());
        assertEquals(5, row1.getQuantityRequested());
        assertEquals("Box of 10", row1.getPackaging());
        assertEquals(80.00, row1.getWholesalePrice(), 0.001);
        
        // Check the bulk row
        BoundTreeExcelRow row2 = (BoundTreeExcelRow) result.get(1);
        assertEquals("Product 1", row2.getItemName());
        assertEquals("Manufacturer A", row2.getManufacturer());
        assertEquals("SKU001", row2.getSku());
        assertEquals(5, row2.getQuantityRequested());
        assertEquals("Box of 50", row2.getPackaging());
        assertEquals(75.00, row2.getWholesalePrice(), 0.001);
    }

    @Test
    public void testParseRowWithoutBulkData() {
        String[] currItemArray = { "Customer Product B", "3", "http://example.com/product2" };
        
        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);
        
        assertNotNull(result);
        assertEquals(1, result.size());

        // Check the regular row
        BoundTreeExcelRow row = (BoundTreeExcelRow) result.get(0);
        assertEquals("Product 2", row.getItemName());
        assertEquals("Manufacturer B", row.getManufacturer());
        assertEquals("SKU002", row.getSku());
        assertEquals(3, row.getQuantityRequested());
        assertEquals("Each", row.getPackaging());
        assertEquals(40.00, row.getWholesalePrice(), 0.001);
    }

    @Test
    public void testParseRowWithInvalidUrl() {
        String[] currItemArray = { "Invalid Product", "1", "http://example.com/invalid" };

        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);

        assertNull(result);  // No match for the URL, expecting null
    }
}
