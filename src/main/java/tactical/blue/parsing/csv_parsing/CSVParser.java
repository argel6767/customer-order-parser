package tactical.blue.parsing.csv_parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tactical.blue.excel.excelrows.ExcelRow;

public interface CSVParser {
    public List<ExcelRow> parseRow(String[] currItemArray, Map<String, List<String[]>> webScrapedMap, HashMap<String, Integer> columnHeaderIndex);

    private boolean isRowEmptyLogic(String[] currentWebScrapedData, int index) {
        for (int i = index; i < currentWebScrapedData.length;i++) {
            if (currentWebScrapedData[i] != null) {
                return false;
            }
        }
        return true;
    }

    default boolean isRowEmpty(String[] currentWebScrapedData, int index) {
        return isRowEmptyLogic(currentWebScrapedData, index);
    }
}
