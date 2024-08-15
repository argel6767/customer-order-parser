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

public class CleanExcelFile {
    private File fileInOctoparse;
    private File fileInItemDescription;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedReader bufferedReaderOcto;
    private BufferedReader bufferedReaderItemDescription;
    private List<ExcelRow> excelRows = new ArrayList<>();
    private XSSFWorkbook workbook;
   
    //constructor that sets up bufferedreader object
    public CleanExcelFile(String fileInOctoparsePath, String fileInItemDescription) {
        this.fileInOctoparse = new File(fileInOctoparsePath);
        this.fileInItemDescription = new File(fileInItemDescription);
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
                    continue;
                }
                String[] currOctoArray = currLineOctoparse.split(",");
                if (currOctoArray.length >= 4) { // Ensure the array has enough columns
                    String octoUrl = currOctoArray[3];
                    octoparseMap.put(octoUrl, currOctoArray);
                    System.out.println(octoUrl);
                }
            }
        

        // Step 2: Iterate over Item Description CSV and match URLs
            String currLineItemDescription;
            iteration = 0;
            while ((currLineItemDescription = bufferedReaderItemDescription.readLine()) != null) {
                if (iteration == 0) { // Skip headers
                    iteration++;
                    continue;
                }
                String[] currItemArray = currLineItemDescription.split(",");
                if (currItemArray.length >= 5) { // Ensure the array has enough columns
                    String itemUrl = currItemArray[4];
                    if (octoparseMap.containsKey(itemUrl)) { // URL match found
                        String[] currOctoArray = octoparseMap.get(itemUrl);
                        String productNumber = StringUtils.deleteWhitespace(currOctoArray[1]);
                        int quantity = Integer.parseInt(currItemArray[2].trim());
                        double msrp = Double.parseDouble(currOctoArray[2].trim());
                        double wholesalePrice = msrp * quantity * 0.7;

                        ExcelRow currRow = new ExcelRow(currItemArray[0], productNumber, quantity, msrp, wholesalePrice, itemUrl);
                        this.excelRows.add(currRow);
                    }
                }
            }
        
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
