package tactical.blue.services;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;


/*
 * This class holds the ExecutorService that holds the threads needed for mulitthreading
 * and allowing for the UI not the freeze
 */
public class ExecutorServiceHandler {
   private final ExecutorService executorService;

    public ExecutorServiceHandler(int threadNumber) {
        this.executorService = Executors.newFixedThreadPool(threadNumber);
   }

   /*
    * designates a thread with the task of creating a price report
    */
   public void makePriceReport(File webScrape, File customerOrder, String siteName) {
        this.executorService.execute(createPriceReportRunnable(webScrape, customerOrder, siteName));
   }
   
   /*
    * creates the Runnable that will be executed by the executorService
    * and runs makeNewExcelFile()
    */
   private Runnable createPriceReportRunnable(File webScrape, File customerOrder, String sitename) {
          Runnable priceReport = () -> {
               PriceReportCreator priceReportCreator = new PriceReportCreator(webScrape, customerOrder, sitename);
               priceReportCreator.makeNewExcelFile();
          };
        return priceReport;  
   }

   /*
    * designates a thread with the task of consolidating reports
    */
   public void makeReportConsolidation(List<File> reports, File customerOrder) {
          this.executorService.execute(createReportConsolidatorRunnable(reports, customerOrder));
   }
   
   /*
    * create Runnable; runs consolidateReports()
    */
   private Runnable createReportConsolidatorRunnable(List<File> reports, File customerOrder) {
       Runnable consolidator = () -> {
          ReportConsolidator reportConsolidator = new ReportConsolidator(reports, customerOrder);
          reportConsolidator.consolidateReports();
       };
       return consolidator;
   }
}
