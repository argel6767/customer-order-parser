package tactical.blue.parsing.csv_parsing;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public abstract class CSVParser {

    /*
     * This method returns the rows of the Customer Order Information CSV file
     * as a List of String[]
     */
    public List<String[]> getCSVRows(File customerInfo) {
        try {
            System.out.println("grabbing CSV rows");
            FileReader fileReader = new FileReader(customerInfo);
            CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
            return csvReader.readAll();
        }
        catch (Exception e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
