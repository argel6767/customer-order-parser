package tactical.blue.excel.excelrows;

/*
 * Bound Tree Data Scrape Structure:
 * Results, Item, Manufacturer, SKU, Original_Url, Our_Price, List_Price, Packaging, (Amount_Per_Package)sometimes, unless its each
 */
public class BoundTreeExcelRow extends ExcelRow{
    
    public BoundTreeExcelRow(String itemName, String manufacturer, String sku, int quantityRequested, String packagingType, String amountPerPackage, double msrp, double wholeSalePrice, String productURL) {
        super(itemName, manufacturer, sku, quantityRequested, formatPackaging(amountPerPackage, packagingType), msrp, wholeSalePrice, productURL);
    }

    /*
     * Formats packaging using the packaingType and amountPerPackage to
     */
    private static String formatPackaging(String amountPerPackage, String packagingType) {
        if (amountPerPackage == null || amountPerPackage.equals("")) {
            return packagingType;
        }
        return amountPerPackage + "/" + packagingType;
    }
}
