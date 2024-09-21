package tactical.blue.parsing.csv_parsing;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import tactical.blue.excel.excelrows.ExcelRow;

/*
 * Houses the logic of mapping the order of the item descriptions in the Customer Order to ensure items are group correctly in the excel file
 */
public class CustomerOrderInformationCSVParser extends CSVParser{

/*
 * This methods returns the Item Descriptions of products wanted by the customer
 * as a LinkedHashMap object with the format
 * <String, List<ExcelRow>>
 */
    public LinkedHashMap<String, List<ExcelRow>> getItemDescriptions(File customerInfo) {
            List<String[]> rows = getCSVRows(customerInfo);
            return mapItemDescriptions(rows);
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
