package tactical.blue.excel.excelrows;

/*
 * Product, Source, Price, Packaging, Manufactuer
 */
public class HenryScheinExcelRow extends ExcelRow{

    public HenryScheinExcelRow(String customerDescription, String itemName, String manufacturer, String sku, int quantityRequested, String packaging, double msrp, double wholeSalePrice, String productURL) {
        super(customerDescription, itemName, manufacturer, sku, quantityRequested, packaging, msrp, wholeSalePrice, productURL);
    }

}
