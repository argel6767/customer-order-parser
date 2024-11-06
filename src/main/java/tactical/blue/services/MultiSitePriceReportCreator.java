package tactical.blue.services;

import tactical.blue.excel.excelrows.ExcelRow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiSitePriceReportCreator {

    /*
     * TODO Find a way to match the items, possible use the search query of the url, as they will be the same.
     *  Use the CSVReaders, need to read the documents, customer reader that overrides from the from url to url-search query
     *  could possibly then use ReportConsolidator making a new method that takes in a list and organizes them uses the
     *  the rest of the report consolidation logic to make an Excel file
     *
     */

    private List<File> scrapedDataFiles = new ArrayList<>();
    private File customerOrderInfoFile;
    private List<ExcelRow> rows = new ArrayList<>();


    public MultiSitePriceReportCreator(List<File> scrapedDataFiles, File customerOrderInfoFile) {
        this.scrapedDataFiles = scrapedDataFiles;
        this.customerOrderInfoFile = customerOrderInfoFile;
    }

    /*
     * empty constructor for testing
     */
    public MultiSitePriceReportCreator() {}


    private void grabRows() {

    }

    public List<File> getScrapedDataFiles() {
        return scrapedDataFiles;
    }

    public void setScrapedDataFiles(List<File> scrapedDataFiles) {
        this.scrapedDataFiles = scrapedDataFiles;
    }

    public File getCustomerOrderInfoFile() {
        return customerOrderInfoFile;
    }

    public void setCustomerOrderInfoFile(File customerOrderInfoFile) {
        this.customerOrderInfoFile = customerOrderInfoFile;
    }

    public List<ExcelRow> getRows() {
        return rows;
    }

    public void setRows(List<ExcelRow> rows) {
        this.rows = rows;
    }


}
