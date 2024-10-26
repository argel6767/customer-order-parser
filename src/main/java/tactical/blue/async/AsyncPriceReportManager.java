package tactical.blue.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    public void runCSVParsingConcurrently() {

    }

    /*
     * submits the task to the ExecutorService
     */
    public CompletableFuture<?> doCSVParseTaskAsync(Function<Void, CompletableFuture<?>> function) {
        return CompletableFuture.runAsync(createCSVParsingRunnable(function), executor);
    }

    /*
     * creates the Runnable to run Async, using a Function allows for either task to work
     */
    private Runnable createCSVParsingRunnable(Function<Void, CompletableFuture<?>> function) {
        return () -> {
            function.apply(null);
        };
    }
}
