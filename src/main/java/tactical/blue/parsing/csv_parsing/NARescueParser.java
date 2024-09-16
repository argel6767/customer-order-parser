package tactical.blue.parsing.csv_parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tactical.blue.excel.excelrows.ExcelRow;

/*
 * Placeholder, if NA Rescue can eventually be sucessfully parsed
 */
public class NARescueParser implements CSVParser{

    @Override
    public List<ExcelRow> parseRow(String[] currItemArray, Map<String, List<String[]>> webScrapedMap,
            HashMap<String, Integer> columnHeaderIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseRow'");
    }

}
