package tactical.blue.excel;

public class ExcelRow {
    private String item;
    private String productNumber;
    private int quantity;
    private double msrp;
    private double wholeSalePrice;


    public ExcelRow(String item, String productNumber, int quantity, double msrp, double wholeSalePrice) {
        this.item = item;
        this.productNumber = productNumber;
        this.quantity = quantity;
        this.msrp = msrp;
        this.wholeSalePrice = wholeSalePrice;
    }

    //If Data is not preformatted before object is constructed
    public ExcelRow(String item, String productNumber, String quantity, String msrp, String wholeSalePrice) {
        this.item = item;
        this.productNumber = productNumber;
        this.quantity = Integer.parseInt(quantity);
        this.msrp = Double.parseDouble(msrp);
        this.wholeSalePrice = Double.parseDouble(wholeSalePrice);
    }

    @Override
    public String toString() {
        return this.item + ", " + this.productNumber + ", " + this.quantity + ", " + this.msrp + ", " + this.wholeSalePrice; 
    }
}

