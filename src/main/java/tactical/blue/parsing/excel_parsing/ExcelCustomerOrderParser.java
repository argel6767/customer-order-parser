package tactical.blue.parsing.excel_parsing;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.message.StringFormattedMessage;
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
import java.util.stream.Collectors;

public class ExcelCustomerOrderParser {

    private Workbook workbook;

    private final LinkedHashMap<String, List<List<String>>> groupedRows = new LinkedHashMap<>();

    private List<String> urls = new ArrayList<>();
    private List<String> itemDescritpions = new ArrayList<>();
    private List<String> quantity = new ArrayList<>();

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

    public void createUrls() {
        List<List<String>>  list = groupedRows.values().stream().flatMap(List::stream).toList();
        for (List<String> row : list) {
            String itemDescription = row.get(0).trim().replaceAll(",", "");
            this.itemDescritpions.add(itemDescription);
            this.urls.add(UrlCreator.createUrl("Medco", itemDescription));
            this.quantity.add(row.get(4).trim().replace(".0", ""));
        }

        this.urls.forEach(System.out::println);
        this.itemDescritpions.forEach(System.out::println);
        this.quantity.forEach(System.out::println);

    }

    public void writeToFile() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(new File("Medco.csv")));
        String[] header = {"Item", "Quantity", "Url"};
        writer.writeNext(header);
        for (int i = 0; i < this.itemDescritpions.size(); i++) {
            String[] row = {itemDescritpions.get(i), quantity.get(i), urls.get(i)};
            writer.writeNext(row);
        }
    }


    public LinkedHashMap<String, List<List<String>>> getGroupedRows() {
        return groupedRows;
    }

    public static void main(String[] args) throws IOException {
        ExcelCustomerOrderParser excelCustomerOrderParser = new ExcelCustomerOrderParser(new File("src/main/java/tactical/blue/parsing/excel_parsing/2.2.1-Request for Quote_Attachment 1 – Required Supplies and Pricing Template.xlsx"));
        List<List<String>> list = excelCustomerOrderParser.readFiles();
        LinkedHashMap<String, List<List<String>>> groupedRows = excelCustomerOrderParser.getGroupedRows();
        groupedRows.forEach((key, value) -> {
            System.out.println("\n");
            System.out.println(key);
            printList(value);
        });

        excelCustomerOrderParser.createUrls();
        excelCustomerOrderParser.writeToFile();
    }

    private static void printList(List<List<String>> list) {
        for (List<String> row : list) {
            for (String cell : row) {
                if (!cell.isEmpty()) {
                    System.out.print(cell.trim() + " | ");
                }
            }
            System.out.println();
        }
    }
}
