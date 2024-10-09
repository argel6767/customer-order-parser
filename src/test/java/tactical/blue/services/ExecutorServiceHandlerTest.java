package tactical.blue.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.List;
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
        Mockito.doReturn(mockExecutorService).when(executorServiceHandler).getExecutorService();
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
}

