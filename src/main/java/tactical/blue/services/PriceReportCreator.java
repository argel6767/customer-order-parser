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

import com.opencsv.CSVParser;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.parsing.csv_parsing.CustomerOrderInformationCSVParser;
import tactical.blue.parsing.csv_parsing.ScrapedDataCSVParser;
import tactical.blue.parsing.excel_parsing.ExcelWriter;
import tactical.blue.parsing.row_parsing.*;

public class PriceReportCreator{
    private File fileInWebScrape;
    private File fileInCustomerOrderInfo;
    private String siteName;
    private BufferedReader bufferedReaderWebScrape;
    private BufferedReader bufferedReaderItemDescription;
    private List<ExcelRow> excelRows = new ArrayList<>();
    private HashMap<String,Integer> columnHeaderIndex = new HashMap<>();
    private ScrapedDataCSVParser scrapedDataCSVParser = new ScrapedDataCSVParser();
    private RowParser csvParser; //strategy pattern
    private ExcelWriter excelWriter = new ExcelWriter();
   
    
    /*
     * constructor that sets up bufferedreader object, used when path is put in constrcutor as String
     */
    public PriceReportCreator(String fileInOctoparsePath, String fileInCustomerOrderInfo, String siteName) {
        this.fileInWebScrape = new File(fileInOctoparsePath);
        this.fileInCustomerOrderInfo = new File(fileInCustomerOrderInfo);
        this.siteName = siteName;
        setCSVParser(siteName);
        try {
        this.bufferedReaderWebScrape = new BufferedReader(new FileReader(fileInWebScrape));
        this.bufferedReaderItemDescription = new BufferedReader(new FileReader(fileInCustomerOrderInfo));
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }
    }

    /*
     * Constructor used when File objects are given
     */
    public PriceReportCreator(File fileInOctoparsePath, File fileInCustomerOrderInfo, String siteName) {
        this.fileInWebScrape = fileInOctoparsePath;
        this.fileInCustomerOrderInfo = fileInCustomerOrderInfo;
        this.siteName = siteName;
        setCSVParser(siteName);
        try {
        this.bufferedReaderWebScrape = new BufferedReader(new FileReader(fileInOctoparsePath));
        this.bufferedReaderItemDescription = new BufferedReader(new FileReader(fileInCustomerOrderInfo));
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }
    }

    /*
     * Testing purposes
     */
    public PriceReportCreator() {
    }

     /*
     * Abstracted overview of how a Excel file is created using the scraped data and order info given by customer
     */
    public void makeNewExcelFile() {
        System.out.println("makeNewExcelFile() called");
        try {
            readCSVFiles();
            //method();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        excelWriter.createExcelCells(excelRows, "Weekly Customer Price Report for" + this.siteName);
        excelWriter.generateExcelFile(siteName + "-Report-");
    }

    /*
     * Depending on what site the data is sourced the CSVParser object will be instantiated as the appropriate
     * strategy
     */
    private void setCSVParser(String siteName) {
        switch (siteName) {
            case "Bound Tree":
                this.csvParser = new BoundTreeRowParser();
                break;
            case "Henry Schein":
                this.csvParser = new HenryScheinRowParser();
                break;
            case "Medco":
                this.csvParser = new MedcoRowParser();
                break;
            case "NA Rescue":
                this.csvParser = new NARescueRowParser();
                break;
            default:
                System.out.println("Not a valid option!");
        }
    }

    public void setExcelRows(List<ExcelRow> excelRows) {
        this.excelRows = excelRows;
    }


    private void method() throws IOException {
        String columnTitles = bufferedReaderWebScrape.readLine();
        getExcelColumnNames(columnTitles.split(","));
        HashMap<String, List<String[]>> webScrapedMap = mapScrapedRows();
        List<String[]> orderInfoRows = getOrderInfoRows();        
        parseScrapedRowsToExcelRows(webScrapedMap, orderInfoRows);
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

    //reads csv file that is put in
   private void readCSVFiles() throws IOException {
        System.out.println("readCSVFiles() was called");

        // Step 1: Read Web Scrape Data CSV into a map (URL -> row data)
        Map<String, List<String[]>> webScrapedMap = new HashMap<>();
            String currLineDataScrape;
            int iteration = 0;
            while ((currLineDataScrape = bufferedReaderWebScrape.readLine()) != null) {
                if (iteration == 0) { // Skip headers
                    iteration++; 
                    String[] columnHeaders = currLineDataScrape.split(",");
                    getExcelColumnNames(columnHeaders);
                    continue;
                }
                String[] currWebScrapedDataArray = currLineDataScrape.split(",");
                if (currWebScrapedDataArray.length >= 5) { // Ensure the array has enough columns
                    String productURL = currWebScrapedDataArray[currWebScrapedDataArray.length-1]; //url of item in octoparse fil
                    if (this.siteName.equals("Henry Schein")) {
                        productURL = currWebScrapedDataArray[0].replaceAll("\"", ""); //henry schein is the only with url first
                    }
                    
                    if (webScrapedMap.containsKey(productURL)) {
                        List<String[]> urValList = webScrapedMap.get(productURL);
                        urValList.add(currWebScrapedDataArray);
                    }
                    else {
                       List<String[]> urlValList = new ArrayList<>();
                       urlValList.add(currWebScrapedDataArray);
                       webScrapedMap.put(productURL, urlValList);
                    }
                    
                }
            }
        
        //TODO replace this with OrderInformationParser.getItemDescriptions() method
        // Step 2: Iterate over Item Description CSV and match URLs
            String currLineItemDescription;
            iteration = 0;
            while ((currLineItemDescription = bufferedReaderItemDescription.readLine()) != null) { //getting current row of file
                if (iteration == 0) { // Skip headers
                    iteration++;
                    continue;
                }
                String[] currItemArray = currLineItemDescription.split(",");
                List<ExcelRow> currentRows = parseRowsForExcelFile(currItemArray, webScrapedMap);
                if (currentRows != null) { //checks if valid rows, will be null if not
                    this.excelRows.addAll(currentRows);
                }
            }
        
    }

    /*
     * Grabs the column headers from scraped data to have dynamic indexes
     */
    private void getExcelColumnNames(String[] columnHeaders) { //allows for dynamic indexes of headers, should they be in an unexpected order
        for (int i = 0; i < columnHeaders.length; i++) {
            columnHeaderIndex.put(columnHeaders[i], i);
        }
    }

    /*
     * calls the CSVParser parseRow() object to parse scraped data and have them formatted for the Excel table
     */
    private List<ExcelRow> parseRowsForExcelFile(String[] currItemArray, Map<String, List<String[]>> webScrapedMap) {
        return csvParser.parseRow(currItemArray, webScrapedMap, columnHeaderIndex);
    }

}
