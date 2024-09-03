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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tactical.blue.excel.excelrows.BoundTreeExcelRow;
import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.HenryScheinExcelRow;
import tactical.blue.excel.excelrows.MedcoSportsMedicineExcelRow;

public class CleanExcelFile {
    private File fileInOctoparse;
    private File fileInItemDescription;
    private String citeName;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedReader bufferedReaderOcto;
    private BufferedReader bufferedReaderItemDescription;
    private List<ExcelRow> excelRows = new ArrayList<>();
    private XSSFWorkbook workbook;
    private HashMap<String,Integer> columnHeaderIndex = new HashMap<>();
   
    //constructor that sets up bufferedreader object
    public CleanExcelFile(String fileInOctoparsePath, String fileInItemDescription, String citeName) {
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
     * Testing purposes
     */
    public CleanExcelFile() {
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
        Map<String, String[]> octoparseMap = new HashMap<>();
            String currLineOctoparse;
            int iteration = 0;
            while ((currLineOctoparse = bufferedReaderOcto.readLine()) != null) {
                if (iteration == 0) { // Skip headers
                    iteration++; 
                    String[] columnHeaders = currLineOctoparse.split(",");
                    getExcelColumnNames(columnHeaders);
                    continue;
                }
                String[] currOctoArray = currLineOctoparse.split(",");
                if (currOctoArray.length >= 5) { // Ensure the array has enough columns
                    String octoUrl = currOctoArray[currOctoArray.length-1]; //url of item in octoparse file
                    octoparseMap.put(octoUrl, currOctoArray);
                    System.out.println(octoUrl);
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
                ExcelRow currentRow = parseRowBasedOnSite(currItemArray, octoparseMap);
                if (currentRow != null) { //checks if valid row, will be null if not
                    this.excelRows.add(currentRow);
                }
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
    private ExcelRow parseRowBasedOnSite(String[] currItemArray, Map<String, String[]> octoparseMap) {
        switch (this.citeName) {
            case "Henry Schein":
                return henryScheinExcelRow(currItemArray, octoparseMap);
            case "Bound Tree":
                return boundTreeExcelRow(currItemArray, octoparseMap);
            case "Medco":
                return medcoExcelRow(currItemArray, octoparseMap);
            case "NARescue":
                return naRescueExcelRow(currItemArray, octoparseMap);
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
    private HenryScheinExcelRow henryScheinExcelRow(String[] currItemArray, Map<String, String[]> octoparseMap) {
        if (currItemArray.length >= 5) { // Ensure the array has enough columns
            String itemUrl = currItemArray[3];
            if (octoparseMap.containsKey(itemUrl)) { // URL match found
                String[] currOctoArray = octoparseMap.get(itemUrl);
                String productName = currOctoArray[columnHeaderIndex.get("Product")];
                String manufacturerInfo = StringUtils.deleteWhitespace(currOctoArray[columnHeaderIndex.get("Manufacturer")]);
                int quantity = Integer.parseInt(currItemArray[1].trim());
                String packaging = currOctoArray[columnHeaderIndex.get("Packaging")];
                double msrp = Double.parseDouble(currOctoArray[columnHeaderIndex.get("MSRP")].trim());
                double wholesalePrice = Double.parseDouble(currOctoArray[columnHeaderIndex.get("Price")]);

                return new HenryScheinExcelRow(productName, manufacturerInfo, quantity, packaging, msrp, wholesalePrice, itemUrl);
            }
            
        }
        return null; //will return null if line is not valid
    }

   


    private BoundTreeExcelRow boundTreeExcelRow(String[] currOctoArray, Map<String, String[]> octoparseMap) {
        return null;
    }

    private MedcoSportsMedicineExcelRow medcoExcelRow(String[] currItemArray, Map<String, String[]> octoparseMap) {
        if (currItemArray.length >= 3) {
        String itemUrl = currItemArray[2];
        if (octoparseMap.containsKey(itemUrl)) {
            String[] currOctoArray = octoparseMap.get(itemUrl);
            String customerDescription = currItemArray[0];
            String itemName = currOctoArray[columnHeaderIndex.get("Product")];
            String manufacturer = currOctoArray[columnHeaderIndex.get("Manufacturer")];
            String sku = currOctoArray[columnHeaderIndex.get("SKU")];
            int quantityRequested = Integer.parseInt(currItemArray[1]);
            //Medco Does not include MSRP
            Double msrp = 0.0;
            double wholesalePrice = Double.parseDouble(currOctoArray[columnHeaderIndex.get("Wholesale")]);
            
            return new MedcoSportsMedicineExcelRow(customerDescription, itemName, manufacturer, sku, quantityRequested, msrp, wholesalePrice, itemUrl);
        }

        }
        return null; //will return null if line is not valid

    }

    private ExcelRow naRescueExcelRow(String[] currItemArray, Map<String, String[]> octoparseMap) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'medcoExcelRow'");
    }

    //reads lines from csvRows and puts them into a new excel file
    public void createExcelCells() {
        System.out.println("createExcelCells() called");
        
        this.workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("weekly price report for " + this.citeName + " " + LocalDate.now());

        Map<String, Object[]> dataSheetInfo = new HashMap<>();
        dataSheetInfo.put("1", new Object[] {"Row","Item", "Manfucturer", "Source", "SKU", "Packaging", "Quantity", "MSRP", "Wholesale Price", "Cost of Goods", "Markup", "Unit Price", "Extended Price", "Contribution", "Product URL"}); //headers
        
        int index = 2;
        //adds the excel rows into dataSheetInfo from List excelRows
        for (ExcelRow excelRow : excelRows) {
            System.out.println(excelRow.toString());
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
                    System.out.print(obj + " ");
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
            File directory = new File("../" + DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir();
            }
        try {
            FileOutputStream excelOutput = new FileOutputStream(new File(directory + "/" + String.valueOf(LocalDate.now()) + ".xlsx"));
            this.workbook.write(excelOutput);
            excelOutput.close();
        } catch (Exception e) {
            System.out.println("FILE NOT FOUND!");
            e.printStackTrace();
        }

        }

        public static void main(String[] args) {
            CleanExcelFile cleanExcelFile = new CleanExcelFile("/Users/argelhernandezamaya/Desktop/Blue-Tactical/product-orders/Medco-Sports-Medicine-Scrape.csv", "/Users/argelhernandezamaya/Desktop/Blue-Tactical/product-orders/Medco-order-example.csv", "Medco");
            cleanExcelFile.makeNewExcelFile();
        }

}
