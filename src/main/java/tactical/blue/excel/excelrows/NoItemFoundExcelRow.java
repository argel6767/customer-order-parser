package tactical.blue.excel.excelrows;


/*
 * TODO Implement this class and use it for parsing algorithms
 * If no item is found, an object of this class will be instantiated, as opposed to whichever site object that would have been made
 */
public class NoItemFoundExcelRow extends ExcelRow{
    public NoItemFoundExcelRow(String itemDescription, String productURl) {
        super.setItemDescription(itemDescription);
        super.setProductURL(productURl);
    }

    /*
     * Overrides the original toArray() implementation due to the fact that no item info was found
     * /*
     * Excel Row Format:
     * Line Item, Product Description, Product Name, Manufacturer, Source, SKU, Packaging, Quantity, MSRP, Wholesale, Cost of Goods, Markup, Unit Price, Extended Price, Contribution, Product URL
     */
    @Override
    public Object[] toArray() {
        return new Object[] {ExcelRow.row++, super.getItemDescription(), "No Item Found Matching Customer Description", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", convertToPercent(super.getMARKUP()), "N/A", "N/A", "N/A", super.getProductURL()};
    }
}
