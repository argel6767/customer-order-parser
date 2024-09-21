package tactical.blue.parsing.csv_parsing;

import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import tactical.blue.excel.excelrows.ExcelRow;

/*
 * Houses the logic of mapping the order of the item descriptions in the Customer Order to ensure items are group correctly in the excel file
 */
public class CustomerOrderInformationParser {

/*
 * Main method that creates a CSVReader Object that does all the parsing for us
 */
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

    /*
     * Uses the list of rows given by the csvReader object in order to map the item descriptions in order by
     * using a LinkedHashMap
     */
    private LinkedHashMap<String, List<ExcelRow>> mapItemDescriptions(List<String[]> rows) {
        LinkedHashMap<String, List<ExcelRow>> itemDescriptionsMap = new LinkedHashMap<>();
        for (String[] row : rows) {
            String itemDescription = row[0].replace("\"", ""); //Get rid of any left over "'s
            itemDescriptionsMap.put(itemDescription, new ArrayList<>());
        }
        return itemDescriptionsMap;
    }
}
