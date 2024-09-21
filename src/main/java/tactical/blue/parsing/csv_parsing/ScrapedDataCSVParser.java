package tactical.blue.parsing.csv_parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScrapedDataCSVParser extends CSVParser{

    /*
     * This method maps the rows of CSV file to their productURL, as they will share this value
     * with the matching row in the Customer Order
     */
    public HashMap<String, List<String[]>> mapRows(File webscrapedData, String siteName) {
        List<String[]> rows = getCSVRows(webscrapedData);
        return groupRowsByTheirProductURL(rows, siteName);
    }

    /*
     * This method does the actual grouping by matching up rows by their
     * product URL
     */
    private HashMap<String,List<String[]>> groupRowsByTheirProductURL(List<String[]> rows, String siteName) {
        HashMap<String,List<String[]>> webScrapedMap = new HashMap<>();
        for (String[] row : rows) {
            if (row.length >= 5) { //Ensuring theres enough columns in the row
                String productURl = siteName.equals("Henry Schein")? 
                row[0].replaceAll("\"", "") : row[row.length-1];
                mapingRowByProductURL(webScrapedMap, productURl, row);
            }
        }
        return webScrapedMap;
    }

    /*
     * This method checks if a productURL is alreay a key in the hashmap
     * If so: grabs the associated lists and adds the row
     * Else: makes a new list, adds the row
     * then puts the productUrl and list as a pair into the HashMap
     */
    private void mapingRowByProductURL(HashMap<String,List<String[]>> webScrapedMap, String productURL, String[] row) {
        if (webScrapedMap.containsKey(productURL)) {
            List<String[]> urValList = webScrapedMap.get(productURL);
            urValList.add(row);
        }
        else {
            List<String[]> urlValList = new ArrayList<>();
            urlValList.add(row);
            webScrapedMap.put(productURL, urlValList);
        }
    }
}
