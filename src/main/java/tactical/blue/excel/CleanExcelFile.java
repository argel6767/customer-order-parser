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
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.HenryScheinExcelRow;

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

    public void setExcelRows(List<ExcelRow> excelRows) {
        this.excelRows = excelRows;
    }

    public void makeNewExcelFile() {
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
                if (currOctoArray.length >= 7) { // Ensure the array has enough columns
                    String octoUrl = currOctoArray[1]; //url of item in octoparse file
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
                if (currentRow != null) { //checks if valid row, will be not if not
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
            case "HenrySchein":
                return henryScheinExcelRow(currItemArray, octoparseMap);
            case "Boundtree":
                return boundTreeExcelRow(currItemArray, octoparseMap);
            default:
                throw new AssertionError();
        }
    }

    private HenryScheinExcelRow henryScheinExcelRow(String[] currItemArray, Map<String, String[]> octoparseMap) {
        if (currItemArray.length >= 5) { // Ensure the array has enough columns
            String itemUrl = currItemArray[4];
            if (octoparseMap.containsKey(itemUrl)) { // URL match found
                String[] currOctoArray = octoparseMap.get(itemUrl);
                String manufacturerInfo = StringUtils.deleteWhitespace(currOctoArray[columnHeaderIndex.get("Manufacturer")]);
                int quantity = Integer.parseInt(currItemArray[columnHeaderIndex.get("Quantity")].trim());
                String packaging = currOctoArray[columnHeaderIndex.get("Packaging")];
                double msrp = Double.parseDouble(currOctoArray[columnHeaderIndex.get("MSRP")].trim());
                double wholesalePrice = Double.parseDouble(currOctoArray[columnHeaderIndex.get("Price")]);

                return new HenryScheinExcelRow(currItemArray[2], manufacturerInfo, quantity, packaging, msrp, wholesalePrice, itemUrl);
            }
            
        }
        return null; //will return null if line is not valid
    }

    private ExcelRow boundTreeExcelRow(String[] currOctoArray, Map<String, String[]> octoparseMap) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //reads lines from csvRows and puts them into a new excel file
    public void createExcelCells() {
        this.workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("weekly price report");

        Map<String, Object[]> dataSheetInfo = new TreeMap<>();
        dataSheetInfo.put("1", new Object[] {"Item", "Product Number", "Quantity", "MSRP", "Wholesale Price", "ProductURL"}); //headers
        
        int index = 2;
        //adds the excel rows into dataSheetInfo from List excelRows
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
                    System.out.print(obj + " ");
                    if (obj instanceof Double) 
                        cell.setCellValue((Double)obj); 
    
                    else if (obj instanceof Integer) 
                        cell.setCellValue((Integer) obj); 
                    else cell.setCellValue((String)obj);
                } 
            } 
        }

        private void generateExcelFile() {
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
        }

        }

}
