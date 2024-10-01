package tactical.blue.parsing.excel_parsing;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tactical.blue.excel.excelrows.ExcelRow;

public class ExcelWriter {
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private CellStyle cellStyle = workbook.createCellStyle();
    private boolean applyStyling = false;

    public ExcelWriter() {}

    /*
     * creates the necesssary cells from the ExcelRows that will be written into the ExcelFile
     */
      public void createExcelCells(List<ExcelRow> excelRows, String sheetTitle) {
        System.out.println("createExcelCells() called");
        
        XSSFSheet sheet = workbook.createSheet(sheetTitle + " " + LocalDate.now());

        Map<String, Object[]> dataSheetInfo = new LinkedHashMap<>(); //use LinkedHashMap to keep order
        dataSheetInfo.put("1", new Object[] {"Row","Item Description","Item", "Manfucturer", "Source", "SKU", "Packaging", "Quantity", "MSRP", "Wholesale Price", "Cost of Goods", "Markup", "Unit Price", "Extended Price", "Contribution", "Product URL"}); //headers
        
        int index = 2;
        //adds the excel rows into dataSheetInfo from List excelRows
        System.out.println("Grabbing rows...");
        for (ExcelRow excelRow : excelRows) {
            dataSheetInfo.put(String.valueOf(index), excelRow.toArray());
            index++;
        }

        
        if (applyStyling) {
            addBestDealStyling();
        }

        int rowNum = 0;
        String currItem = "";
        String prevItem = "";
        Set<String> keySet = dataSheetInfo.keySet();
            for (String key : keySet) { 
  
            // Creating a new row in the sheet 
                Row row = sheet.createRow(rowNum); 
                rowNum++;
                Object[] objArr = dataSheetInfo.get(key); 
                currItem = String.valueOf(objArr[1]);
                if (!currItem.equals(prevItem)) {

                }
                int cellnum = 0; 
    
                writeRows(row, objArr, cellnum); 
            } 
        }

    private void writeRows(Row row, Object[] objArr, int cellnum) {
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

    /*
     * Creates an Excel File in Desktop/Blue-Tactical/weekly-scrapes currently
     */
    public void generateExcelFile(String fileTitle) {
            System.out.println("generateExcelFile() Called");
            String userHome = System.getProperty("user.home");
            Path folderPath = Paths.get(userHome, "Desktop", "Weekly-Reports");
            if (!Files.exists(folderPath)) {
                try {
                    Files.createDirectories(folderPath);
                } catch (Exception e) {
                    System.out.println("Creation of folder path: " + folderPath + ", failed!");
                }
            }
        try {
            Path filePath = folderPath.resolve(fileTitle + LocalDate.now() + ".xlsx");
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

    private void addBestDealStyling() {
        this.cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
    }

}
