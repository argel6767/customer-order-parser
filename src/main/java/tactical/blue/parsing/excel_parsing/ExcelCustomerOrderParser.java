package tactical.blue.parsing.excel_parsing;

import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tactical.blue.parsing.UrlCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ExcelCustomerOrderParser {

    private Workbook workbook;

    private final LinkedHashMap<String, List<List<String>>> groupedRows = new LinkedHashMap<>();

    private final List<String> urls = new ArrayList<>();
    private final List<String> itemDescriptions = new ArrayList<>();
    private final List<String> quantity = new ArrayList<>();

    public ExcelCustomerOrderParser(File file) {
        try {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            System.out.println("Something went wrong! The Excel file could not be found.");
            e.printStackTrace();
        }
    }

    public List<List<String>> readFiles() {
        Sheet sheet = workbook.getSheetAt(1);
        Iterator<Row> rowIterator = sheet.iterator();
        List<List<String>> list = new ArrayList<List<String>>();
        PriceReportParser.grabRowsFromExcelFile(list, rowIterator);
        mapRows(list);
        return list;
    }
    
    private void mapRows(List<List<String>> rows) {
        String currentHeader = "";
        for (List<String> row : rows) {
            if (row.get(1).isEmpty()) {
                currentHeader = row.get(0);
                this.groupedRows.put(currentHeader, new ArrayList<>());
            }
            else {
                this.groupedRows.get(currentHeader).add(row);
            }
        }
    }

    public void createUrls(String siteName) {
        List<List<String>>  list = groupedRows.values().stream().flatMap(List::stream).toList();
        for (List<String> row : list) {
            String itemDescription = row.get(0).trim().replaceAll(",", "");
            this.itemDescriptions.add(itemDescription);
            this.urls.add(UrlCreator.createUrl(siteName, itemDescription));
            this.quantity.add(row.get(4).trim().replace(".0", ""));
        }

        this.urls.forEach(System.out::println);
        this.itemDescriptions.forEach(System.out::println);
        this.quantity.forEach(System.out::println);

    }

    public void writeToFile() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(new File("Medco.csv")));
        String[] header = {"Item", "Quantity", "Url"};
        writer.writeNext(header);
        for (int i = 0; i < this.itemDescriptions.size(); i++) {
            String[] row = {itemDescriptions.get(i), quantity.get(i), urls.get(i)};
            writer.writeNext(row);
        }
    }

    public LinkedHashMap<String, List<List<String>>> getGroupedRows() {
        return groupedRows;
    }

}
