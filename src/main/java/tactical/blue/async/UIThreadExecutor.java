package tactical.blue.async;

import tactical.blue.services.PriceReportCreator;
import tactical.blue.services.ReportConsolidator;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;


/*
 * This class holds the ExecutorService that holds the threads needed for multithreading
 * and allowing for the UI not the freeze
 */
public class UIThreadExecutor {
   private final ExecutorService executorService;

    public UIThreadExecutor(int threadNumber) {
        this.executorService = Executors.newFixedThreadPool(threadNumber);
   }

   /*
   Testing Constructor
    */
   public UIThreadExecutor(ExecutorService executorService) {
     this.executorService = executorService;
   }

   public ExecutorService getExecutorService() {
     return this.executorService;
   }

   /*
    * designates a thread with the task of creating a price report, and returns a CompletableFuture object
    */
    public CompletableFuture<Void> makePriceReportAsync(File webScrape, File customerOrder, String siteName) {
        if (webScrape == null || customerOrder == null) {
            return CompletableFuture.completedFuture(null);
        }
       PriceReportCreator priceReportCreator = new PriceReportCreator(webScrape, customerOrder, siteName);
       return priceReportCreator.makeNewExcelFile();
   }

   /*
    * different method signature for when the items are grouped
    */
    public CompletableFuture<Void> makePriceReportAsync(File webScrape, File customerOrder, String siteName, boolean isGroupedItems) {
        if (webScrape == null || customerOrder == null) {
            return CompletableFuture.completedFuture(null);
        }
        PriceReportCreator priceReportCreator = new PriceReportCreator(webScrape, customerOrder, siteName, isGroupedItems);
        return priceReportCreator.makeNewExcelFile();
    }

   /*
    * shuts down ExecutorService
    */
   public void shutdown() {
       executorService.shutdown();
   }


   /*
    * designates a thread with the task of consolidating reports and returns CompletableFuture object
    */
   public CompletableFuture<Void> makeReportConsolidationAsync(List<File> reports, File customerOrder) {
       return CompletableFuture.runAsync(createReportConsolidatorRunnable(reports, customerOrder), executorService);
   }
   
   /*
    * create Runnable; runs consolidateReports()
    */
   private Runnable createReportConsolidatorRunnable(List<File> reports, File customerOrder) {
       return () -> {
          ReportConsolidator reportConsolidator = new ReportConsolidator(reports, customerOrder);
          reportConsolidator.consolidateReports();
       };
   }
}
