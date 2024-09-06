package tactical.blue.excel;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tactical.blue.excel.excelrows.BoundTreeExcelRow;
import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.HenryScheinExcelRow;
import tactical.blue.excel.excelrows.MedcoSportsMedicineExcelRow;

public class PriceReportCreator{
    private File fileInOctoparse;
    private File fileInItemDescription;
    private String citeName;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedReader bufferedReaderOcto;
    private BufferedReader bufferedReaderItemDescription;
    private List<ExcelRow> excelRows = new ArrayList<>();
    private XSSFWorkbook workbook;
    private HashMap<String,Integer> columnHeaderIndex = new HashMap<>();
   
    
    /*
     * constructor that sets up bufferedreader object, used when path is put in constrcutor as String
     */
    public PriceReportCreator(String fileInOctoparsePath, String fileInItemDescription, String citeName) {
        this.fileInOctoparse = new File(fileInOctoparsePath);
        this.fileInItemDescription = new File(fileInItemDescription);
        this.citeName = citeName;
        try {
        this.bufferedReaderOcto = new BufferedReader(new FileReader(fileInOctoparse));
        this.bufferedReaderItemDescription = new BufferedReader(new FileReader(fileInItemDescription));
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }
    }

    /*
     * Constructor used when File objects are given
     */
    public PriceReportCreator(File fileInOctoparsePath, File fileInItemDescription, String citeName) {
        this.citeName = citeName;
        try {
        this.bufferedReaderOcto = new BufferedReader(new FileReader(fileInOctoparsePath));
        this.bufferedReaderItemDescription = new BufferedReader(new FileReader(fileInItemDescription));
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }
    }
    /*
     * Testing purposes
     */
    public PriceReportCreator() {
    }

    public void setExcelRows(List<ExcelRow> excelRows) {
        this.excelRows = excelRows;
    }

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

        // Step 1: Read Octoparse CSV into a map (URL -> row data)
        Map<String, List<String[]>> webScrapedMap = new HashMap<>();
            String currLineOctoparse;
            int iteration = 0;
            while ((currLineOctoparse = bufferedReaderOcto.readLine()) != null) {
                if (iteration == 0) { // Skip headers
                    iteration++; 
                    String[] columnHeaders = currLineOctoparse.split(",");
                    getExcelColumnNames(columnHeaders);
                    continue;
                }
                String[] currWebScrapedDataArray = currLineOctoparse.split(",");
                if (currWebScrapedDataArray.length >= 5) { // Ensure the array has enough columns
                    String productURL = currWebScrapedDataArray[currWebScrapedDataArray.length-1]; //url of item in octoparse file
                    if (webScrapedMap.containsKey(productURL)) {
                        List<String[]> urValList = webScrapedMap.get(productURL);
                        urValList.add(currWebScrapedDataArray);
                    }
                    else {
                       List<String[]> urlValList = new ArrayList<>();
                       urlValList.add(currWebScrapedDataArray);
                       webScrapedMap.put(productURL, urlValList);
                    }
                    
                    System.out.println(productURL);
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
                List<ExcelRow> currentRows = parseRowBasedOnSite(currItemArray, webScrapedMap);
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

    private void getExcelColumnNames(String[] columnHeaders) { //allows for dynamic indexes of headers, should they be in an unexpected order
        for (int i = 0; i < columnHeaders.length; i++) {
            columnHeaderIndex.put(columnHeaders[i], i);
        }
    }

    /*
     * Determines what website the items are being sourced from to determine correct way to format rows
     * and get data
     */
    private List<ExcelRow> parseRowBasedOnSite(String[] currItemArray, Map<String, List<String[]>> webScrapedMap) {
        List<ExcelRow> rows =  new ArrayList<>();
        switch (this.citeName) {
            case "Henry Schein":
                //return henryScheinExcelRow(currItemArray, webScrapedMap);
            case "Bound Tree":
                //return boundTreeExcelRow(currItemArray, webScrapedMap);
            case "Medco":
                List<MedcoSportsMedicineExcelRow> medcoRows = medcoExcelRow(currItemArray, webScrapedMap);
                if (medcoRows!= null) {
                    rows.addAll(medcoRows);
                }
                return rows;
            case "NARescue":
                //return naRescueExcelRow(currItemArray, webScrapedMap);
            default:
                throw new AssertionError();
        }
    }

    /*
     * Customer Details Will Be This Format: Item Description, Quantity, URL
     */

    /*
     * Creates a HenryScheinExcelRow object that will become a row
     */
    private HenryScheinExcelRow henryScheinExcelRow(String[] currItemArray, Map<String, String[]> webScrapedMap) {
        if (currItemArray.length >= 5) { // Ensure the array has enough columns
            String itemUrl = currItemArray[3];
            if (webScrapedMap.containsKey(itemUrl)) { // URL match found
                String[] currWebScrapedDataArray = webScrapedMap.get(itemUrl);
                String productName = currWebScrapedDataArray[columnHeaderIndex.get("Product")];
                String manufacturerInfo = StringUtils.deleteWhitespace(currWebScrapedDataArray[columnHeaderIndex.get("Manufacturer")]);
                int quantity = Integer.parseInt(currItemArray[1].trim());
                String packaging = currWebScrapedDataArray[columnHeaderIndex.get("Packaging")];
                double msrp = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("MSRP")].trim());
                double wholesalePrice = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("Price")]);

                return new HenryScheinExcelRow(productName, manufacturerInfo, quantity, packaging, msrp, wholesalePrice, itemUrl);
            }
            
        }
        return null; //will return null if line is not valid
    }

   


    private BoundTreeExcelRow boundTreeExcelRow(String[] currWebScrapedDataArray, Map<String, String[]> webScrapedMap) {
        return null;
    }

    private List<MedcoSportsMedicineExcelRow> medcoExcelRow(String[] currItemArray, Map<String, List<String[]>> webScrapedMap) {
        List<MedcoSportsMedicineExcelRow> productRows = new ArrayList<>();
        if (currItemArray.length >= 3) {
        String itemUrl = currItemArray[2];
        if (webScrapedMap.containsKey(itemUrl)) {
            List<String[]> urlValList = webScrapedMap.get(itemUrl); //grabs all products found under url

            for (String[] currWebScrapedDataArray : urlValList) { //makes new objects for each one
                String customerDescription = currItemArray[0]; //objects with same URL with have the same customer description
                String itemName = currWebScrapedDataArray[columnHeaderIndex.get("Product")];
                String manufacturer = currWebScrapedDataArray[columnHeaderIndex.get("Manufacturer")];
                String sku = currWebScrapedDataArray[columnHeaderIndex.get("SKU")];
                int quantityRequested = Integer.parseInt(currItemArray[1]);
                //Medco Does not include MSRP
                Double msrp = 0.0;
                double wholesalePrice = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("Wholesale")]);

                productRows.add(new MedcoSportsMedicineExcelRow(customerDescription, itemName, manufacturer, sku, quantityRequested, msrp, wholesalePrice, itemUrl)); //then add current row to all the will be returned
            }
            
            return productRows;
        }

        }
        return null; //will return null if line is not valid

    }

    /*
     * TODO
     * Create a method to loop through each list element to make a new Excel row per item, mutiple items can have the same, url
     * they will all share the same currItemArray
     */

    private ExcelRow naRescueExcelRow(String[] currItemArray, Map<String, String[]> webScrapedMap) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'medcoExcelRow'");
    }

    //reads lines from csvRows and puts them into a new excel file
    public void createExcelCells() {
        System.out.println("createExcelCells() called");
        
        this.workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("weekly price report for " + this.citeName + " " + LocalDate.now());

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
            Path filePath = folderPath.resolve(citeName + "-Report-" + LocalDate.now() + ".xlsx");
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
            PriceReportCreator cleanExcelFile = new PriceReportCreator("/Users/argelhernandezamaya/Desktop/Blue-Tactical/product-orders/Medco-Sports-Medicine-Scrape.csv", "/Users/argelhernandezamaya/Desktop/Blue-Tactical/product-orders/Medco-order-example.csv", "Medco");
            cleanExcelFile.makeNewExcelFile();
        }

}
