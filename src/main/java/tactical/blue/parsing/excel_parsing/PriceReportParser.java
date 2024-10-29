package tactical.blue.parsing.excel_parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.NoItemFoundExcelRow;



public class PriceReportParser {

    private Workbook workbook;

    public PriceReportParser(File file) {
        try {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Grabs rows from the Excel file and maps them to ExcelRow objects
     * then adds all these objects into a List that is returned
     */
    public List<ExcelRow> parseFile() {
        List<List<String>> rowsList = new ArrayList<>();
        Sheet sheet =  workbook.getSheetAt(0);
        //use iterator to grab rows of data
        Iterator<Row> rowIterator = sheet.iterator();
        grabRowsFromExcelFile(rowsList, rowIterator);
        return createExcelRowObjects(rowsList);
    }

    /*
     * Grabs all rows from the Excel File and makes each of them into a String List
     */
    private void grabRowsFromExcelFile(List<List<String>> rowsList, Iterator<Row> rowIterator) {
        int index = 0;
        while (rowIterator.hasNext()) {
            if (index == 0) {
                rowIterator.next();
                index++;
                continue;
            }
            Row currentRow =  rowIterator.next();
            Iterator<Cell> cellIterator = currentRow.cellIterator();

            List<String> currentRowValues = new ArrayList<>();
            while (cellIterator.hasNext()) {
                //String cell = determineCellType(cellIterator.next());
                currentRowValues.add(String.valueOf(cellIterator.next()));
            }
            rowsList.add(currentRowValues);
        }
    }

    
    /*
     * creates the necessary objects to construct an ExcelRow Object then adds it to a list
     * then returns them all
     */
    private List<ExcelRow> createExcelRowObjects(List<List<String>> rowList) {
        List<ExcelRow> excelRows = new ArrayList<>();
        for (List<String> list : rowList) {
            try
            {
                String itemDescription = list.get(1).replaceAll("\"","");
                String itemName = list.get(2);
                String manufacturer = list.get(3);
                String source = list.get(4);
                String sku = list.get(5);
                String packaging = list.get(6);
                Integer quantity = (int)Double.parseDouble(list.get(7));
                Double msrp = isMSRPPresent(list.get(8));
                Double wholesale = Double.valueOf(list.get(9));
                Double costOfGoods = Double.valueOf(list.get(10));
                Double unitPrice = Double.valueOf(list.get(12));
                Double extendedPrice = Double.valueOf(list.get(13));
                Double contribution =  Double.valueOf(list.get(14));
                String productUrl = list.get(15);

                ExcelRow excelRow = new ExcelRow(itemDescription, itemName, manufacturer, sku, quantity, packaging,
                msrp, wholesale, costOfGoods, unitPrice, extendedPrice, contribution, source, productUrl);

                excelRows.add(excelRow);
            }
            catch(NumberFormatException nfe) {
                String itemDescription = list.get(1).replaceAll("\"","");
                String productURL = "No URL Listed -- Item Not Found"; //placeholder in case program is on Windows
                if (list.size() == 16) { //program being run on mac
                    productURL = list.get(15);
                }                
                excelRows.add(new NoItemFoundExcelRow(itemDescription, productURL));
            }
        }
        return excelRows;
    }

    private Double isMSRPPresent(String msrp) {
        if (msrp.equals("N/A")) {
            return 0.0;
        }
        return Double.valueOf(msrp);
    }
}
