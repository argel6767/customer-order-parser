package tactical.blue.parsing.row_parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tactical.blue.excel.excelrows.ExcelRow;

/*
 * Placeholder, if NA Rescue can eventually be successfully parsed
 */
public class NARescueRowParser implements RowParser{

    @Override
    public List<ExcelRow> parseRow(String[] currItemArray, Map<String, List<String[]>> webScrapedMap,
            HashMap<String, Integer> columnHeaderIndex, boolean isGroupedRow) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseRow'");
    }

}
