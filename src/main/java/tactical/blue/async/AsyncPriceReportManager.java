package tactical.blue.async;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * This class holds the managing of tasks that will happen concurrently in PriceReportCreator
 * ie reading both CSV files at the same time for greater efficiency
 */
public class AsyncPriceReportManager<T> {

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
    public void runCSVParsingConcurrently(Function<Void, HashMap<String, List<String>>> mapScrapedRows, Function<Void, List<String[]>> getOrderInfoRows,
                                          BiConsumer<HashMap<String, List<String>>, List<String[]>> parseScrapedRowsToExcelRowsTask) {
        CompletableFuture<HashMap<String, List<String>>> mapScrapedRowsTask = doCSVParseTaskAsync(mapScrapedRows);
        CompletableFuture<List<String[]>> getOrderInfoRowsTask = doCSVParseTaskAsync(getOrderInfoRows);
        CompletableFuture<Void> parsingDone = mapScrapedRowsTask.thenAcceptBoth(getOrderInfoRowsTask, parseScrapedRowsToExcelRowsTask);
    }

    /*
     * submits the task to the ExecutorService
     */
    public <T> CompletableFuture<T> doCSVParseTaskAsync(Function<Void, T> function) {
        return CompletableFuture.supplyAsync(() -> function.apply(null), executor);
    }

}
