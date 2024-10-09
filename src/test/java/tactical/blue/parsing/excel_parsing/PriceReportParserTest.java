package tactical.blue.parsing.excel_parsing;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.NoItemFoundExcelRow;

//TODO fix these TESTS!
public class PriceReportParserTest {
    @TempDir
    Path tempDir;

    private File testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("test.xlsx").toFile();
    }

    @Test
    void testParseFileWithValidData() throws IOException {
        // Arrange
        createValidTestExcelFile(testFile);
        PriceReportParser parser = new PriceReportParser(testFile);

        // Act
        List<ExcelRow> excelRows = parser.parseFile();

        // Assert
        assertNotNull(excelRows, "Excel rows should not be null");
        assertEquals(2, excelRows.size(), "Should parse two rows");

        ExcelRow firstRow = excelRows.get(0);
        //assertEquals("Item Description 1", firstRow.getItemDescription());
        assertEquals("Item Name 1", firstRow.getItemName());
        assertEquals("Manufacturer 1", firstRow.getManufacturer());
        assertEquals("Source 1", firstRow.getSource());
        assertEquals("SKU1", firstRow.getSku());
        assertEquals("Packaging 1", firstRow.getPackaging());
        assertEquals(Integer.valueOf(10), firstRow.getQuantityNeeded());
        assertEquals(100.0, firstRow.getMsrp());
       //assertEquals(80.0, firstRow.getWholeSalePrice());
        assertEquals(70.0, firstRow.getCostOfGoods());
        assertEquals(90.0, firstRow.getUnitPrice());
        assertEquals(900.0, firstRow.getExtendedPrice());
        //assertEquals(100.0, firstRow.getContribution());
        assertEquals("http://product1.com", firstRow.getProductURL());

        ExcelRow secondRow = excelRows.get(1);
        //assertEquals("Item Description 2", secondRow.getItemDescription());
        // ... continue assertions for second row
    }

    private void createValidTestExcelFile(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Header row
        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        // First data row
        Row dataRow1 = sheet.createRow(1);
        createDataRow(dataRow1, "Item Description 1", "Item Name 1", "Manufacturer 1", "Source 1",
                "SKU1", "Packaging 1", 10, 100.0, 80.0, 70.0, 90.0, 900.0, 100.0, "http://product1.com");

        // Second data row
        Row dataRow2 = sheet.createRow(2);
        createDataRow(dataRow2, "Item Description 2", "Item Name 2", "Manufacturer 2", "Source 2",
                "SKU2", "Packaging 2", 5, 200.0, 160.0, 140.0, 180.0, 900.0, 60.0, "http://product2.com");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    @Test
    void testParseFileWithInvalidNumberFormat() throws IOException {
        // Arrange
        createInvalidNumberFormatExcelFile(testFile);
        PriceReportParser parser = new PriceReportParser(testFile);

        // Act
        List<ExcelRow> excelRows = parser.parseFile();

        // Assert
        assertNotNull(excelRows, "Excel rows should not be null");
        assertEquals(1, excelRows.size(), "Should parse one row as NoItemFoundExcelRow");

        ExcelRow firstRow = excelRows.get(0);
        assertTrue(firstRow instanceof NoItemFoundExcelRow, "Row should be instance of NoItemFoundExcelRow");
        //assertEquals("Item Description Invalid", firstRow.getItemDescription());
    }

    private void createInvalidNumberFormatExcelFile(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Header row
        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        // Data row with invalid number format
        Row dataRow = sheet.createRow(1);
        createDataRow(dataRow, "Item Description Invalid", "Item Name", "Manufacturer", "Source",
                "SKU", "Packaging", "Invalid Quantity", "Invalid MSRP", "Invalid Wholesale", "Invalid Cost",
                "Invalid Unit Price", "Invalid Extended Price", "Invalid Contribution", "http://invalid.com");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    @Test
    void testParseFileWithMissingData() throws IOException {
        // Arrange
        createMissingDataExcelFile(testFile);
        PriceReportParser parser = new PriceReportParser(testFile);

        // Act
        List<ExcelRow> excelRows = parser.parseFile();

        // Assert
        assertNotNull(excelRows, "Excel rows should not be null");
        assertEquals(1, excelRows.size(), "Should parse one row");

        ExcelRow firstRow = excelRows.get(0);
        //assertEquals("Item Description Missing", firstRow.getItemDescription());
        assertEquals("N/A", firstRow.getMsrp());
    }

    private void createMissingDataExcelFile(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Header row
        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        // Data row with missing MSRP
        Row dataRow = sheet.createRow(1);
        createDataRow(dataRow, "Item Description Missing", "Item Name", "Manufacturer", "Source",
                "SKU", "Packaging", 10, "", 80.0, 70.0, 90.0, 900.0, 100.0, "http://missingmsrp.com");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    @Test
    void testParseEmptyFile() throws IOException {
        // Arrange
        createEmptyExcelFile(testFile);
        PriceReportParser parser = new PriceReportParser(testFile);

        // Act
        List<ExcelRow> excelRows = parser.parseFile();

        // Assert
        assertNotNull(excelRows, "Excel rows should not be null");
        assertTrue(excelRows.isEmpty(), "Excel rows should be empty");
    }

    private void createEmptyExcelFile(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("Sheet1");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // Helper methods to create Excel files
    private void createHeaderRow(Row row) {
        String[] headers = {"Row", "Item Description", "Item Name", "Manufacturer", "Source", "SKU",
                "Packaging", "Quantity", "MSRP", "Wholesale Price", "Cost of Goods", "Markup",
                "Unit Price", "Extended Price", "Contribution", "Product URL"};
        for (int i = 0; i < headers.length; i++) {
            createCell(row, i, headers[i]);
        }
    }

    private void createDataRow(Row row, Object... values) {
        for (int i = 0; i < values.length; i++) {
            createCell(row, i + 1, values[i]); // Start from column index 1
        }
    }

    private void createCell(Row row, int columnIndex, Object value) {
        Cell cell = row.createCell(columnIndex);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double || value instanceof Integer) {
            cell.setCellValue(Double.parseDouble(value.toString()));
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }
}
