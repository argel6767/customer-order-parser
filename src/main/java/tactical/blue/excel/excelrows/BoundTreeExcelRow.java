package tactical.blue.excel.excelrows;

/*
 * Bound Tree Data Scrape Structure:
 * Results, Product, Manufacturer, SKU, Wholesale, List_Price, Packaging, Whole_Sale_Bulk, List_Price_Bulk, Bulk_Packaging, Original_URL
 */
public class BoundTreeExcelRow extends ExcelRow{
    
    public BoundTreeExcelRow(String customerDescription, String itemName, String manufacturer, String sku, int quantityRequested, String packagingType, String amountPerPackage, double msrp, double wholeSalePrice, String productURL) {
        super(customerDescription,itemName, manufacturer, sku, quantityRequested, formatPackaging(amountPerPackage, packagingType), msrp, wholeSalePrice, productURL);
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
