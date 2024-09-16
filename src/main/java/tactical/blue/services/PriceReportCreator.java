package tactical.blue.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.parsing.csv_parsing.*;

public class PriceReportCreator{
    private File fileInWebScrape;
    private File fileInCustomerOrderInfo;
    private String siteName;
    private BufferedReader bufferedReaderWebScrape;
    private BufferedReader bufferedReaderItemDescription;
    private List<ExcelRow> excelRows = new ArrayList<>();
    private XSSFWorkbook workbook;
    private HashMap<String,Integer> columnHeaderIndex = new HashMap<>();
    private CSVParser csvParser; //strategy pattern
   
    
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
     * Depending on what site the data is sourced the CSVParser object will be instantiated as the appropriate
     * strategy
     */
    private void setCSVParser(String siteName) {
        switch (siteName) {
            case "Bound Tree":
                this.csvParser = new BoundTreeCSVParser();
                break;
            case "Henry Schein":
                this.csvParser = new HenryScheinCSVParser();
                break;
            case "Medco":
                this.csvParser = new MedcoCSVParser();
                break;
            case "NA Rescue":
                this.csvParser = new NARescueParser();
                break;
            default:
                System.out.println("Not a valid option!");
        }
    }

    public void setExcelRows(List<ExcelRow> excelRows) {
        this.excelRows = excelRows;
    }

    /*
     * Abstracted overview of how a Excel file is created using the scraped data and order info given by customer
     */
    public void makeNewExcelFile() {
        System.out.println("makeNewExcelFile() called");
        try {
            readCSVFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        createExcelCells();
        generateExcelFile();
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
                Collections.sort(excelRows, new Comparator<ExcelRow>() {
                    @Override
                    public int compare(ExcelRow row1, ExcelRow row2) {
                        return row1.getProductURL().compareTo(row2.getProductURL());
                    }
                });
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


    //reads lines from csvRows and puts them into a new excel file
    public void createExcelCells() {
        System.out.println("createExcelCells() called");
        
        this.workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("weekly price report for " + this.siteName + " " + LocalDate.now());

        Map<String, Object[]> dataSheetInfo = new LinkedHashMap<>(); //use LinkedHashMap to keep order
        dataSheetInfo.put("1", new Object[] {"Row","Item", "Manfucturer", "Source", "SKU", "Packaging", "Quantity", "MSRP", "Wholesale Price", "Cost of Goods", "Markup", "Unit Price", "Extended Price", "Contribution", "Product URL"}); //headers
        
        int index = 2;
        //adds the excel rows into dataSheetInfo from List excelRows
        System.out.println("Grabbing rows...");
        for (ExcelRow excelRow : excelRows) {
            dataSheetInfo.put(String.valueOf(index), excelRow.toArray());
            index++;
        }

        

        int rowNum = 0;
        Set<String> keySet = dataSheetInfo.keySet();
            for (String key : keySet) { 
  
            // Creating a new row in the sheet 
                Row row = sheet.createRow(rowNum); 
                rowNum++;
                Object[] objArr = dataSheetInfo.get(key); 
                
                int cellnum = 0; 
    
                for (Object obj : objArr) { 
                    // This line creates a cell in the next 
                    //  column of that row 
                    Cell cell = row.createCell(cellnum++); 
                    
                    if (obj == null) {
                        cell.setCellValue("N/A");
                    }
                    if (obj instanceof Double) 
                        cell.setCellValue((Double)obj); 
    
                    else if (obj instanceof Integer) 
                        cell.setCellValue((Integer) obj); 
                    else cell.setCellValue((String)obj);
                } 
            } 
        }

        private void generateExcelFile() {
            System.out.println("generateExcelFile() Called");
            String userHome = System.getProperty("user.home");
            Path folderPath = Paths.get(userHome, "Desktop", "Blue-Tactical", "weekly-scrapes");
            if (!Files.exists(folderPath)) {
                try {
                    Files.createDirectories(folderPath);
                } catch (Exception e) {
                    System.out.println("Creation of folder path: " + folderPath + ", failed!");
                }
            }
        try {
            Path filePath = folderPath.resolve(siteName + "-Report-" + LocalDate.now() + ".xlsx");
             // Open a FileOutputStream using the Path to write the Excel file
            try (FileOutputStream excelOutput = new FileOutputStream(filePath.toFile())) {
                    this.workbook.write(excelOutput);
                    excelOutput.close();
                }
        } catch (Exception e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }

        }

        public static void main(String[] args) {
            PriceReportCreator cleanExcelFile = new PriceReportCreator("/Users/argelhernandezamaya/Desktop/Blue-Tactical/product-orders/Henry-Schein-Scrape.csv", "/Users/argelhernandezamaya/Desktop/Blue-Tactical/product-orders/Henry-Shein-Order-Example.csv", "Henry Schein");
            cleanExcelFile.makeNewExcelFile();
        }

}
