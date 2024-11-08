module excel_cleaner {
    requires javafx.controls;
    requires javafx.fxml;
    opens tactical.blue to javafx.fxml;
    exports tactical.blue;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.lang3;
    requires jdk.jsobject;
    requires java.naming;
    requires unirest.java;
    requires javafx.graphics;
    requires opencsv;
    requires org.apache.xmlbeans;
    requires org.apache.logging.log4j;
}
