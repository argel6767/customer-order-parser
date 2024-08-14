package tactical.blue.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CleanExcelFile {
    private File fileOut;
    private File fileIn;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedReader bufferedReader;
   

    public CleanExcelFile(String fileInPath) {
        this.fileIn = new File(fileInPath);
        try {
        this.bufferedReader = new BufferedReader(new FileReader(fileIn));
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }

        
    }

}
