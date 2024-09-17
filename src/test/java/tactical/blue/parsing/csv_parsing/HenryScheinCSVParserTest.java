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
import tactical.blue.excel.excelrows.HenryScheinExcelRow;

public class HenryScheinCSVParserTest {
        private HenryScheinCSVParser parser;
    private Map<String, List<String[]>> webScrapedMap;
    private HashMap<String, Integer> columnHeaderIndex;

    @BeforeEach
    public void setUp() {
        parser = new HenryScheinCSVParser();
        webScrapedMap = new HashMap<>();
        columnHeaderIndex = new HashMap<>();

        // Setup the column header index mappings
        columnHeaderIndex.put("\"HenrySchein_Product_And_Manufacturer\"", 1);
        columnHeaderIndex.put("\"HenrySchein_Packaging\"", 4);
        columnHeaderIndex.put("\"HenrySchein_MSRP\"", 2);
        columnHeaderIndex.put("\"HenrySchein_Wholesale\"", 3);

        // Example scraped data
        List<String[]> webScrapedData1 = new ArrayList<>();
        webScrapedData1.add(new String[] { "https://www.henryschein.com/us-en/Search.aspx?searchkeyWord=Ankle+Brace+X-Small", "Actimove Ar Wlkr Boot Ank/Ft/Lg Sz Men Up to 4.5 / Women Up to 6 X-Small Lft/Rt1467977 | BSN Medical, Inc - 7627255", "$66.99", "1 @ $32.69", "Each" });
        webScrapedMap.put("https://www.henryschein.com/us-en/Search.aspx?searchkeyWord=Ankle+Brace+X-Small", webScrapedData1);

        List<String[]> webScrapedData2 = new ArrayList<>();
        webScrapedData2.add(new String[] { "", "Cavilon Advanced Liquid Protectant 2.7mL Skin 20/Ca1275752 | 3M Medical Products - 5050", "$353.99", "1 @ $261.47", "CA20/Case" });
        webScrapedMap.put("https://www.henryschein.com/us-en/Search.aspx?searchkeyWord=liquid+skin", webScrapedData2);
    }

    @Test
    public void testParseRowWithValidData() {
        String[] currItemArray = { "Customer Product A", "5", "https://www.henryschein.com/us-en/Search.aspx?searchkeyWord=Ankle+Brace+X-Small" };

        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);

        assertNotNull(result);
        assertEquals(1, result.size());

        // Check the row data
        HenryScheinExcelRow row = (HenryScheinExcelRow) result.get(0);
        assertEquals("Actimove Ar Wlkr Boot Ank/Ft/Lg Sz Men Up to 4.5 / Women Up to 6 X-Small Lft/Rt", row.getItemName());
        assertEquals("BSN Medical, Inc", row.getManufacturer());
        assertEquals("7627255", row.getSku());
        assertEquals(5, row.getQuantityRequested());
        assertEquals("Each", row.getPackaging());
        assertEquals(66.99, (Double)row.getMsrp(), 0.001);
        assertEquals(32.69, row.getWholesalePrice(), 0.001);
    }

    @Test
    public void testParseRowWithInvalidUrl() {
        String[] currItemArray = { "Invalid Product", "1", "https://www.henryschein.com/us-en/Search.aspx?searchkeyWord=invalid" };

        List<ExcelRow> result = parser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);

        assertNull(result);  // No match for the URL, expecting null
    }

}
