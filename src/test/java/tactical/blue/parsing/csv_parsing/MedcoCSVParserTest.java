package tactical.blue.parsing.csv_parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.MedcoSportsMedicineExcelRow;

public class MedcoCSVParserTest {
    private MedcoCSVParser parser;
    private Map<String, List<String[]>> webScrapedMap;
    private HashMap<String, Integer> columnHeaderIndex;

    @BeforeEach
    public void setUp() {
        parser = new MedcoCSVParser();
        webScrapedMap = new HashMap<>();
        columnHeaderIndex = new HashMap<>();

        // Setup the column header index mappings
        columnHeaderIndex.put("Product", 0);
        columnHeaderIndex.put("Manufacturer", 1);
        columnHeaderIndex.put("SKU", 2);
        columnHeaderIndex.put("Wholesale", 3);

        // Example scraped data
        List<String[]> webScrapedData1 = new ArrayList<>();
        webScrapedData1.add(new String[] { "Product 1", "Manufacturer A", "SKU001", "80.00" });
        webScrapedMap.put("http://example.com/product1", webScrapedData1);

        List<String[]> webScrapedData2 = new ArrayList<>();
        webScrapedData2.add(new String[] { "Product 2", "Manufacturer B", "SKU002", "60.00" });
        webScrapedMap.put("http://example.com/product2", webScrapedData2);
    }

    @Test
    public void testParseRowWithValidData() {
        String[] currItemArray = { "Customer Product A", "5", "http://example.com/product1" };
        
        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);

        assertNotNull(result);
        assertEquals(1, result.size());

        // Check the row data
        MedcoSportsMedicineExcelRow row = (MedcoSportsMedicineExcelRow) result.get(0);
        assertEquals("Product 1", row.getItemName());
        assertEquals("Manufacturer A", row.getManufacturer());
        assertEquals("SKU001", row.getSku());
        assertEquals(5, row.getQuantityRequested());
        assertEquals("N/A", (String)row.getMsrp());  // MSRP is always 0.0 for Medco
        assertEquals(80.00, row.getWholesalePrice(), 0.001);
    }

    @Test
    public void testParseRowWithInvalidUrl() {
        String[] currItemArray = { "Invalid Product", "1", "http://example.com/invalid" };
        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);
        assertNull(result);  // No match for the URL, expecting null
    }

    @Test
    public void testParseRowWithMultipleProducts() {
        // Add multiple products under the same URL
        List<String[]> webScrapedData = new ArrayList<>();
        webScrapedData.add(new String[] { "Product 3", "Manufacturer C", "SKU003", "90.00" });
        webScrapedData.add(new String[] { "Product 4", "Manufacturer D", "SKU004", "70.00" });
        webScrapedMap.put("http://example.com/multiple", webScrapedData);

        String[] currItemArray = { "Customer Product Multiple", "3", "http://example.com/multiple" };

        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);

        assertNotNull(result);
        assertEquals(2, result.size());

        // Check the first product
        MedcoSportsMedicineExcelRow row1 = (MedcoSportsMedicineExcelRow) result.get(0);
        assertEquals("Product 3", row1.getItemName());
        assertEquals("Manufacturer C", row1.getManufacturer());
        assertEquals("SKU003", row1.getSku());
        assertEquals(3, row1.getQuantityRequested());
        assertEquals("N/A", (String)row1.getMsrp());
        assertEquals(90.00, row1.getWholesalePrice(), 0.001);

        // Check the second product
        MedcoSportsMedicineExcelRow row2 = (MedcoSportsMedicineExcelRow) result.get(1);
        assertEquals("Product 4", row2.getItemName());
        assertEquals("Manufacturer D", row2.getManufacturer());
        assertEquals("SKU004", row2.getSku());
        assertEquals(3, row2.getQuantityRequested());
        assertEquals("N/A", "N/A");
        assertEquals(70.00, row2.getWholesalePrice(), 0.001);
    }

}
