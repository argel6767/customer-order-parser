package tactical.blue.parsing.excel_parsing;

import org.apache.poi.ss.usermodel.Cell;

@FunctionalInterface
public interface CellStyler {
    void customize(Cell cell);
}
