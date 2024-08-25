package tactical.blue.excel.excelrows;

/*
 * Medco Sports Medicine Scrape Structure
 * Results, Manufacturer, Product, SKU, Source, Our_Price, Original_URL
 */
public class MedcoSportsMedicineExcelRow extends ExcelRow{
    //String itemName, String manufacturer, String sku, int quantityRequested, String packaging, double msrp, double wholeSalePrice, String productURL
    public MedcoSportsMedicineExcelRow(String customerDescription, String itemName, String manufacturer, String sku, int quantityRequested, String itemDescription, double msrp, double wholeSalePrice, String productURL) {
       super(customerDescription,itemName, manufacturer, sku, quantityRequested, getPackagingFromItemDescription(itemDescription), msrp, wholeSalePrice,productURL);
    }

}
