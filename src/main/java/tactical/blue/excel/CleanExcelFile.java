package tactical.blue.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CleanExcelFile {
    private File fileOut;
    private File fileIn;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedReader bufferedReader;
    private List<String> csvRows = new ArrayList<>();
   

    public CleanExcelFile(String fileInPath) {
        this.fileIn = new File(fileInPath);
        try {
        this.bufferedReader = new BufferedReader(new FileReader(fileIn));
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }
    }

    private void readCSVFile() throws IOException {
        String currLine;
        while ((currLine = bufferedReader.readLine()) != null) {
            this.csvRows.add(currLine);
        }
    }

    
}
