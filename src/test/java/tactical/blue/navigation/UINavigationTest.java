package tactical.blue.navigation;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import tactical.blue.ui.*;

import java.io.FileNotFoundException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UINavigationTest {

    @Mock
    private Stage mockStage;

    @Mock
    private MainPageUIBuilder mockMainPageUIBuilder;

    @Mock
    private PriceReportCreatorUIBuilder mockPriceReportCreatorUIBuilder;

    @Mock
    private ConsolidateExcelFilesUIBuilder mockConsolidateExcelFilesUIBuilder;

    @Mock
    private Scene mockScene;

    @Mock
    private Scene mockCurrentScene;

    @Mock
    private Parent mockRoot;

    @Mock
    private ExecutorServiceHandler mockHandler;

    @InjectMocks
    private UINavigation uiNavigation;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        // The MockitoExtension handles mock initialization and injection.
        // No need for manual initialization.
    }

    @Test
    void testSetSceneToMainPage_FirstTime() throws FileNotFoundException {
        // Arrange
        when(mockMainPageUIBuilder.getScene()).thenReturn(mockScene);
        when(mockStage.getScene()).thenReturn(null);

        // Act
        uiNavigation.setSceneToMainPage();

        // Assert
        verify(mockMainPageUIBuilder).getScene();
        verify(mockStage).getScene();
        verify(mockStage).setScene(mockScene);
        verify(mockStage).setTitle("Blue Tactical Customer Order Parser");
    }

    @Test
    void testSetSceneToMainPage_WhenSceneExists() throws FileNotFoundException {
        // Arrange
        when(mockMainPageUIBuilder.getScene()).thenReturn(mockScene);
        when(mockStage.getScene()).thenReturn(mockCurrentScene);
        when(mockCurrentScene.getRoot()).thenReturn(mockRoot);

        // Act
        uiNavigation.setSceneToMainPage();

        // Assert
        verify(mockMainPageUIBuilder).getScene();
        verify(mockStage, times(2)).getScene(); // getScene is called twice in switchScene
        verify(mockCurrentScene).getRoot();
    }

    @Test
    void testSetSceneToPriceReportCreator_FirstTime() throws FileNotFoundException {
        // Arrange
        when(mockPriceReportCreatorUIBuilder.getScene()).thenReturn(mockScene);
        when(mockStage.getScene()).thenReturn(null);

        // Act
        uiNavigation.setSceneToPriceReportCreator();

        // Assert
        verify(mockPriceReportCreatorUIBuilder).getScene();
        verify(mockStage).getScene();
        verify(mockStage).setScene(mockScene);
        verify(mockStage).setTitle("Scraped Data Price Report Creator");
    }

    @Test
    void testSetSceneToPriceReportCreator_WhenSceneExists() throws FileNotFoundException {
        // Arrange
        when(mockPriceReportCreatorUIBuilder.getScene()).thenReturn(mockScene);
        when(mockStage.getScene()).thenReturn(mockCurrentScene);
        when(mockCurrentScene.getRoot()).thenReturn(mockRoot);

        // Act
        uiNavigation.setSceneToPriceReportCreator();

        // Assert
        verify(mockPriceReportCreatorUIBuilder).getScene();
        verify(mockStage, times(2)).getScene();
        verify(mockCurrentScene).getRoot();
    }

    @Test
    void testSetSceneToConsolidateExcelFiles_FirstTime() throws FileNotFoundException {
        // Arrange
        when(mockConsolidateExcelFilesUIBuilder.getScene()).thenReturn(mockScene);
        when(mockStage.getScene()).thenReturn(null);

        // Act
        uiNavigation.setSceneToConsolidateExcelFiles();

        // Assert
        verify(mockConsolidateExcelFilesUIBuilder).getScene();
        verify(mockStage).getScene();
        verify(mockStage).setScene(mockScene);
        verify(mockStage).setTitle("Price Report Consolidator");
    }

    @Test
    void testSetSceneToConsolidateExcelFiles_WhenSceneExists() throws FileNotFoundException {
        // Arrange
        when(mockConsolidateExcelFilesUIBuilder.getScene()).thenReturn(mockScene);
        when(mockStage.getScene()).thenReturn(mockCurrentScene);
        when(mockCurrentScene.getRoot()).thenReturn(mockRoot);

        // Act
        uiNavigation.setSceneToConsolidateExcelFiles();

        // Assert
        verify(mockConsolidateExcelFilesUIBuilder).getScene();
        verify(mockStage, times(2)).getScene();
        verify(mockCurrentScene).getRoot();
    }
}
