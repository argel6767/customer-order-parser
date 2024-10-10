package tactical.blue.navigation;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import tactical.blue.services.ExecutorServiceHandler;
import tactical.blue.ui.*;

import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class UINavigationTest {

    private Stage stage;
    private ExecutorServiceHandler mockHandler;
    private MainPageUIBuilder mockMainPageUIBuilder;
    private PriceReportCreatorUIBuilder mockPriceReportCreatorUIBuilder;
    private ConsolidateExcelFilesUIBuilder mockConsolidateExcelFilesUIBuilder;
    private UINavigation uiNavigation;

    @BeforeAll
    static void initToolkit() throws InterruptedException {
        // Initialize JavaFX Toolkit once before any tests run
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        latch.await(); // Wait for the toolkit to be initialized
    }

    @BeforeEach
    void setUp() throws Exception {
        // Use a CountDownLatch to ensure JavaFX code runs on the correct thread
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // Create real Stage and other JavaFX components
            stage = new Stage();

            // Create mock or stub implementations of your UI builders
            mockHandler = new ExecutorServiceHandler(5);
            try {
                mockMainPageUIBuilder = new StubMainPageUIBuilder();
                mockPriceReportCreatorUIBuilder = new StubPriceReportCreatorUIBuilder();
                mockConsolidateExcelFilesUIBuilder = new StubConsolidateExcelFilesUIBuilder();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            // Instantiate UINavigation with injected dependencies
            uiNavigation = new UINavigation(stage, mockHandler,
                    mockMainPageUIBuilder,
                    mockPriceReportCreatorUIBuilder,
                    mockConsolidateExcelFilesUIBuilder);

            latch.countDown(); // Signal that setup is complete
        });

        latch.await(); // Wait for the setup to complete before proceeding with tests
    }

    @Test
    void testSetSceneToMainPage_FirstTime() throws Exception {
        // Use CountDownLatch to ensure test actions are executed on JavaFX thread
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                uiNavigation.setSceneToMainPage();
                latch.countDown(); // Signal that the UI action is complete
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        latch.await(); // Wait for the scene change to complete

        // Assert
        assertEquals("Blue Tactical Customer Order Parser", stage.getTitle());
        assertNotNull(stage.getScene());
    }

    @Test
    void testSetSceneToPriceReportCreator_FirstTime() throws Exception {
        // Use CountDownLatch to ensure test actions are executed on JavaFX thread
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                uiNavigation.setSceneToPriceReportCreator();
                latch.countDown(); // Signal that the UI action is complete
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        latch.await(); // Wait for the scene change to complete

        // Assert
        assertEquals("Scraped Data Price Report Creator", stage.getTitle());
        assertNotNull(stage.getScene());
    }

    @Test
    void testSetSceneToConsolidateExcelFiles_FirstTime() throws Exception {
        // Use CountDownLatch to ensure test actions are executed on JavaFX thread
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                uiNavigation.setSceneToConsolidateExcelFiles();
                latch.countDown(); // Signal that the UI action is complete
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        latch.await(); // Wait for the scene change to complete

        // Assert
        assertEquals("Price Report Consolidator", stage.getTitle());
        assertNotNull(stage.getScene());
    }

    // Stub implementations of your UI builders
    class StubMainPageUIBuilder extends MainPageUIBuilder {
        public StubMainPageUIBuilder() throws FileNotFoundException {
            super(null, null, null);
        }

        @Override
        public Scene getScene() {
            return new Scene(new Parent() {});
        }
    }

    class StubPriceReportCreatorUIBuilder extends PriceReportCreatorUIBuilder {
        public StubPriceReportCreatorUIBuilder() throws FileNotFoundException {
            super(null, null, null);
        }

        @Override
        public Scene getScene() {
            return new Scene(new Parent() {});
        }
    }

    class StubConsolidateExcelFilesUIBuilder extends ConsolidateExcelFilesUIBuilder {
        public StubConsolidateExcelFilesUIBuilder() throws FileNotFoundException {
            super(null, null, null);
        }

        @Override
        public Scene getScene() {
            return new Scene(new Parent() {});
        }
    }
}
