package tactical.blue.async;

import javafx.application.Platform;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatusTextStateManagerTest {
    @Mock
    private Text mockText;

    private StatusTextStateManager manager;
    private static final String ORIGINAL_TEXT = "Original Status";
    private static final String NEW_STATUS = "Task Completed";

    @BeforeAll
    public static void initToolkit() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        // Toolkit initialized
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    void setUp() {
        when(mockText.getText()).thenReturn(ORIGINAL_TEXT);
        when(mockText.isVisible()).thenReturn(false);
        manager = new StatusTextStateManager(mockText);
    }

    @Test
    void testConstructorInitializesWithOriginalText() {
        verify(mockText).getText();
        assertEquals(mockText, manager.getStatusText());
    }

    @Test
    void testShowStatusTextMakesVisible() {
        manager.showStatusText();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(latch::countDown);
        // Wait for all Platform.runLater calls to complete
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("Test timed out");
        }
        verify(mockText).setVisible(true);

    }

    @Test
    void testHideStatusTextMakesInvisible() {
        manager.hideStatusText();
        verify(mockText).setVisible(false);
        assertFalse(manager.getStatusText().isVisible());
    }

    @Test
    void testSetStatusTextUpdatesReference() {
        Text newText = new Text();
        manager.setStatusText(newText);
        assertEquals(newText, manager.getStatusText());
    }

    @Test
    void testUpdateTextStatusCompletesSuccessfully() throws Exception {
        CompletableFuture<Void> task = CompletableFuture.completedFuture(null); // Ensure task is completed
        CompletableFuture<Void> result = manager.updateTextStatus(task, NEW_STATUS);
        result.get(1, TimeUnit.SECONDS); // Wait for the task to complete
        verify(mockText, timeout(100)).setText(NEW_STATUS);
        verify(mockText, timeout(3600)).setText(ORIGINAL_TEXT);
    }

    @Test
    void testFailedTaskDoesNotUpdateText() {
        CompletableFuture<Void> failedTask = new CompletableFuture<>();
        failedTask.completeExceptionally(new RuntimeException("Task failed"));
        CompletableFuture<Void> result = manager.updateTextStatus(failedTask, NEW_STATUS);
        assertTrue(result.isCompletedExceptionally());
        assertEquals(mockText.getText(), ORIGINAL_TEXT);
        verify(mockText, never()).setText(NEW_STATUS);
    }

    @Test
    void testCancelledTaskDoesNotUpdateText() {
        CompletableFuture<Void> cancelledTask = new CompletableFuture<>();
        cancelledTask.cancel(true);
        CompletableFuture<Void> result = manager.updateTextStatus(cancelledTask, NEW_STATUS);
        assertNull(result.getNow(null));
        assertEquals(mockText.getText(), ORIGINAL_TEXT);
        verify(mockText, never()).setText(NEW_STATUS);
    }
}
