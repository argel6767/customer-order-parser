package tactical.blue.async;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/*
 * This class holds the managing of tasks that will happen concurrently in PriceReportCreator
 * ie reading both CSV files at the same time for greater efficiency
 */
public class AsyncPriceReportManager<T> {

    private final ExecutorService executor;

    /*
     * makes an ExecutorService with only two threads
     */
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
     * shuts down the ExecutorService
     */
    public void shutdown() {
        executor.shutdown();
    }

    /*
     * getter
     */
    public ExecutorService getExecutorService() {
        return this.executor;
    }

    /*
     * tasks in two Suppliers than does both tasks asynchronously
     * once both are done their return values are used by the BiConsumer, ie creating using the data to make excel rows,
     * to do more work
     */
    public CompletableFuture<?> runCSVParsingConcurrently(Supplier<HashMap<String, List<String[]>>> mapScrapedRows, Supplier<List<String[]>> getOrderInfoRows,
                                                          BiConsumer<HashMap<String, List<String[]>>, List<String[]>> parseScrapedRowsToExcelRowsTask) {
        CompletableFuture<HashMap<String, List<String[]>>> mapScrapedRowsTask = doCSVParseTaskAsync(mapScrapedRows);
        CompletableFuture<List<String[]>> getOrderInfoRowsTask = doCSVParseTaskAsync(getOrderInfoRows);
        return mapScrapedRowsTask.thenAcceptBoth(getOrderInfoRowsTask, parseScrapedRowsToExcelRowsTask);
    }

    /*
     * submits the task to the ExecutorService
     */
    <T> CompletableFuture<T> doCSVParseTaskAsync(Supplier<T> function) {
        return CompletableFuture.supplyAsync(function, executor);
    }

}
