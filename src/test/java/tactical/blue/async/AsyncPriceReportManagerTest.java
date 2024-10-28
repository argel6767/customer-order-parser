package tactical.blue.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AsyncPriceReportManagerTest {

    private AsyncPriceReportManager<Object> manager;
    private ExecutorService executor;

    @BeforeEach
    void setUp() {
        executor = Executors.newFixedThreadPool(2);
        manager = new AsyncPriceReportManager<>(executor);
    }

    @AfterEach
    void tearDown() {
        manager.shutdown();
    }

    @Test
    void testConstructorWithExecutor() {
        assertNotNull(manager.getExecutorService());
    }

    @Test
    void testShutdown() {
        // Before shutdown
        assertFalse(executor.isShutdown());

        manager.shutdown();

        // After shutdown
        assertTrue(executor.isShutdown());
    }

    @Test
    void testDoCSVParseTaskAsync() throws Exception {
        Function<Void, String> mockFunction = mock(Function.class);
        when(mockFunction.apply(null)).thenReturn("Test");

        CompletableFuture<String> future = manager.doCSVParseTaskAsync(mockFunction);

        assertEquals("Test", future.get());
        verify(mockFunction).apply(null);
    }

    @Test
    void testRunCSVParsingConcurrently() throws Exception {
        Function<Void, HashMap<String, List<String>>> mockMapFunction = mock(Function.class);
        Function<Void, List<String[]>> mockListFunction = mock(Function.class);
        BiConsumer<HashMap<String, List<String>>, List<String[]>> mockConsumer = mock(BiConsumer.class);

        when(mockMapFunction.apply(null)).thenReturn(new HashMap<>());
        when(mockListFunction.apply(null)).thenReturn(new ArrayList<String[]>(Collections.singleton(new String[]{"Item1"})));

        CompletableFuture<Void> future = (CompletableFuture<Void>) manager.runCSVParsingConcurrently(mockMapFunction, mockListFunction, mockConsumer);

        // Ensure tasks are submitted.
        assertFalse(future.isCompletedExceptionally());
        future.get();

        // Verify mock invocations
        verify(mockMapFunction).apply(null);
        verify(mockListFunction).apply(null);
        verify(mockConsumer).accept(any(), any());
    }
}