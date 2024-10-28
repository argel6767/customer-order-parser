package tactical.blue.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import tactical.blue.async.AsyncPriceReportManager;
import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.parsing.csv_parsing.ScrapedDataCSVParser;
import tactical.blue.parsing.excel_parsing.ExcelWriter;
import tactical.blue.parsing.row_parsing.BoundTreeRowParser;
import tactical.blue.parsing.row_parsing.HenryScheinRowParser;
import tactical.blue.parsing.row_parsing.MedcoRowParser;
import tactical.blue.parsing.row_parsing.NARescueRowParser;
import tactical.blue.parsing.row_parsing.RowParser;

public final class PriceReportCreator{
    private File fileInWebScrape;
    private File fileInCustomerOrderInfo;
    private String siteName;
    private BufferedReader bufferedReaderWebScrape;
    private List<ExcelRow> excelRows = new ArrayList<>();
    private final HashMap<String,Integer> columnHeaderIndex = new HashMap<>();
    private final ScrapedDataCSVParser scrapedDataCSVParser = new ScrapedDataCSVParser();
    private RowParser rowParser; //strategy pattern
    private final ExcelWriter excelWriter = new ExcelWriter();
   
    /*
     * Constructor that is called by UI
     */
    public PriceReportCreator(File fileInWebScrape, File fileInCustomerOrderInfo, String siteName) {
        this.fileInWebScrape = fileInWebScrape;
        this.fileInCustomerOrderInfo = fileInCustomerOrderInfo;
        this.siteName = siteName;
        setRowParser(siteName);
        createBufferedReader(fileInWebScrape);

    }

    public void createBufferedReader(File file) {
        try {
            this.bufferedReaderWebScrape = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException fnfe) {
            System.out.println("File could not be found! Try again");
        }
    }

    /*
     * Testing purposes
     */
    public PriceReportCreator() {
    }

     /*
     * Abstracted overview of how an Excel file is created using the scraped data and order info given by customer
     */
    public void makeNewExcelFile() {
        System.out.println("makeNewExcelFile() called");
        runParsingAndWriting();
    }

    /*
     * holds the try/catch logic
     * readCSVFiles() called, then once the CSV files are done then the Excel write
     * creates the Excel file
     */
    private CompletableFuture<Void> runParsingAndWriting() {
        try {
           var filesParsed = readCSVFiles();
           return filesParsed.thenRun( () -> {
               excelWriter.createExcelCells(excelRows, "Weekly Customer Price Report for" + this.siteName);
               excelWriter.generateExcelFile(siteName + "-Report-");
               ExcelRow.resetRowNumber();
           });
        } catch (IOException ioe) {
            System.out.println("Something went wrong! Check the Exception Stack");
            ioe.printStackTrace();
            return CompletableFuture.failedFuture(ioe);
        }
    }

    /*
     * Depending on what site the data is sourced the RowParser object will be instantiated as the appropriate
     * strategy
     */
    void setRowParser(String siteName) {
        switch (siteName) {
            case "Bound Tree":
                this.rowParser = new BoundTreeRowParser();
                break;
            case "Henry Schein":
                this.rowParser = new HenryScheinRowParser();
                break;
            case "Medco":
                this.rowParser = new MedcoRowParser();
                break;
            case "NA Rescue":
                this.rowParser = new NARescueRowParser();
                break;
            default:
                System.out.println("Not a valid option!");
                this.rowParser = null;
        }
    }

    public void setExcelRows(List<ExcelRow> excelRows) {
        this.excelRows = excelRows;
    }

    /*
     * An abstracted view of readCSVFiles
     * the column titles are found for dynamic grabbing of values
     * the rows are then red and mapped by their product url
     * the Order Info file is read
     * The rows are then converted to a proper format for Excel Rows and written to Excel File
     */
    private CompletableFuture<?> readCSVFiles() throws IOException {
        String columnTitles = bufferedReaderWebScrape.readLine();
        getExcelColumnNames(columnTitles.split(","));
        return grabCSVRowsConcurrently();
    }

    /*
     * declares and instantiates an AsyncPriceReportManager and runs
     * mapScrapedRows() and getOrderInfoRows() on their own threads to run them concurrently
     * then uses values created by these methods in parseScrapedRowsToExcelRows()
     * then shuts down the ExecutorService in AsyncPriceReportManager
     */
    private CompletableFuture<?> grabCSVRowsConcurrently() {
        AsyncPriceReportManager<Void> manager = new AsyncPriceReportManager<>();
        var done = manager.runCSVParsingConcurrently(this::mapScrapedRows, this::getOrderInfoRows, this::parseScrapedRowsToExcelRows);
        done.thenRun(() -> {
            manager.shutdown();
            System.out.println("Done! Shutting down ExecutorService");
        });
        return done;

    }

    /*
     * Calls scrapedDataCSVParser.mapRows()
     */
    private HashMap<String, List<String[]>> mapScrapedRows() {
        return scrapedDataCSVParser.mapRows(fileInWebScrape, siteName);
    }

    /*
     * Calls scrapedDataCSVParser.getRows()
     */
    private List<String[]> getOrderInfoRows() {
        return scrapedDataCSVParser.getCSVRows(fileInCustomerOrderInfo);
    }

    /*
     * Creates ExcelRow objects, by calling parseRowsForExcelFiles
     * then adds the returned objects to the entire list excelRows if not null
     */
    private void parseScrapedRowsToExcelRows(HashMap<String, List<String[]>> webScrapedMap, List<String[]> orderInfoRows) {
        for (String[] row: orderInfoRows) {
            List<ExcelRow> currentRows = parseRowsForExcelFile(row, webScrapedMap);
            if (currentRows != null) { //checks if valid rows, will be null if not
                this.excelRows.addAll(currentRows);
            }
        }
    }

    /*
     * Grabs the column headers from scraped data to have dynamic indexes
     */
    void getExcelColumnNames(String[] columnHeaders) { //allows for dynamic indexes of headers, should they be in an unexpected order
        for (int i = 0; i < columnHeaders.length; i++) {
            columnHeaderIndex.put(columnHeaders[i], i);
        }
    }

    /*
     * calls the CSVParser parseRow() object to parse scraped data and have them formatted for the Excel table
     */
    List<ExcelRow> parseRowsForExcelFile(String[] currItemArray, Map<String, List<String[]>> webScrapedMap) {
        return rowParser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);
    }

    public List<ExcelRow> getExcelRows() {
        return excelRows;
    }

    public File getFileInWebScrape() {
        return fileInWebScrape;
    }

    public void setFileInWebScrape(File fileInWebScrape) {
        this.fileInWebScrape = fileInWebScrape;
    }

    public File getFileInCustomerOrderInfo() {
        return fileInCustomerOrderInfo;
    }

    public void setFileInCustomerOrderInfo(File fileInCustomerOrderInfo) {
        this.fileInCustomerOrderInfo = fileInCustomerOrderInfo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public BufferedReader getBufferedReaderWebScrape() {
        return bufferedReaderWebScrape;
    }

    public void setBufferedReaderWebScrape(BufferedReader bufferedReaderWebScrape) {
        this.bufferedReaderWebScrape = bufferedReaderWebScrape;
    }

    public HashMap<String, Integer> getColumnHeaderIndex() {
        return columnHeaderIndex;
    }

    public ScrapedDataCSVParser getScrapedDataCSVParser() {
        return scrapedDataCSVParser;
    }

    public RowParser getRowParser() {
        return rowParser;
    }

    public void setRowParser(RowParser rowParser) {
        this.rowParser = rowParser;
    }

    public ExcelWriter getExcelWriter() {
        return excelWriter;
    }
}
