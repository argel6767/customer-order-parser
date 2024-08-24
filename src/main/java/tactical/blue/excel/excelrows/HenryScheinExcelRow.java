package tactical.blue.excel.excelrows;

public class HenryScheinExcelRow extends ExcelRow{

    private final String SOURCE = "HenrySchein";

    public HenryScheinExcelRow(String itemName, String manufacturerInfo, int quantityRequested, String packaging, double msrp, double wholeSalePrice, String productURL) {
        super(itemName, quantityRequested, packaging, msrp, wholeSalePrice, productURL);
        setManufactuerAndSKU(manufacturerInfo);
        setSource(this.SOURCE);
    }

    /*
     * Must be used for HenrySchein data as manufacturer and SKU are tied to the same element on the web page
     */

    private void setManufactuerAndSKU(String manufacturerInfo) {
        String [] manufacturerAndSKU = manufacturerInfo.split("\\R+");
        String manufacturer = manufacturerAndSKU[1].trim();
        String sku = manufacturerAndSKU[2].trim();
        setManufacturer(manufacturer);
        setSKU(sku);
    }
}
