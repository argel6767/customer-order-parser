package tactical.blue.services;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceHandler {
   private final ExecutorService executorService;

    public ExecutorServiceHandler(int threadNumber) {
        this.executorService = Executors.newFixedThreadPool(threadNumber);
   }

   public void addPriceReportThread(File webScrape, File customerOrder, String siteName) {
        this.executorService.execute(createPriceReportRunnable(webScrape, customerOrder, siteName));
   }

   private Runnable createPriceReportRunnable(File webScrape, File customerOrder, String sitename) {
        Runnable priceReport = () -> {
            PriceReportCreator priceReportCreator = new PriceReportCreator(webScrape, customerOrder, sitename);
            priceReportCreator.makeNewExcelFile();
        };
        return priceReport;  
   }
   
}
