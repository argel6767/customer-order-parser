package tactical.blue.services;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.concurrent.Future;


/*
 * This class holds the ExecutorService that holds the threads needed for multithreading
 * and allowing for the UI not the freeze
 */
public class ExecutorServiceHandler {
   private final ExecutorService executorService;

    public ExecutorServiceHandler(int threadNumber) {
        this.executorService = Executors.newFixedThreadPool(threadNumber);
   }

   public ExecutorServiceHandler(ExecutorService executorService) {
     this.executorService = executorService;
   }

   public ExecutorService getExecutorService() {
     return this.executorService;
   }

   /*
    * designates a thread with the task of creating a price report, and returns a Future object
    */
   public CompletableFuture<Void> makePriceReportAsync(File webScrape, File customerOrder, String siteName) {
       return CompletableFuture.runAsync(() -> {
           PriceReportCreator priceReportCreator = new PriceReportCreator(webScrape, customerOrder, siteName);
           priceReportCreator.makeNewExcelFile();
       }, executorService);
   }

    /*
    * creates the Runnable that will be executed by the executorService
    * and runs makeNewExcelFile()
    */
   private Runnable createPriceReportRunnable(File webScrape, File customerOrder, String sitename) {
       return () -> {
            PriceReportCreator priceReportCreator = new PriceReportCreator(webScrape, customerOrder, sitename);
            priceReportCreator.makeNewExcelFile();
       };
   }

   /*
    * designates a thread with the task of consolidating reports and returns future object
    */
   public Future<?> makeReportConsolidation(List<File> reports, File customerOrder) {
          return this.executorService.submit(createReportConsolidatorRunnable(reports, customerOrder));
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
