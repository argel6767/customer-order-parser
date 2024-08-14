package tactical.blue.excel;

public class ExcelRow {
    private String item;
    private String productNumber;
    private int quantity;
    private double msrp;
    private double wholeSalePrice;
    private String productURL;


    public ExcelRow(String item, String productNumber, int quantity, double msrp, double wholeSalePrice, String productURL) {
        this.item = item;
        this.productNumber = productNumber;
        this.quantity = quantity;
        this.msrp = msrp;
        this.wholeSalePrice = wholeSalePrice;
        this.productURL = productURL;
    }

    //If Data is not preformatted before object is constructed
    public ExcelRow(String item, String productNumber, String quantity, String msrp, String wholeSalePrice, String productURL) {
        this.item = item;
        this.productNumber = productNumber;
        this.quantity = Integer.parseInt(quantity);
        this.msrp = Double.parseDouble(msrp);
        this.wholeSalePrice = Double.parseDouble(wholeSalePrice);
        this.productURL = productURL;
    }

    public ExcelRow(){}

    public void setItem(String item) {
        this.item = item;
    }
    
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }
    
    public void setWholeSalePrice(double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    @Override
    public String toString() {
        return this.item + ", " + this.productNumber + ", " + this.quantity + ", " + this.msrp + ", " + this.wholeSalePrice + ", " + this.productURL;
    }

    public Object[] toArray() {
        return new Object[]{item, productNumber, quantity, msrp, wholeSalePrice, productURL};
    }
}

