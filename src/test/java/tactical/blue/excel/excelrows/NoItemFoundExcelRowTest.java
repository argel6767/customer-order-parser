package tactical.blue.excel.excelrows;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class NoItemFoundExcelRowTest {
    // Instantiation of NoItemFoundExcelRow with valid item description and product URL
    @Test
    public void testInstantiationWithValidData() {
        String itemDescription = "Sample Item";
        String productURL = "http://example.com/product";

        NoItemFoundExcelRow noItemFoundExcelRow = new NoItemFoundExcelRow(itemDescription, productURL);

        assertEquals(itemDescription, noItemFoundExcelRow.getItemDescription());
        assertEquals(productURL, noItemFoundExcelRow.getProductURL());
        assertEquals("Unknown", noItemFoundExcelRow.getSource());
    }


    // Correctly setting item description and product URL in NoItemFoundExcelRow
    @Test
    public void testCorrectlySettingItemDescriptionAndProductUrl() {
        String itemDescription = "Test Description";
        String productURL = "http://testurl.com";
        NoItemFoundExcelRow noItemFoundExcelRow = new NoItemFoundExcelRow(itemDescription, productURL);
        assertEquals(itemDescription, noItemFoundExcelRow.getItemDescription());
        assertEquals(productURL, noItemFoundExcelRow.getProductURL());
        assertEquals("Unknown", noItemFoundExcelRow.getSource());
    }

    @Test
    public void testProperlyDeterminingSource() {
        String itemDescription = "Test Description";
        String productURL = "http://testurl.com/product";
        NoItemFoundExcelRow noItemFoundExcelRow = new NoItemFoundExcelRow(itemDescription, productURL);
        assertEquals(itemDescription, noItemFoundExcelRow.getItemDescription());
        assertEquals(productURL, noItemFoundExcelRow.getProductURL());
        assertEquals("Unknown", noItemFoundExcelRow.getSource());
    }

    @Test
    public void testInstantiationWithLongDescriptionAndUrl() {
        String longDescription = "This is an extremely long item description that goes on and on to test the handling of very lengthy descriptions in the ExcelRow class";
        String longURL = "http://example.com/this/is/a/very/long/url/that/is/used/to/test/the/handling/of/long/urls/in/the/ExcelRow/class";
        NoItemFoundExcelRow noItemFoundExcelRow = new NoItemFoundExcelRow(longDescription, longURL);
        assertEquals(longDescription, noItemFoundExcelRow.getItemDescription());
        assertEquals(longURL, noItemFoundExcelRow.getProductURL());
        assertEquals("Unknown", noItemFoundExcelRow.getSource());
    }

    @Test
    public void testNoItemFoundExcelRowInstantiation() {
        String itemDescription = "Sample Item";
        String productURL = "http://example.com/product";
        NoItemFoundExcelRow noItemFoundExcelRow = new NoItemFoundExcelRow(itemDescription, productURL);
        assertEquals(itemDescription, noItemFoundExcelRow.getItemDescription());
        assertEquals(productURL, noItemFoundExcelRow.getProductURL());
        assertEquals("Unknown", noItemFoundExcelRow.getSource());
    }

     // Converts object to array with default values for missing items
     @Test
     public void testToArrayWithDefaultValues() {
         NoItemFoundExcelRow row = new NoItemFoundExcelRow("Sample Item", "http://example.com/product");
         Object[] result = row.toArray();
         assertEquals(16, result.length);
         assertEquals("No Item Found Matching Customer Description", result[2]);
         assertEquals("N/A", result[3]);
         assertEquals("N/A", result[5]);
         assertEquals("N/A", result[6]);
         assertEquals("N/A", result[7]);
         assertEquals("N/A", result[8]);
         assertEquals("N/A", result[9]);
         assertEquals("N/A", result[10]);
         assertEquals("N/A", result[12]);
         assertEquals("N/A", result[13]);
         assertEquals("N/A", result[14]);
     }
}
