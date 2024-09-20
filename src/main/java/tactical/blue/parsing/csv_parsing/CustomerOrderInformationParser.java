package tactical.blue.parsing.csv_parsing;

import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import tactical.blue.excel.excelrows.ExcelRow;


public class CustomerOrderInformationParser {

    public LinkedHashMap<String, List<ExcelRow>> getItemDescriptions(File customerInfo) {
        try {
            FileReader fileReader = new FileReader(customerInfo);
            CSVReader csvReader = new CSVReaderBuilder(fileReader) .withSkipLines(1).build(); 
            List<String[]> rows = csvReader.readAll();
            return mapItemDescriptions(rows);
        } catch (Exception e) {
            System.out.println("File not found!");
        }
        return new LinkedHashMap<>();
    }

    private LinkedHashMap<String, List<ExcelRow>> mapItemDescriptions(List<String[]> rows) {
        LinkedHashMap<String, List<ExcelRow>> itemDescriptionsMap = new LinkedHashMap<>();
        for (String[] row : rows) {
            itemDescriptionsMap.put(row[0], new ArrayList<>());
        }
        return itemDescriptionsMap;
    }
}
