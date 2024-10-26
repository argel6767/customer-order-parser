package tactical.blue.async;

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

public class UIThreadExecutorTest {

    private UIThreadExecutor UIThreadExecutor;
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
        UIThreadExecutor = Mockito.spy(new UIThreadExecutor(5));

          // Inject the mock executorService into the handler
        UIThreadExecutor = new UIThreadExecutor(mockExecutorService);
    }

    @Test
    void testMakePriceReportAsync() {
        UIThreadExecutor.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportAsyncReturnsCompletableFuture() {
        CompletableFuture<Void> task  = UIThreadExecutor.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
        assertNotNull(task);
        assertInstanceOf(Future.class, task);
    }

    @Test
    void testMakeReportConsolidationAsync() {
        UIThreadExecutor.makeReportConsolidationAsync(mockReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncReturnsCompletableFuture() {
        CompletableFuture<Void> task  = UIThreadExecutor.makeReportConsolidationAsync(mockReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
        assertNotNull(task);
        assertInstanceOf(Future.class, task);
    }

    @Test
    void testMakePriceReportAsyncWithNullFiles() {
        UIThreadExecutor.makePriceReportAsync(null, null, null);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncWithEmptyReports() {
        List<File> emptyReports = new ArrayList<>();
        UIThreadExecutor.makeReportConsolidationAsync(emptyReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportAsyncMultipleTimes() {
        UIThreadExecutor.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        UIThreadExecutor.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        UIThreadExecutor.makePriceReportAsync(mockWebScrape, mockCustomerOrder, siteName);
        verify(mockExecutorService, times(3)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncWithNullReportsList() {
        UIThreadExecutor.makeReportConsolidationAsync(null, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportAsyncWithoutSiteName() {
        UIThreadExecutor.makePriceReportAsync(mockWebScrape, mockCustomerOrder, null);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationAsyncWithMultipleReports() {
        List<File> multipleReports = new ArrayList<>();
        multipleReports.add(mock(File.class));
        multipleReports.add(mock(File.class));
        multipleReports.add(mock(File.class));
        UIThreadExecutor.makeReportConsolidationAsync(multipleReports, mockCustomerOrder);
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }
}

