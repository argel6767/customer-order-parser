package tactical.blue.excel;

import java.io.File;
import java.time.LocalDateTime;


public class CleanExcelFile {
    private File fileOut;
    private File fileIn;
    private final File DIRECTORY = new File("weekly-scrape");
   

    public CleanExcelFile(String fileInPath) {
        this.fileIn = new File(fileInPath);
        String fileOutName = LocalDateTime.now() + "-weekly-scrape";
        this.fileOut = new File(DIRECTORY, fileOutName);

    }

}
