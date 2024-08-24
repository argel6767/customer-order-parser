package tactical.blue.excel.excelrows;

import tactical.blue.excel.api.OpenAIClient;

/*
 * Medco Sports Medicine Scrape Structure
 * Results, Manufacturer, Product, SKU, Our_Price, Original_URL
 */
public class MedcoSportsMedicineExcelRow extends ExcelRow{
    
    public MedcoSportsMedicineExcelRow(String itemName, String manufacturer, String sku, int quantityRequested, String itemDescription, double msrp, double wholeSalePrice, String productURL) {
       super(itemName, manufacturer, sku, quantityRequested, getPackagingFromItemDescription(itemDescription), msrp, wholeSalePrice,productURL);
    }


/*
 * Takes in a itemDescription then makes an OpenAIObject in the method to return the packaging for constructor
 */
    private static String getPackagingFromItemDescription(String itemDescription) {
        OpenAIClient openAIClient = new OpenAIClient(itemDescription);
        return openAIClient.makeAPICall();
    }
}
