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
import java.util.function.Supplier;

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
        assertFalse(executor.isShutdown());
        manager.shutdown();
        assertTrue(executor.isShutdown());
    }

    @Test
    void testDoCSVParseTaskAsync() throws Exception {
        Supplier<String> mockSupplier = mock(Supplier.class);
        when(mockSupplier.get()).thenReturn("Test");
        CompletableFuture<String> future = manager.doCSVParseTaskAsync(mockSupplier);
        assertEquals("Test", future.get());
        verify(mockSupplier).get();
    }

    @Test
    void testRunCSVParsingConcurrently() throws Exception {
        Supplier<HashMap<String, List<String[]>>> mockMapSupplier = mock(Supplier.class);
        Supplier<List<String[]>> mockListSupplier = mock(Supplier.class);
        BiConsumer<HashMap<String, List<String[]>>, List<String[]>> mockConsumer = mock(BiConsumer.class);
        when(mockMapSupplier.get()).thenReturn(new HashMap<>());
        when(mockListSupplier.get()).thenReturn(new ArrayList<String[]>(Collections.singleton(new String[]{"Item1"})));
        var future =  manager.runCSVParsingConcurrently(mockMapSupplier, mockListSupplier, mockConsumer);
        assertFalse(future.isCompletedExceptionally());
        future.get();
        verify(mockMapSupplier).get();
        verify(mockListSupplier).get();
        verify(mockConsumer).accept(any(), any());
    }
}