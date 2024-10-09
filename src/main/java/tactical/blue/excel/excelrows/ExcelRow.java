package tactical.blue.excel.excelrows;

import java.text.NumberFormat;

import tactical.blue.api.OpenAIClient;

public class ExcelRow{
    protected static Integer row = 1;
    private String itemDescription; //item description orginally given by customer
    private String itemName; //name of item
    private String manufacturer; //maker of item
    private String sku; //item sku
    private Integer quantityRequested; //how much of item customer wants
    private Integer quantityNeeded; //how much is actually needed to be bought, depened on packaging
    private String packaging; //how item is sold -- each, box, etc
    private Object msrp; //Manufacturers Suggested Retail Price 
    private Double wholeSalePrice; //cost for a single item
    private Double costOfGoods; //  Cost of Goods Sold – qty x wholesale (usually just qty * wholesale)
    private final Double MARKUP = .30; //markup of customers 
    private Double unitPrice; //price per item customer will pay
    private Double extendedPrice; //entire quantity of item customer will pay
    private Double contribution;//the money earned above
    private String source; //website item information was acquired from
    private String productURL; //url of product page
    private Boolean isFirstGroupItem = false;

    

   public ExcelRow(String itemDescription, String itemName, String manufacturer, String sku,
            Integer quantityNeeded, String packaging, Object msrp, Double wholeSalePrice, Double costOfGoods,
            Double unitPrice, Double extendedPrice, Double contribution, String source, String productURL) {
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.manufacturer = manufacturer;
        this.sku = sku;
        this.quantityNeeded = quantityNeeded;
        this.packaging = packaging;
        this.msrp = msrp;
        this.wholeSalePrice = wholeSalePrice;
        this.costOfGoods = costOfGoods;
        this.unitPrice = unitPrice;
        this.extendedPrice = extendedPrice;
        this.contribution = contribution;
        this.source = source;
        this.productURL = productURL;
    }


    /*
   Need to find raw quantity of item using item description
   */
    public ExcelRow(String itemDescription ,String itemName, String manufacturer, String sku, Integer quantityRequested, String packaging, Double msrp, Double wholeSalePrice, String productURL) {
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.manufacturer = manufacturer;
        this.sku = sku;
        this.packaging = packaging;
        this.quantityRequested = calculateRawQuantity(quantityRequested, itemDescription);
        this.msrp = determineIfMSRPIsPresent(msrp);
        this.wholeSalePrice = wholeSalePrice;
        this.productURL = productURL;
        calculatePricingAndQuantities(packaging);
        determineSourceUsingProductURL();
    }


    /*
     * If no packaging info is found, an item descrition can be used to get packaging type
     */
    public ExcelRow(String itemDescription, String itemName, String manufacturer, String sku, Integer quantityRequested, Double msrp, Double wholeSalePrice, String productURL) {
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.manufacturer = manufacturer;
        this.sku = sku;
        this.packaging = getPackagingFromItemDescription(itemDescription);
        this.quantityRequested = calculateRawQuantity(quantityRequested, itemDescription);
        this.msrp = determineIfMSRPIsPresent(msrp);
        this.wholeSalePrice = wholeSalePrice;
        this.productURL = productURL;
        calculatePricingAndQuantities(packaging);
        determineSourceUsingProductURL();
    }

    //If Data is not preformatted before object is constructed
    public ExcelRow(String itemName, String manufacturer, String sku, String quantityRequested, String packaging, String msrp, String wholeSalePrice, String productURL) {
        this.itemName = itemName;
        this.manufacturer = manufacturer;
        this.sku = sku;
        this.packaging = packaging;
        this.quantityRequested = Integer.valueOf(quantityRequested);
        this.msrp = Double.valueOf(msrp);
        this.wholeSalePrice = Double.valueOf(wholeSalePrice);
        this.productURL = productURL;
        calculatePricingAndQuantities(packaging); 
        determineSourceUsingProductURL();
    }


    public ExcelRow(String itemName, Integer quantityRequested, String packaging, Double msrp, Double wholeSalePrice, String productURL) {
        this.itemName = itemName;
        this.quantityRequested = quantityRequested;
        this.packaging = packaging;
        this.msrp = determineIfMSRPIsPresent(msrp);
        this.wholeSalePrice = wholeSalePrice;
        this.productURL = productURL;
        calculatePricingAndQuantities(packaging);
    }

    /*
     * resets the row value to make sure each new file has proper numbering starting at one
     * allows for static calls
     */
    public static void resetRowNumber() {
        ExcelRow.row = 1;
    }

    /*
     * sets isFirstGroupItem flag to true which allows for the first items of each group, ie the best deal to be turned green to highlight
     */
    public void setIsFirstGroupItem() {
        this.isFirstGroupItem = true;
    }

    /*
     * Will Check if MSRP is null, ie the Store Front Does not have an MSRP and return "N/A"
     * return the value back otherwise
     */
    private Object determineIfMSRPIsPresent(Double msrp) {
        if (msrp.equals(0.0)) {
            return "N/A";
        }
        return msrp;
    }

    
    /*
     * Finds the packaging via customer description of item, then multplies by quantity requested in order to get raw value, as opposed to packaged value
     * (100 as opposed to 4 of 25pk)
     * If packaging is each then just return requested quantity
     */
    private int calculateRawQuantity(int quantityRequested, String customerDescription) {
        String packaging = getPackagingFromItemDescription(customerDescription);
        if (packaging.equals("Each")) {
           return quantityRequested;
        }
        else {
            int packagingVal = getNumberPartOfPackagingOnly(packaging);
            return quantityRequested * packagingVal;
        }
    }

    /*
    * Takes in a itemDescription then makes an OpenAIObject in the method to return the packaging for any use
    * works with both customer and store item descriptions
    */
    protected static String getPackagingFromItemDescription(String itemDescription) {
        OpenAIClient openAIClient = new OpenAIClient(itemDescription);
        return openAIClient.makeAPICall();
    }

    /*
     * Grabs the number portion of a packaging item and returns it as an int
     */

    private int getNumberPartOfPackagingOnly(String packaging) {
        if (packaging.length() < 1) {
            return 1;
        }
        int index = 0;
        String digit = "";
        while (Character.isDigit(packaging.charAt(index))) {
            digit += packaging.charAt(index);
            index++;
        }
        if (digit.equals("")) {
            return 1;
        }
        return Integer.parseInt(digit);
    }


    /*
     * Caluclates various important values for excel row, that are not potentially given directly by websites or customers
     */
    private void calculatePricingAndQuantities(String packaging) {
        calculateQuantityNeeded(packaging);
        calculateCostOfGoods();
        calculateUnitPrice();
        calculateExtendedPrice();
        calculateContribution();
    }

    /*
     * grab the number value of the packaging variable to be able to determine how much
     * of much of an item needs to be bought then save it to
     * quantity needed
     */
    private void calculateQuantityNeeded(String packaging) {
       double extractedPackagingVal = getNumberPartOfPackagingOnly(packaging);
       double quantityNeededToBuy = Math.ceil(this.quantityRequested/extractedPackagingVal);
       setQuantityNeeded((int)quantityNeededToBuy);
    }

    //find the source of the item bought using the url of the product page
    protected void determineSourceUsingProductURL() {
        String urlLowerCase = this.productURL.toLowerCase(); // Convert to lowercase for easier matching
        
        if (urlLowerCase.contains("boundtree") || urlLowerCase.contains("boundtree.com")) {
            this.source = "Boundtree";
        } else if (urlLowerCase.contains("henryschein") || urlLowerCase.contains("henryschein.com")) {
            this.source = "Henry Schein";
        } else if (urlLowerCase.contains("narescue") || urlLowerCase.contains("narescue")) {
            this.source = "North American Rescue";
        } else if (urlLowerCase.contains("dynarex") || urlLowerCase.contains("dynarex.com")) {
            this.source = "Dynarex";
        } else if (urlLowerCase.contains("medco-athletics") || urlLowerCase.contains("medco sports medicine")) {
            this.source = "Medco Sports Medicine";
        } else if (urlLowerCase.contains("login required")) {
            this.source = "Login Required";
        } else {
            this.source = "Unknown";
        }
    }

    /*
     * calculating values costOfGoods, unitPrice, and extendedPrice
     */
    private void calculateCostOfGoods() {
        Double price = this.quantityNeeded * this.wholeSalePrice;
        setCostOfGoods(price);
    }

    private void calculateUnitPrice() {
        Double price = this.wholeSalePrice * (1.0+ this.MARKUP);
        setUnitPrice(price);
    }

    private void calculateExtendedPrice() {
        Double price = this.unitPrice * quantityNeeded;
        setExtendedPrice(price);
    }

    private void calculateContribution() {
        Double contribution = (this.extendedPrice - this.costOfGoods);
        setContribution(contribution);
    }


    //getters and setters


    
    public void setMsrp(Object msrp) {
        this.msrp = msrp;
    }


    public String getItemDescription() {
        return itemDescription;
    }


    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public Double getContribution() {
        return contribution;
    }


    public Double getWholeSalePrice() {
        return wholeSalePrice;
    }


    public void setWholeSalePrice(Double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
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

    public Object getMsrp() {
        return this.msrp;
    }


    public Double getWholesalePrice() {
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

    public void setContribution(Double contribution) {
        this.contribution = contribution;
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
        this.msrp = determineIfMSRPIsPresent(msrp);
    }
    
    public void setWholeSalePrice(double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    @Override
public String toString() {
    return "ExcelRow{" +
            "itemDescription='" + itemDescription + '\''+
            "itemName='" + itemName + '\'' +
            ", manufacturer='" + manufacturer + '\'' +
            ", sku='" + sku + '\'' +
            ", quantityRequested=" + quantityRequested +
            ", quantityNeeded=" + quantityNeeded +
            ", packaging='" + packaging + '\'' +
            ", msrp=" + msrp +
            ", wholeSalePrice=" + wholeSalePrice +
            ", costOfGoods=" + costOfGoods +
            ", MARKUP=" + MARKUP +
            ", unitPrice=" + unitPrice +
            ", extendedPrice=" + extendedPrice +
            ", contribution=" + contribution +
            ", source='" + source + '\'' +
            ", productURL='" + productURL + '\'' +
            '}';
}

    /*
     * Excel Row Format:
     * Line Item, Product Name, Manufacturer, Source, SKU, Packaging, Quantity, MSRP, Wholesale, Cost of Goods, Markup, Unit Price, Extended Price, Contribution, Product URL
     */
    public Object[] toArray() { //TODO fix contribution bug, this is temporary fix
        return new Object[]{ExcelRow.row++, this.itemDescription, this.itemName, this.manufacturer, this.source, this.sku, this.packaging, this.quantityNeeded, this.msrp, this.wholeSalePrice, this.costOfGoods, convertToPercent(this.MARKUP), this.unitPrice, this.extendedPrice, this.contribution, this.productURL, this.isFirstGroupItem};
    }

    /*
     * Converts contribution to the desired percent format in the Excel Sheet
     */
    protected String convertToPercent(Double decimal) {
        NumberFormat percentConverter = NumberFormat.getPercentInstance();
        percentConverter.setMaximumFractionDigits(0);
        return percentConverter.format(decimal);
    }
 
}

