package tactical.blue.excel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CleanExcelFile {
    private File fileOut;
    private File fileIn;
    private final File DIRECTORY = new File("weekly-scrape");
    private BufferedWriter writer;
    private BufferedReader reader;

    public CleanExcelFile(String fileInPath) {
        this.fileIn = new File(fileInPath);
        String fileOutName = LocalDateTime.now() + "-weekly-scrape";
        this.fileOut = new File(DIRECTORY, fileOutName);
        try {
            this.writer = new BufferedWriter(new FileWriter(fileOut));
            this.reader = new BufferedReader(new FileReader(fileIn));
        } catch (IOException ex) {
            System.out.println("SOMETHING WENT WRONG! FILE NOT FOUND");
        }
    }

    

}
