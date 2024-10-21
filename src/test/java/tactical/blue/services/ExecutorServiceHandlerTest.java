package tactical.blue.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ExecutorServiceHandlerTest {

    private ExecutorServiceHandler executorServiceHandler;
    private ExecutorService mockExecutorService;
    private File mockWebScrape;
    private File mockCustomerOrder;
    private List mockReports;
    private String siteName;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockExecutorService = Mockito.mock(ExecutorService.class);
        mockWebScrape = Mockito.mock(File.class);
        mockCustomerOrder = Mockito.mock(File.class);
        mockReports = Mockito.mock(List.class);
        siteName = "example.com";

        // Spy on the class so we can override the actual executorService with a mock one
        executorServiceHandler = Mockito.spy(new ExecutorServiceHandler(5));

          // Inject the mock executorService into the handler
        executorServiceHandler = new ExecutorServiceHandler(mockExecutorService);
    }

    @Test
    void testMakePriceReportAsync() {
        executorServiceHandler.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportAsyncReturnsCompletableFuture() {
        CompletableFuture<Void> task  = executorServiceHandler.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
        assertNotNull(task);
        assertInstanceOf(Future.class, task);
    }

    @Test
    void testMakeReportConsolidationAsync() {
        executorServiceHandler.makeReportConsolidationAsync(mockReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncReturnsCompletableFuture() {
        CompletableFuture<Void> task  = executorServiceHandler.makeReportConsolidationAsync(mockReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
        assertNotNull(task);
        assertInstanceOf(Future.class, task);
    }

    @Test
    void testMakePriceReportAsyncWithNullFiles() {
        executorServiceHandler.makePriceReportAsync(null, null, null);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncWithEmptyReports() {
        List<File> emptyReports = new ArrayList<>();
        executorServiceHandler.makeReportConsolidationAsync(emptyReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportAsyncMultipleTimes() {
        executorServiceHandler.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        executorServiceHandler.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        executorServiceHandler.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        verify(mockExecutorService, times(3)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncWithNullReportsList() {
        executorServiceHandler.makeReportConsolidationAsync(null, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportAsyncWithoutSiteName() {
        executorServiceHandler.makePriceReportAsync(mockWebScrape, mockCustomerOrder, null);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncWithMultipleReports() {
        List<File> multipleReports = new ArrayList<>();
        multipleReports.add(mock(File.class));
        multipleReports.add(mock(File.class));
        multipleReports.add(mock(File.class));
        executorServiceHandler.makeReportConsolidationAsync(multipleReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }
}

