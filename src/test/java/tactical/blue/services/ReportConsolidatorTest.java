package tactical.blue.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.parsing.excel_parsing.ExcelWriter;
import tactical.blue.parsing.excel_parsing.PriceReportParser;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportConsolidatorTest {
    @Mock
    private ExcelWriter mockExcelWriter;
    @Mock
    private File mockFile1;
    @Mock
    private File mockFile2;
    @Mock
    private PriceReportParser mockParser;
    private ReportConsolidator reportConsolidator;
    private LinkedHashMap<String, List<ExcelRow>> itemDescriptionMappedRows;
    private List<File> priceReportFiles;

    @BeforeEach
    void setUp() {
        itemDescriptionMappedRows = new LinkedHashMap<>();
        priceReportFiles = new ArrayList<>();
        priceReportFiles.add(mockFile1);
        priceReportFiles.add(mockFile2);
    }

    @Test
    void testConsolidateReportsProcessAndWriteData() {
        List<ExcelRow> sortedRows = new ArrayList<>();
        reportConsolidator = spy(new ReportConsolidator(priceReportFiles, mockExcelWriter, itemDescriptionMappedRows));
        doReturn(new ArrayList<ExcelRow>()).when(reportConsolidator).grabAllRowsFromEveryFile();
        doReturn(sortedRows).when(reportConsolidator).sortGroupedLists();
        reportConsolidator.consolidateReports();
        verify(mockExcelWriter).allowStyling();
        verify(mockExcelWriter).createExcelCells(sortedRows, "Combined Weekly Report");
        verify(mockExcelWriter).generateExcelFile("Combined-Weekly-Report-");
    }



    @Test
    void testGroupExcelRowsByItemDescriptionGroupCorrectly() {
        List<ExcelRow> rows = new ArrayList<>();
        ExcelRow row1 = mock(ExcelRow.class);
        ExcelRow row2 = mock(ExcelRow.class);
        when(row1.getItemDescription()).thenReturn("Item1");
        when(row2.getItemDescription()).thenReturn("Item2");
        rows.add(row1);
        rows.add(row2);
        List<ExcelRow> group1 = new ArrayList<>();
        List<ExcelRow> group2 = new ArrayList<>();
        itemDescriptionMappedRows.put("Item1", group1);
        itemDescriptionMappedRows.put("Item2", group2);
        reportConsolidator = new ReportConsolidator(priceReportFiles, mockExcelWriter, itemDescriptionMappedRows);
        reportConsolidator.groupExcelRowsByItemDescription(rows);
        assertEquals(1, itemDescriptionMappedRows.get("Item1").size());
        assertEquals(1, itemDescriptionMappedRows.get("Item2").size());
        assertTrue(itemDescriptionMappedRows.get("Item1").contains(row1));
        assertTrue(itemDescriptionMappedRows.get("Item2").contains(row2));
    }

}
