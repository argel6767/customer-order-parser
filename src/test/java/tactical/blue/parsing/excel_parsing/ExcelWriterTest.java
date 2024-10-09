package tactical.blue.parsing.excel_parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tactical.blue.excel.excelrows.ExcelRow;


public class ExcelWriterTest {

    @TempDir
    Path tempDir;

    private ExcelWriter excelWriter;
    private List<ExcelRow> excelRows;
    private String sheetTitle;

    @BeforeEach
    void setUp() {
        excelWriter = new ExcelWriter();
        excelRows = new ArrayList<>();
        sheetTitle = "Test Sheet";
    }

    @Test
    void testCreateExcelCells() {
            // Arrange
            ExcelRow row1 = new ExcelRow(
        "Dell Latitude 5420 Laptop, 14-inch FHD Display, Intel Core i5",  // itemDescription
        "Latitude 5420",                                                  // itemName
        "Dell",                                                          // manufacturer
        "LAT-5420-i5",                                                   // sku
        Integer.valueOf(5),                                              // quantityRequested
        "1 per box",                                                     // packaging
        Double.valueOf(1299.99),                                         // msrp
        Double.valueOf(999.99),                                          // wholeSalePrice
        "https://dell.com/latitude-5420"                                // productURL
        );
        ExcelRow row2 = new ExcelRow(
    "HP Premium Paper, 8.5 x 11, 98 Bright, 500 Sheets/Ream",       // itemDescription
    "Premium Paper Ream",                                           // itemName
    "HP",                                                           // manufacturer
    "HPP-8511-98B",                                                // sku
    Integer.valueOf(20),                                           // quantityRequested
    "10 reams per case",                                           // packaging
    Double.valueOf(12.99),                                         // msrp
    Double.valueOf(8.50),                                          // wholeSalePrice
    "https://suppliersite.com/paper/hpp-8511-98b"                 // productURL
);
        excelRows.add(row1);
        excelRows.add(row2);

        // Act
        excelWriter.createExcelCells(excelRows, sheetTitle);

        // Assert
        XSSFWorkbook workbook = excelWriter.getWorkbook();
        assertNotNull(workbook, "Workbook should not be null");
        Sheet sheet = workbook.getSheetAt(0);
        assertNotNull(sheet, "Sheet should not be null");
        assertEquals(sheetTitle + " " + LocalDate.now(), sheet.getSheetName(), "Sheet name should match");

        // Verify header row
        Row headerRow = sheet.getRow(0);
        assertNotNull(headerRow, "Header row should not be null");
        assertEquals("Row", headerRow.getCell(0).getStringCellValue());
        assertEquals("Item Description", headerRow.getCell(1).getStringCellValue());
        // ... continue asserting header cells as needed

        // Verify data rows
        Row dataRow1 = sheet.getRow(1);
        assertNotNull(dataRow1, "Data row 1 should not be null");
        assertEquals(1, (int) dataRow1.getCell(0).getNumericCellValue());
        assertEquals("Dell Latitude 5420 Laptop, 14-inch FHD Display, Intel Core i5", dataRow1.getCell(1).getStringCellValue());
        // ... continue asserting data cells as needed

        Row dataRow2 = sheet.getRow(2);
        assertNotNull(dataRow2, "Data row 2 should not be null");
        assertEquals(2, (int) dataRow2.getCell(0).getNumericCellValue());
        assertEquals("HP Premium Paper, 8.5 x 11, 98 Bright, 500 Sheets/Ream", dataRow2.getCell(1).getStringCellValue());
        // ... continue asserting data cells as needed
    }

    @Test
    void testGenerateExcelFile() throws IOException {
        // Arrange
        ExcelRow row3 = new ExcelRow(
    "USB-C Charging Cable, 6ft, Black, Bulk Pack",                 // itemDescription
    "USB-C Cable 6ft",                                            // itemName
    "Anker",                                                      // manufacturer
    "USBC-6FT-BLK",                                              // sku
    Integer.valueOf(100),                                         // quantityRequested
    "25 per box",                                                // packaging
    34.40,                                                        // msrp (will be handled by determineIfMSRPIsPresent)
    Double.valueOf(8.00),                                        // wholeSalePrice
    "https://anker.com/cables/usbc-6ft-blk"                     // productURL
);
        excelRows.add(row3);
        excelWriter.createExcelCells(excelRows, sheetTitle);

        String fileTitle = "TestFile-";
        Path filePath = tempDir.resolve("Desktop/Weekly-Reports/" + fileTitle + LocalDate.now() + ".xlsx");

        // Act
        // Override the folder path to the temp directory
        System.setProperty("user.home", tempDir.toString());
        excelWriter.generateExcelFile(fileTitle);

        // Assert
        File generatedFile = filePath.toFile();
        assertTrue(generatedFile.exists(), "Excel file should have been generated");

        // Verify contents of the file
        try (FileInputStream fis = new FileInputStream(generatedFile)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            assertNotNull(sheet, "Sheet should not be null");

            // Verify data
            Row dataRow = sheet.getRow(1);
            assertNotNull(dataRow, "Data row should not be null");
            assertEquals(1, (int) dataRow.getCell(0).getNumericCellValue());
            assertEquals("USB-C Charging Cable, 6ft, Black, Bulk Pack", dataRow.getCell(1).getStringCellValue());
        }
    }

    @Test
    void testAllowStyling() {
        // Arrange
        assertFalse(excelWriter.isApplyStyling(), "Styling should be false by default");

        // Act
        excelWriter.allowStyling();

        // Assert
        assertTrue(excelWriter.isApplyStyling(), "Styling should be enabled after calling allowStyling()");
    }

    @Test
    void testCreateExcelCellsWithNoStyling() {
        // Arrange
        excelWriter.allowStyling();
        ExcelRow row4 = new ExcelRow(
            "Ergonomic Office Chair, Mesh Back, Adjustable Arms",         // itemDescription
            "ErgoMesh Pro Chair",                                        // itemName
            "Herman Miller",                                             // manufacturer
            "HM-ERGO-2023",                                             // sku
            Integer.valueOf(10),                                         // quantityRequested
            "Single unit",                                              // packaging
            Double.valueOf(599.99),                                     // msrp
            Double.valueOf(449.99),                                     // wholeSalePrice
            "https://hermanmiller.com/chairs/ergomesh-pro"             // productURL
        );


        excelRows.add(row4);

        // Act
        excelWriter.createExcelCells(excelRows, sheetTitle);

        // Assert
        Sheet sheet = excelWriter.getWorkbook().getSheetAt(0);
        Row dataRow = sheet.getRow(1);
        Cell styledCell = dataRow.getCell(0); // Assuming styling is applied to the first cell

        CellStyle cellStyle = styledCell.getCellStyle();
        assertNotNull(cellStyle, "Cell style should not be null");

        // Check if the cell has the expected background color
        assertEquals(IndexedColors.AUTOMATIC.getIndex(), cellStyle.getFillForegroundColor(), "Cell background color should be light green");
        assertEquals(FillPatternType.NO_FILL, cellStyle.getFillPattern(), "Cell fill pattern should be solid foreground");
    }


}
