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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CleanExcelFile {
    private File fileOut;
    private File fileInOctoparse;
    private File fileInItemDescription;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedReader bufferedReader;
    private List<String> csvRows = new ArrayList<>();
   
    //constructor that sets up bufferedreader object
    public CleanExcelFile(String fileInPath) {
        this.fileInOctoparse = new File(fileInPath);
        try {
        this.bufferedReader = new BufferedReader(new FileReader(fileInOctoparse));
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }
    }
    //reads csv file that is put in
    private void readCSVFile() throws IOException {
        String currLine;
        while ((currLine = bufferedReader.readLine()) != null) {
            this.csvRows.add(currLine);
        }
    }

    //reads lines from csvRows and puts them into a new excel file
    private void createExcelFile() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("weekly price report");

        Map<String, String[]> dataSheetInfo = new TreeMap<>();
        dataSheetInfo.put("1", new String[] {"Item", "Product Number", "Quantity", "MSRP", "Wholesale Price"}); //headers

        int num = 2;
        for (String row:csvRows) {
            dataSheetInfo.put(String.valueOf(num), row.split(",")); //splits line by comma
            num++;
        }

        Set<String> keySet = dataSheetInfo.keySet();
        int currRow = 1;

        for (String key: keySet) {
            Row row = sheet.createRow(currRow);
            String[] rowArray = dataSheetInfo.get(key);

            int currentCell = 0;
            for (String str : rowArray) {
                Cell cell = row.createCell(currentCell);
            }
        }
    }
}
