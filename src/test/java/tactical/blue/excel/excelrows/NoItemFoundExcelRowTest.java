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
}
