package tactical.blue.navigation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class SceneSwitchHandlerTest {

    private SceneSwitchHandler sceneSwitchHandler;

    @Mock
    private Stage stage;

    @Mock
    private Scene currentScene;

    @Mock
    private Scene newScene;

    @Mock
    private FadeTransition fadeTransition;

    @BeforeAll
    static void initToolkit() throws InterruptedException {
        // Initialize JavaFX Toolkit once before any tests run
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await(); // Wait for the toolkit to be initialized
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sceneSwitchHandler = new SceneSwitchHandler();
    }

    @AfterAll
    static void tearDown() {
        Platform.exit();
    }

    @Test
    public void testSwitchSceneFirstTime() throws Exception {
        when(stage.getScene()).thenReturn(null);
        when(newScene.getRoot()).thenReturn(new Group());
        String title = "New Scene Title";
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            sceneSwitchHandler.switchScene(newScene, title, stage);
            verify(stage).setScene(newScene);
            verify(stage).setTitle(title);
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testSwitchSceneSceneExists() throws Exception {
        when(stage.getScene()).thenReturn(currentScene);
        when(currentScene.getRoot()).thenReturn(new Group());
        when(newScene.getRoot()).thenReturn(new Group());
        String title = "New Scene Title";
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            sceneSwitchHandler.switchScene(newScene, title, stage);
            verify(stage, atLeastOnce()).getScene();
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        verify(stage, timeout(500)).setScene(newScene);
        verify(stage).setTitle(title);
    }

    @Test
    public void testFadeOut() throws Exception {
        when(stage.getScene()).thenReturn(currentScene);
        when(currentScene.getRoot()).thenReturn(new Group());
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            sceneSwitchHandler.fadeOut(stage);
            verify(stage, atLeastOnce()).getScene();
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
}

