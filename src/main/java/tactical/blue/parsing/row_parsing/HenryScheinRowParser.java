package tactical.blue.parsing.row_parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tactical.blue.excel.excelrows.ExcelRow;
import tactical.blue.excel.excelrows.HenryScheinExcelRow;
import tactical.blue.excel.excelrows.NoItemFoundExcelRow;

public class HenryScheinRowParser implements RowParser {


    @Override
    public List<ExcelRow> parseRow(String[] currItemArray, Map<String, List<String[]>> webScrapedMap, HashMap<String, Integer> columnHeaderIndex) {
        List<ExcelRow> productRows = new ArrayList<>();
        
        String itemUrl = currItemArray[2];
        if (webScrapedMap.containsKey(itemUrl)) {
                List<String[]> urlValList = webScrapedMap.get(itemUrl); //grabs all products found under url
            
                for (String[] currWebScrapedDataArray : urlValList) { //makes new objects for each one
                    if (!currWebScrapedDataArray[1].equals("\"\"")) { //check if it's not an empty row
                        String customerDescription = currItemArray[0].replace("\"", ""); //objects with same URL with have the same customer description
                        String[] seperatedProductInfo = getItemNameManufactuerAndSKUFromExtractedElement(currWebScrapedDataArray[columnHeaderIndex.get("\"HenrySchein_Product_And_Manufacturer\"")]);
                        String itemName = seperatedProductInfo[0];
                        String manufacturer = seperatedProductInfo[1];
                        String sku = seperatedProductInfo[2];
                        int quantityRequested = Integer.parseInt(currItemArray[1]);
                        String packaging = currWebScrapedDataArray[columnHeaderIndex.get("\"HenrySchein_Packaging\"")];

                        
                        String unCleanedMSRP = currWebScrapedDataArray[columnHeaderIndex.get("\"HenrySchein_MSRP\"")];
                        Double msrp = cleanScrapedMSRP(unCleanedMSRP);
                        
                        String unCleanedWholesale = currWebScrapedDataArray[columnHeaderIndex.get("\"HenrySchein_Wholesale\"")];
                        double wholesale = cleanScrapedWholsale(unCleanedWholesale);
                        productRows.add(new HenryScheinExcelRow(customerDescription, itemName, manufacturer, sku, quantityRequested, packaging, msrp, wholesale, itemUrl)); //then add current row to all the will be returned
                    }

                    else {
                        String itemDescription = currItemArray[0];
                        productRows.add(new NoItemFoundExcelRow(itemDescription, itemUrl));
                    }
                }
            
            return productRows;
        } 
        return null; //Non valid row
        
    }

     /*
     * Must be used for HenrySchein data as item name, manufacturer and SKU are tied to the same element on the web page
     * will seperate the three by using items that are used as dividors on actual website " - " and " | "
     */

     private String[] getItemNameManufactuerAndSKUFromExtractedElement(String itemAndManufacturerInfo) {
        String[] firstSplit = itemAndManufacturerInfo.split(" \\| ");
        String itemNameAndHenryScheinNumber = firstSplit[0];
        String itemName = itemNameAndHenryScheinNumber.replaceAll("\\d+$", "");
        String manufacturerAndSKU = firstSplit[1];
        String[] secondSplit = manufacturerAndSKU.split(" \\- ");
        String sku = secondSplit[1];
        String manufacturer = secondSplit[0];

        String[] productInfo = {itemName, manufacturer, sku};
        return productInfo;
        
    }


    /*
     * Cleans scraped MSRP by getting rid of $ sign and lingering ""
     */
    private Double cleanScrapedMSRP(String msrp) {
        String msrpCleaned = msrp.replace("$", "").replaceAll("\"", "");
        
        return Double.valueOf(msrpCleaned);

    }

    /*
     * Cleans scraped Wholesale by getting rid of 1 @ $ and lingering ""
     */
    private Double cleanScrapedWholsale (String wholesale) {
        wholesale = wholesale.replace("1 @ $", "");
        wholesale = wholesale.replaceAll("\"", "");

        return Double.valueOf(wholesale);
    }


} 

    
