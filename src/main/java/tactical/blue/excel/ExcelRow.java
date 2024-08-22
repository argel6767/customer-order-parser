package tactical.blue.excel;

public class ExcelRow {
    private String itemName;
    private String manufacturer;
    private String sku;
    private Integer quantityRequested; //how much of item customer wants
    private Integer quantityNeeded; //how much is actually needed to be bought, depened on packaging
    private String packaging; //how item is sold -- each, box, etc
    private Double msrp; //Manufacturers Suggested Retail Price 
    private Double wholeSalePrice; //cost for a single item
    private Double costOfGoods; //  Cost of Goods Sold – qty x wholesale (usually just qty * wholesale)
    private final Double MARKUP = 1.30; //markup of customers 
    private Double unitPrice; //price per item customer will pay
    private Double extendedPrice; //entiry quantity of item customer will pay
    private String source; //website item information was aqcuired from
    private String productURL;


    public ExcelRow(String itemName, String sku, int quantity, double msrp, double wholeSalePrice, String productURL) {
        this.itemName = itemName;
        this.sku = sku;
        this.quantityNeeded = quantity;
        this.msrp = msrp;
        this.wholeSalePrice = wholeSalePrice;
        this.productURL = productURL;
        calculateCostAndPrices();
    }

    //If Data is not preformatted before object is constructed
    public ExcelRow(String itemName, String sku, String quantity, String msrp, String wholeSalePrice, String productURL) {
        this.itemName = itemName;
        this.sku = sku;
        this.quantityNeeded = Integer.valueOf(quantity);
        this.msrp = Double.valueOf(msrp);
        this.wholeSalePrice = Double.valueOf(wholeSalePrice);
        this.productURL = productURL;
        calculateCostAndPrices();
    }

    private void calculateCostAndPrices() {
        calculateCostOfGoods();
        calculateUnitPrice();
        calculateExtendedPrice();
    }

    /*
     * calculating values costOfGoods, unitPrice, and extendedPrice
     */
    private void calculateCostOfGoods() {
        Double price = this.quantityNeeded * this.wholeSalePrice;
        setCostOfGoods(price);
    }

    private void calculateUnitPrice() {
        Double price = this.wholeSalePrice * this.MARKUP;
        setUnitPrice(price);
    }

    private void calculateExtendedPrice() {
        Double price = this.unitPrice * quantityNeeded;
        setExtendedPrice(price);
    }



    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantityRequested() {
        return this.quantityRequested;
    }

    public void setQuantityRequested(Integer quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public Integer getQuantityNeeded() {
        return this.quantityNeeded;
    }

    public void setQuantityNeeded(Integer quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }

    public String getPackaging() {
        return this.packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public Double getMsrp() {
        return this.msrp;
    }


    public Double getWholeSalePrice() {
        return this.wholeSalePrice;
    }


    public Double getCostOfGoods() {
        return this.costOfGoods;
    }

    private void setCostOfGoods(Double costOfGoods) {
        this.costOfGoods = costOfGoods;
    }

    public Double getMARKUP() {
        return this.MARKUP;
    }


    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getExtendedPrice() {
        return this.extendedPrice;
    }

    public void setExtendedPrice(Double extendedPrice) {
        this.extendedPrice = extendedPrice;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProductURL() {
        return this.productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    

    public ExcelRow(){}

    public void setitemName(String itemName) {
        this.itemName = itemName;
    }
    
    public void setSKU(String sku) {
        this.sku = sku;
    }
    
    public void setQuantity(int quantity) {
        this.quantityNeeded = quantity;
    }
    
    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }
    
    public void setWholeSalePrice(double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    @Override
    public String toString() {
        return this.itemName + ", " + this.sku + ", " + this.quantityNeeded + ", " + this.msrp + ", " + this.wholeSalePrice + ", " + this.productURL;
    }

    public Object[] toArray() {
        return new Object[]{itemName, sku, quantityNeeded, msrp, wholeSalePrice, productURL};
    }
}

