package tactical.blue.excel;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CleanExcelFileTest {

    @Test
    public void testWriteExcelCells() {
        List<ExcelRow> excelRows = new ArrayList<>();

        // Add ExcelRow objects to the list
        excelRows.add(new ExcelRow("Item 1", "12345", 10, 19.99, 15.00, "https://example.com/product/12345"));
        excelRows.add(new ExcelRow("Item 2", "67890", 5, 29.99, 20.00, "https://example.com/product/67890"));
        excelRows.add(new ExcelRow("Item 3", "11223", 8, 9.99, 7.50, "https://example.com/product/11223"));

        CleanExcelFile cleanExcelFile = new CleanExcelFile("null", "null");
        cleanExcelFile.setExcelRows(excelRows);
        cleanExcelFile.createExcelCells();
    }
}
