package tactical.blue.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.parsing.csv_parsing.CustomerOrderInformationCSVParser;
import tactical.blue.parsing.excel_parsing.ExcelWriter;
import tactical.blue.parsing.excel_parsing.PriceReportParser;

public class ReportConsolidator {

    private List<File> priceReportFiles;
    private LinkedHashMap<String, List<ExcelRow>> itemDescriptionMappedRows = new LinkedHashMap<>();
    private ExcelWriter excelWriter = new ExcelWriter();
    CustomerOrderInformationCSVParser customerOrderInformationParser = new CustomerOrderInformationCSVParser();

    
    public ReportConsolidator(List<File> priceReportFiles, File customerOrderInfo) {
        this.priceReportFiles = priceReportFiles;
        this.itemDescriptionMappedRows = customerOrderInformationParser.getItemDescriptions(customerOrderInfo);

    }

    /*
     * An abstracted view of the creation of the consolidated File
     */
    public void consolidateReports() {
        groupExcelRowsByItemDescription(grabAllRowsFromEveryFile());
        excelWriter.createExcelCells(sortGroupedLists(), "Combined Weekly Report");
        excelWriter.generateExcelFile("Combined-Weekly-Report-");

    }

    /*
     * Grabs all ExcelRows from each file and combines them into one singular list
     */
    private List<ExcelRow> grabAllRowsFromEveryFile() {
        List<ExcelRow> consolidatedRows = new ArrayList<>();
        for (File file : this.priceReportFiles) {
            PriceReportParser priceReportParser = new PriceReportParser(file);
            consolidatedRows.addAll(priceReportParser.parseFile());
        }
        return consolidatedRows;
    }


    /*
     * Groups ExcelRows by Item Description and keeps original order due to LinkedHashMap
     */
    private void groupExcelRowsByItemDescription(List<ExcelRow> excelRows) {
        for (ExcelRow row: excelRows) {
            List<ExcelRow> excelRowGroup = itemDescriptionMappedRows.get(row.getItemDescription());
            if (excelRowGroup != null) {
                excelRowGroup.add(row); 
            }
        }
    }

    /*
     * Grabs All List values from the LinkedHashMap then sorts each group by wholesale
     * then combines all the lists into one
     */
    private List<ExcelRow> sortGroupedLists() {
        Comparator<ExcelRow> comparator = Comparator.comparing(
            excelRow -> excelRow.getWholeSalePrice() != null ? excelRow.getWholeSalePrice() : Double.MAX_VALUE
        ); //Will check if wholesale price is null ie is part of an ItemNotFoundExcelRow Object
        List<List<ExcelRow>> allGroups = new ArrayList<>(this.itemDescriptionMappedRows.values());
        for (List<ExcelRow> excelRows : allGroups) {
            if (excelRows.size() != 0) {
                excelRows.sort(comparator);
            }  
        }

        List<ExcelRow> sortedRows = new ArrayList<>();
        for (List<ExcelRow> excelRows : allGroups) {
            sortedRows.addAll(excelRows);
        }

        return sortedRows;
    }

}
