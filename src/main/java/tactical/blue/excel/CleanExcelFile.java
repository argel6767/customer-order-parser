package tactical.blue.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    private File fileOut;
    private File fileInOctoparse;
    private File fileInItemDescription;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedReader bufferedReaderOcto;
    private BufferedReader bufferedReaderItemDescription;
    private List<ExcelRow> excelRows = new ArrayList<>();
   
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
    //reads csv file that is put in
    private void readCSVFiles() throws IOException {
        String currLineOctoparse;
        String currLineItemDescription;
        int iteration = 0;
        while ((currLineOctoparse = bufferedReaderOcto.readLine()) != null && (currLineItemDescription = bufferedReaderItemDescription.readLine()) != null) {
            if (iteration == 0) { //makes sure title line isnt read
                iteration++;
                continue;
            }

            String[] currOctoArray = currLineOctoparse.split(",");
            String[] currItemArray = currLineItemDescription.split(",");
            //will only make the line if the two csv lines match using the url that is search as it wouldnt change, this guareentees different prouducts are not being combined
            if (currItemArray[4].equals(currOctoArray[3])) {
                String productNumber = StringUtils.deleteWhitespace(currOctoArray[1]); //deletes whitespace that isnt cleaned from scraper
                int quantity = Integer.parseInt(currItemArray[2]); //casts quantity as int
                double msrp = Double.parseDouble(currOctoArray[2]); //casts msrp as double
                double wholeSalePrice = msrp * quantity * .7; //TODO update this when the actiual wholeSaleCalculation is determined!!!!
                ExcelRow currRow = new ExcelRow(currItemArray[0], productNumber, quantity, msrp, wholeSalePrice, currOctoArray[3]);
                this.excelRows.add(currRow);
            }
            

        }
    }

    //reads lines from csvRows and puts them into a new excel file
    private void createExcelFile() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("weekly price report");

        Map<String, Object[]> dataSheetInfo = new TreeMap<>();
        dataSheetInfo.put("1", new Object[] {"Item", "Product Number", "Quantity", "MSRP", "Wholesale Price", "ProductURL"}); //headers
        
        int index = 2;
        //adds the excel rows into dataSheetInfo from List excelRows
        for (ExcelRow excelRow : excelRows) {
            dataSheetInfo.put(String.valueOf(index++), excelRow.toArray());
        }

        int rowNum = 0;
        Set<String> keySet = dataSheetInfo.keySet();
          for (String key : keySet) { 
  
            // Creating a new row in the sheet 
            Row row = sheet.createRow(rowNum++); 
  
            Object[] objArr = dataSheetInfo.get(key); 
  
            int cellnum = 0; 
  
            for (Object obj : objArr) { 
  
                // This line creates a cell in the next 
                //  column of that row 
                Cell cell = row.createCell(cellnum++); 
  
                if (obj instanceof String) 
                    cell.setCellValue((String)obj); 
  
                else if (obj instanceof Integer) 
                    cell.setCellValue((Integer)obj); 
            } 
        } 
        }
}
