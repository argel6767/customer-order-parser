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
import org.apache.poi.ss.usermodel.FillPatternType;
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
     * allows for the the applyStyling flag to become true
     * this is typically only done by the Price Consolidator
     */
    public void allowStyling() {
        applyStyling = true;
    }

    /*
     * creates the necesssary cells from the ExcelRows that will be written into the ExcelFile
     */
      public void createExcelCells(List<ExcelRow> excelRows, String sheetTitle) {
        System.out.println("createExcelCells() called");
        
        XSSFSheet sheet = workbook.createSheet(sheetTitle + " " + LocalDate.now());

        Map<String, Object[]> dataSheetInfo = new LinkedHashMap<>(); //use LinkedHashMap to keep order
        dataSheetInfo.put("1", new Object[] {"Row","Item Description","Item", "Manfucturer", "Source", "SKU", "Packaging", "Quantity", "MSRP", "Wholesale Price", "Cost of Goods", "Markup", "Unit Price", "Extended Price", "Contribution", "Product URL", ""}); //headers, empty string to allow for Product URL to be written
        
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
        Set<String> keySet = dataSheetInfo.keySet();
            for (String key : keySet) { 
  
            // Creating a new row in the sheet 
                Row row = sheet.createRow(rowNum); 
                rowNum++;
                Object[] objArr = dataSheetInfo.get(key); 
                String isFirstGroupItem = String.valueOf(objArr[objArr.length-1]);
                int cellnum = 0; 
                if (applyStyling && isFirstGroupItem(isFirstGroupItem)) {
                    addBestDealStyling();
                    //Arrow function that is defined to style if necessary when the item is the best deal
                    CellStyler cellStyler = (cell) -> {
                        cell.setCellStyle(cellStyle);
                    };
                    writeRows(row, objArr, cellnum, cellStyler); 

                }
                else writeRows(row, objArr, cellnum, null); 
            } 
        }
    
    
    /*
     * checks whether the current row has been flagged as a first group item, aka the best deal
     * in order to highlight it in Excel File
     */
    private boolean isFirstGroupItem(String isFirstGroupItem) {
        return (isFirstGroupItem.equals("true"));
    }

    /*
     * this method writes the cells for each row and checks their type to make sure they are displayed correctly
     * in the Excel File
     */
    private void writeRows(Row row, Object[] objArr, int cellnum, CellStyler cellStyler) {
        for (int i = 0; i < objArr.length - 1; i++) { 
            // This line creates a cell in the next 
            //  column of that row 
            Cell cell = row.createCell(cellnum++); 
            
            if (objArr[i] == null) {
                cell.setCellValue("N/A");
            }
            if (objArr[i] instanceof Double) 
                cell.setCellValue((Double)objArr[i]); 
   
            else if (objArr[i] instanceof Integer) 
                cell.setCellValue((Integer) objArr[i]); 
            else cell.setCellValue((String)objArr[i]);

            if (cellStyler != null) cellStyler.customize(cell);
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
        this.cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        this.cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

}
