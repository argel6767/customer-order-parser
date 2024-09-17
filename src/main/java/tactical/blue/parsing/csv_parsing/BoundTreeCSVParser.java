package tactical.blue.parsing.csv_parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tactical.blue.excel.excelrows.BoundTreeExcelRow;
import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.NoItemFoundExcelRow;

/*
    * Bound Tree Data Scrape Structure:
    * Results, Product, Manufacturer, SKU, Wholesale, List_Price, Packaging, Whole_Sale_Bulk, List_Price_Bulk, Bulk_Packaging, Original_URL
*/

public class BoundTreeCSVParser implements CSVParser{
    @Override
    public List<ExcelRow> parseRow(String[] currItemArray, Map<String, List<String[]>> webScrapedMap,
            HashMap<String, Integer> columnHeaderIndex) {
        List<ExcelRow> productRows = new ArrayList<>();
        if (currItemArray.length >= 3) {
        String itemUrl = currItemArray[2];
        if (webScrapedMap.containsKey(itemUrl)) {
            if (isRowEmpty(currItemArray)) {
                NoItemFoundExcelRow emptyRow = new NoItemFoundExcelRow(currItemArray[0], itemUrl);
                productRows.add(emptyRow);
            }
            else {
                List<String[]> urlValList = webScrapedMap.get(itemUrl); //grabs all products found under url

                for (String[] currWebScrapedDataArray : urlValList) { //makes new objects for each one
                
                    /*
                     * Grab all scraped Info to make row objects
                     * 
                     */
                    String customerDescription = currItemArray[0]; //objects with same URL with have the same customer description
                    String itemName = currWebScrapedDataArray[columnHeaderIndex.get("Product")];
                    String manufacturer = currWebScrapedDataArray[columnHeaderIndex.get("Manufacturer")].trim();
                    String sku = currWebScrapedDataArray[columnHeaderIndex.get("SKU")];
                    int quantityRequested = Integer.parseInt(currItemArray[1]);
                    String packaging = currWebScrapedDataArray[columnHeaderIndex.get("Packaging")];
                    double msrp = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("List_Price")]);
                    double wholesalePrice = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("Wholesale")]);

                    //Grab bulk items to make another BoundTreeExcelRow object
                    /*
                     * Add both a regular and bulk item row to thw entire list of rows
                     */
                    try {
                        double wholesaleBulk = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("Whole_Sale_Bulk")]);
                        double msrpBulk = Double.parseDouble(currWebScrapedDataArray[columnHeaderIndex.get("List_Price_Bulk")]);
                        String packagingBulk = currWebScrapedDataArray[columnHeaderIndex.get("Bulk_Packaging")];
                        productRows.add(new BoundTreeExcelRow(customerDescription, itemName, manufacturer, sku, quantityRequested, packaging, msrp, wholesalePrice, itemUrl)); 
                        productRows.add(new BoundTreeExcelRow(customerDescription, itemName, manufacturer, sku, quantityRequested, packagingBulk, msrpBulk, wholesaleBulk, itemUrl)); 

                    }
                    /*
                     * Will catch NullPointerException that is thrown when an item has no bulk info
                     */
                    catch(NullPointerException npe) {
                        productRows.add(new BoundTreeExcelRow(customerDescription, itemName, manufacturer, sku, quantityRequested, packaging, msrp, wholesalePrice, itemUrl)); 
                    }
                }

            }
                
            return productRows;
        }
    }
    return null;
    }

    

}
