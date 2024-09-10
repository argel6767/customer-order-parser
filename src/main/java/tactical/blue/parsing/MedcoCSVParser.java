package tactical.blue.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.MedcoSportsMedicineExcelRow;

public class MedcoCSVParser implements CSVParser{

    @Override
    public List<ExcelRow> parseRow(String[] currItemArray, Map<String, List<String[]>> webScrapedMap,
            HashMap<String, Integer> columnHeaderIndex) {
        List<ExcelRow> productRows = new ArrayList<>();
        if (currItemArray.length >= 3) {
        String itemUrl = currItemArray[2];
        if (webScrapedMap.containsKey(itemUrl)) {
            List<String[]> urlValList = webScrapedMap.get(itemUrl); //grabs all products found under url

            for (String[] currWebScrapedDataArray : urlValList) { //makes new objects for each one
                String customerDescription = currItemArray[0]; //objects with same URL with have the same customer description
                String itemName = currWebScrapedDataArray[columnHeaderIndex.get("Product")];
                String manufacturer = currWebScrapedDataArray[columnHeaderIndex.get("Manufacturer")];
                String sku = currWebScrapedDataArray[columnHeaderIndex.get("SKU")];
                int quantityRequested = Integer.parseInt(currItemArray[1]);
                //Medco Does not include MSRP
                Double msrp = 0.0;
                double wholesalePrice = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("Wholesale")]);

                productRows.add(new MedcoSportsMedicineExcelRow(customerDescription, itemName, manufacturer, sku, quantityRequested, msrp, wholesalePrice, itemUrl)); //then add current row to all the will be returned
            }
            
            return productRows;
        }

        }
        return null; //will return null if line is not valid
    }

}
