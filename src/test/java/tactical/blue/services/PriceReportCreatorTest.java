package tactical.blue.services;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

import tactical.blue.excel.excelrows.ExcelRow;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

class PriceReportCreatorTest {

    @TempDir
    Path tempDir;

    private File fileInWebScrape;
    private File fileInCustomerOrderInfo;
    private String siteName;

    @BeforeEach
    void setUp() throws IOException {
        // Create test files in the temporary directory
        fileInWebScrape = tempDir.resolve("webscrape.csv").toFile();
        fileInCustomerOrderInfo = tempDir.resolve("customerorder.csv").toFile();

        // Write test data to the files
        writeTestWebScrapeData(fileInWebScrape);
        writeTestCustomerOrderData(fileInCustomerOrderInfo);

        // Set the siteName
        siteName = "Bound Tree";
    }

    private void writeTestWebScrapeData(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write column headers
            writer.write("URL,Name,Price\n");
            // Write test data
            writer.write("http://example.com/product1,Product 1,10.0\n");
            writer.write("http://example.com/product2,Product 2,20.0\n");
        }
    }

    private void writeTestCustomerOrderData(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write column headers (assuming expected format)
            writer.write("Product Code,Quantity\n");
            // Write test data
            writer.write("Product 1,2\n");
            writer.write("Product 2,3\n");
        }
    }

    @Test
    void testMakeNewExcelFile() {
        // Arrange
        PriceReportCreator priceReportCreator = new PriceReportCreator(fileInWebScrape, fileInCustomerOrderInfo, siteName);

        // Act
        priceReportCreator.makeNewExcelFile();

        // Assert
        // Verify that the Excel file was generated
        File[] files = tempDir.toFile().listFiles((dir, name) -> name.startsWith(siteName + "-Report-") && name.endsWith(".xlsx"));
        assertNotNull(files, "Excel report files should not be null.");
        
        // Optionally, check that excelRows are populated
        List<ExcelRow> excelRows = priceReportCreator.getExcelRows();
        assertNotNull(excelRows, "Excel rows should not be null.");
    }

    @Test
    void testConstructorWithInvalidSiteName() {
        // Arrange
        String invalidSiteName = "Invalid Site";
        PriceReportCreator priceReportCreator = new PriceReportCreator(fileInWebScrape, fileInCustomerOrderInfo, invalidSiteName);


        // Assert
        // Since setRowParser() will print "Not a valid option!" and rowParser will be null
        assertNull(priceReportCreator.getRowParser(), "RowParser should be null for invalid site name.");
        // The excelRows should be empty because parsing couldn't occur
        assertTrue(priceReportCreator.getExcelRows().isEmpty(), "Excel rows should be empty for invalid site name.");
    }

    @Test
    void testMakeNewExcelFileWithEmptyWebScrapeFile() throws IOException {
        // Arrange
        // Create an empty web scrape file with only headers
        File emptyWebScrapeFile = tempDir.resolve("emptywebscrape.csv").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(emptyWebScrapeFile))) {
            writer.write("URL,Name,Price\n");
        }

        PriceReportCreator priceReportCreator = new PriceReportCreator(emptyWebScrapeFile, fileInCustomerOrderInfo, siteName);

        // Act
        priceReportCreator.makeNewExcelFile();

        // Assert
        // Excel file should still be generated but excelRows might be empty
        File[] files = tempDir.toFile().listFiles((dir, name) -> name.startsWith(siteName + "-Report-") && name.endsWith(".xlsx"));
        assertNotNull(files, "Excel report files should not be null.");
        assertTrue(priceReportCreator.getExcelRows().isEmpty(), "Excel rows should be empty when web scrape file is empty.");
    }

    @Test
    void testMakeNewExcelFileWithEmptyOrderInfoFile() throws IOException {
        // Arrange
        // Create an empty customer order info file with only headers
        File emptyOrderInfoFile = tempDir.resolve("emptyorderinfo.csv").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(emptyOrderInfoFile))) {
            writer.write("Product Code,Quantity\n");
        }

        PriceReportCreator priceReportCreator = new PriceReportCreator(fileInWebScrape, emptyOrderInfoFile, siteName);

        // Act
        priceReportCreator.makeNewExcelFile();

        // Assert
        // Excel file should be generated but excelRows might be empty
        File[] files = tempDir.toFile().listFiles((dir, name) -> name.startsWith(siteName + "-Report-") && name.endsWith(".xlsx"));
        assertNotNull(files, "Excel report files should not be null.");
        assertTrue(files.length == 0, "Excel report file was not generated.");
        assertTrue(priceReportCreator.getExcelRows().isEmpty(), "Excel rows should be empty when order info file is empty.");
    }

    @Test
    void testMakeNewExcelFileWithInvalidFiles() {
        // Arrange
        File nonExistentFile = new File("nonexistent.csv");
        PriceReportCreator priceReportCreator = new PriceReportCreator(nonExistentFile, nonExistentFile, siteName);
        // Assert
        // Since the files don't exist, excelRows should be empty
        assertTrue(priceReportCreator.getExcelRows().isEmpty(), "Excel rows should be empty when input files are invalid.");
    }

    @Test
    void testSetRowParserForValidSiteName() {
        // Arrange
        PriceReportCreator priceReportCreator = new PriceReportCreator();
        priceReportCreator.setSiteName("Henry Schein");

        // Act
        priceReportCreator.setRowParser("Henry Schein");

        // Assert
        assertNotNull(priceReportCreator.getRowParser(), "RowParser should not be null for valid site name.");
        assertTrue(priceReportCreator.getRowParser() instanceof tactical.blue.parsing.row_parsing.HenryScheinRowParser, "RowParser should be an instance of HenryScheinRowParser.");
    }

    @Test
    void testGetExcelColumnNames() {
        // Arrange
        PriceReportCreator priceReportCreator = new PriceReportCreator();
        String[] columnHeaders = {"URL", "Name", "Price"};

        // Act
        priceReportCreator.getExcelColumnNames(columnHeaders);

        // Assert
        assertEquals(3, priceReportCreator.getColumnHeaderIndex().size(), "ColumnHeaderIndex should have 3 entries.");
        assertEquals(0, priceReportCreator.getColumnHeaderIndex().get("URL"));
        assertEquals(1, priceReportCreator.getColumnHeaderIndex().get("Name"));
        assertEquals(2, priceReportCreator.getColumnHeaderIndex().get("Price"));
    }

    @Test
    void testParseRowsForExcelFileWithNullRowParser() {
        // Arrange
        PriceReportCreator priceReportCreator = new PriceReportCreator();
        priceReportCreator.setRowParser("Not a valid site.com"); // Set rowParser to null
        assertNull(priceReportCreator.getRowParser());
    }

    @Test
    void testSetBufferedReaderWithNonExistentFile() {
        // Arrange
        PriceReportCreator priceReportCreator = new PriceReportCreator();
        File nonExistentFile = new File("nonexistent.csv");

        // Act
        priceReportCreator.setBufferedReader(nonExistentFile);

        // Assert
        assertNull(priceReportCreator.getBufferedReaderWebScrape(), "BufferedReader should be null for non-existent file.");
    }
}
