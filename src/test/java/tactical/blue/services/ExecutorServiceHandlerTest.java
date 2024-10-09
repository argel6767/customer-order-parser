package tactical.blue.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

class ExecutorServiceHandlerTest {

    private ExecutorServiceHandler executorServiceHandler;
    private ExecutorService mockExecutorService;
    private File mockWebScrape;
    private File mockCustomerOrder;
    private List<File> mockReports;
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
    void testMakePriceReport() {
        // Act
        executorServiceHandler.makePriceReport(mockWebScrape, mockCustomerOrder, siteName);

        // Assert
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidation() {
        // Act
        executorServiceHandler.makeReportConsolidation(mockReports, mockCustomerOrder);

        // Assert
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportWithNullFiles() {
        // Act
        executorServiceHandler.makePriceReport(null, null, null);

        // Assert
        // Verify that the executor still submits the task
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationWithEmptyReports() {
        // Setup
        List<File> emptyReports = new ArrayList<>();

        // Act
        executorServiceHandler.makeReportConsolidation(emptyReports, mockCustomerOrder);

        // Assert
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportMultipleTimes() {
        // Act
        executorServiceHandler.makePriceReport(mockWebScrape, mockCustomerOrder, siteName);
        executorServiceHandler.makePriceReport(mockWebScrape, mockCustomerOrder, siteName);
        executorServiceHandler.makePriceReport(mockWebScrape, mockCustomerOrder, siteName);

        // Assert
        verify(mockExecutorService, times(3)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationWithNullReportsList() {
        // Act
        executorServiceHandler.makeReportConsolidation(null, mockCustomerOrder);

        // Assert
        // Ensure that a thread task is still submitted
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakePriceReportWithoutSiteName() {
        // Act
        executorServiceHandler.makePriceReport(mockWebScrape, mockCustomerOrder, null);

        // Assert
        // Verify that the task was submitted even without a site name
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }

    @Test
    void testMakeReportConsolidationWithMultipleReports() {
        // Setup
        List<File> multipleReports = new ArrayList<>();
        multipleReports.add(mock(File.class));
        multipleReports.add(mock(File.class));
        multipleReports.add(mock(File.class));

        // Act
        executorServiceHandler.makeReportConsolidation(multipleReports, mockCustomerOrder);

        // Assert
        // Ensure the task is still submitted even with multiple files
        verify(mockExecutorService, times(1)).execute(any(Runnable.class));
    }
}

