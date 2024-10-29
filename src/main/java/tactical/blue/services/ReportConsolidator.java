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

    private final List<File> priceReportFiles;
    private LinkedHashMap<String, List<ExcelRow>> itemDescriptionMappedRows = new LinkedHashMap<>();
    private final ExcelWriter excelWriter;


    
    public ReportConsolidator(List<File> priceReportFiles, File customerOrderInfo) {
        CustomerOrderInformationCSVParser parser = new CustomerOrderInformationCSVParser();
        this.priceReportFiles = priceReportFiles;
        this.itemDescriptionMappedRows = parser.getItemDescriptions(customerOrderInfo);
        excelWriter = new ExcelWriter();

    }

    /*
    Testing constructor for mock injection
     */
    public ReportConsolidator(List<File> priceReportFiles, ExcelWriter excelWriter, LinkedHashMap<String, List<ExcelRow>> itemDescriptionMappedRows) {
        this.priceReportFiles = priceReportFiles;
        this.itemDescriptionMappedRows = itemDescriptionMappedRows;
        this.excelWriter = excelWriter;
    }

    /*
     * An abstracted view of the creation of the consolidated File
     */
    public void consolidateReports() {
        groupExcelRowsByItemDescription(grabAllRowsFromEveryFile());
        excelWriter.allowStyling();
        excelWriter.createExcelCells(sortGroupedLists(), "Combined Weekly Report");
        excelWriter.generateExcelFile("Combined-Weekly-Report-");

    }

    /*
     * Grabs all ExcelRows from each file and combines them into one singular list
     */
    List<ExcelRow> grabAllRowsFromEveryFile() {
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
    void groupExcelRowsByItemDescription(List<ExcelRow> excelRows) {
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
    List<ExcelRow> sortGroupedLists() {
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
            if (excelRows.size() != 0) {
                ExcelRow bestDeal = excelRows.get(0);
                bestDeal.setIsFirstGroupItem(); //flags each best deal
            }
            sortedRows.addAll(excelRows);
        }

        return sortedRows;
    }

}
