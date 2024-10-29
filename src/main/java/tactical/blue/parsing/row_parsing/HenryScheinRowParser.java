package tactical.blue.parsing.row_parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    if (!currWebScrapedDataArray[1].equals("")) { //check if it's not an empty row
                        String customerDescription = currItemArray[0].replace("\"", ""); //objects with same URL with have the same customer description
                        String[] seperatedProductInfo = getItemNameManufactuerAndSKUFromExtractedElement(currWebScrapedDataArray[columnHeaderIndex.get("\"HenrySchein_Product_And_Manufacturer\"")].replace("\"", ""));
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
     * seperates the values via 2 dividers
     * 1 - a regex utlizing the un-needed Henry Schein code
     * 2 - the "-" divider the site uses to seperate the manufacturer name and sku
     */

     private  String[] getItemNameManufactuerAndSKUFromExtractedElement(String itemAndManufacturerInfo) {
        String[] split = seperateItemAndManufacturerInfo(itemAndManufacturerInfo);
        String itemName = split[0].trim();
        String[] manufacturerAndSKU = seperateManufacturerInfo(split[1]);
        String[] productInfo = {itemName, manufacturerAndSKU[0], manufacturerAndSKU[1]};
        return productInfo;
        
    }

    /*
     * Splits the itemAndManufacturerInfo String into two parts via regex
     * returns the itemName and then the manufacturerInfo in a String[]
     */
    private String[] seperateItemAndManufacturerInfo(String itemAndManufacturerInfo) {
        Matcher matcher = createMatcher("\\d{7,}", itemAndManufacturerInfo);
        matcher.find();
        String itemName = itemAndManufacturerInfo.substring(0, matcher.start());
        String manufacturerAndSKU = itemAndManufacturerInfo.substring(matcher.end()+2).trim();
        return new String[] {itemName, manufacturerAndSKU};
    }

    /*
     * Creates Matcher object to find desired Henry Schein number regex
     */
    private Matcher createMatcher(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    /*
     * Seperates the manufacturerInfo by the index of "-"
     * returns manufacturer and sku in a String[]
     */
    private String[] seperateManufacturerInfo (String manufacturerAndSKU) {
        int indexOfDash = manufacturerAndSKU.indexOf("-");
        String manufacturer = manufacturerAndSKU.substring(0, indexOfDash).trim();
        String sku = manufacturerAndSKU.substring(indexOfDash+1).trim();
        return new String[] {manufacturer, sku};
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

    
