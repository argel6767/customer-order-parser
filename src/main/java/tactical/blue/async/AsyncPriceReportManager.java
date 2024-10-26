package tactical.blue.async;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;

/*
 * This class holds the managing of tasks that will happen concurrently in PriceReportCreator
 * ie reading both CSV files at the same time for greater efficiency
 */
public class AsyncPriceReportManager {

    private final ExecutorService executor;

    public AsyncPriceReportManager() {
        this.executor = Executors.newFixedThreadPool(2);
    }

    /*
     * Testing Constructor
     */
    public AsyncPriceReportManager(ExecutorService executor) {
        this.executor = executor;
    }

    /*
     * TODO make this method once the bottom two are fixed!!!
     *  general format : take the two functions in and then do their work and once the two are done do the creation of the Price Report!!!
     */
    public void runCSVParsingConcurrently(Function<Void, CompletableFuture<HashMap<String, List<String>>>> mapScrapedRowsTask,
                                          Function<Void, List<String[]>> getOrderInfoRows,
                                          BiConsumer<HashMap<String, List<String>>, List<String[]>> parseScrapedRowsToExcelRowsTask) {

    }

    /*
     * submits the task to the ExecutorService
     */
    //TODO Replace this with supplyAsync, runAsync does not return a value!!!
    public CompletableFuture<?> doCSVParseTaskAsync(Function<Void, CompletableFuture<?>> function) {
        return CompletableFuture.runAsync(createCSVParsingRunnable(function), executor);
    }

    /*
     * creates the Runnable to run Async, using a Function allows for either task to work
     */
    //TODO replace with a Supplier, Runnable does not return the value needed!!!
    private Runnable createCSVParsingRunnable(Function<Void, CompletableFuture<?>> function) {
        return () -> {
            function.apply(null);
        };
    }
}
