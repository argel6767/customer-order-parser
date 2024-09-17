package tactical.blue.services;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.parsing.excel_parsing.PriceReportParser;

public class ReportConsolidator {

    List<File> files;
    LinkedHashMap<String, List<ExcelRow>> itemDescriptionMappedRows = new LinkedHashMap<>();
    
    public ReportConsolidator(List<File> files) {
        this.files = files;
    }

    /*
     * An abstracted view of the creation of the consolidated File
     */
    public void consolidateReports() {
        groupExcelRowsBySearchQuery(grabAllRowsFromEveryFile());

    }

    private List<ExcelRow> grabAllRowsFromEveryFile() {
        List<ExcelRow> consolidatedRows = new ArrayList<>();
        for (File file : this.files) {
            PriceReportParser priceReportParser = new PriceReportParser(file);
            consolidatedRows.addAll(priceReportParser.parseFile());
        }
        return consolidatedRows;
    }

    private void groupExcelRowsBySearchQuery(List<ExcelRow> excelRows) {
        for (ExcelRow row: excelRows) {
            
        }
    }

    
}
