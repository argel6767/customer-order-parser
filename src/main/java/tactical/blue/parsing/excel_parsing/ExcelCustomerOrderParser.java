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
    private final List<String> group = new ArrayList<>();

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
        List<List<String>> list = new ArrayList<>();
        PriceReportParser.grabRowsFromExcelFile(list, rowIterator);
        mapRows(list);
        return list;
    }
    
    private void mapRows(List<List<String>> rows) {
        String currentHeader = "";
        for (List<String> row : rows) {
            if (row.get(1).isEmpty()) {
                currentHeader = row.getFirst();
                this.groupedRows.put(currentHeader, new ArrayList<>());
            }
            else {
                row.add(currentHeader);
                this.groupedRows.get(currentHeader).add(row);
            }
        }
    }

    public void createUrls(String siteName) {
        List<List<String>>  list = groupedRows.values().stream().flatMap(List::stream).toList();
        for (List<String> row : list) {
            String itemDescription = row.getFirst().trim().replaceAll(",", "");
            this.itemDescriptions.add(itemDescription);
            this.urls.add(UrlCreator.createUrl(siteName, itemDescription));
            this.quantity.add(row.get(4).trim().replace(".0", ""));
            this.group.add(row.getLast());
            System.out.println(row.getLast());
        }
    }

    public void writeToFile(String site) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(new File(site + ".csv")));
        String[] header = {"Item", "Quantity", "Url", "Group"};
        writer.writeNext(header);
        for (int i = 0; i < this.itemDescriptions.size(); i++) {
            String[] row = {itemDescriptions.get(i), quantity.get(i), urls.get(i), group.get(i)};
            writer.writeNext(row);
        }
        writer.close();
    }


    public LinkedHashMap<String, List<List<String>>> getGroupedRows() {
        return groupedRows;
    }

    public static void main(String[] args) throws IOException {
        ExcelCustomerOrderParser excelCustomerOrderParser = new ExcelCustomerOrderParser(new File("src/main/java/tactical/blue/parsing/excel_parsing/2.2.1-Request for Quote_Attachment 1 – Required Supplies and Pricing Template.xlsx"));
        List<List<String>> list = excelCustomerOrderParser.readFiles();
        excelCustomerOrderParser.mapRows(list);
        excelCustomerOrderParser.createUrls("BoundTree");
        excelCustomerOrderParser.writeToFile("Bound_Tree");
    }

}
