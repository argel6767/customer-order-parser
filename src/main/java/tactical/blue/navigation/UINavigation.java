package tactical.blue.navigation;

import java.io.FileNotFoundException;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import tactical.blue.ui.ConsolidateExcelFilesUIBuilder;
import tactical.blue.ui.MainPageUIBuilder;
import tactical.blue.ui.PriceReportCreatorUIBuilder;

/*
 * Will Handle the navigation between pages, as each method will be called by buttons such as back buttons
 * or option buttons, etc
 */
public class UINavigation {
    private Stage stage;
    private MainPageUIBuilder mainPageUIBuilder;
    private PriceReportCreatorUIBuilder priceReportCreatorUIBuilder;
    private ConsolidateExcelFilesUIBuilder consolidateExcelFilesUIBuilder;

    public UINavigation(Stage stage) throws FileNotFoundException {
        this.stage = stage;
        createUIBuilders(stage);
    }

    private void createUIBuilders(Stage stage) throws FileNotFoundException {
        mainPageUIBuilder = new MainPageUIBuilder(this, stage);
        priceReportCreatorUIBuilder = new PriceReportCreatorUIBuilder(this, stage);
        consolidateExcelFilesUIBuilder = new ConsolidateExcelFilesUIBuilder(this, stage);
    }

    public void setSceneToMainPage() throws FileNotFoundException {
        switchScene(mainPageUIBuilder.getScene(), "Blue Tactical Customer Order Parser");
    }

    public void setSceneToPriceReportCreator() throws FileNotFoundException {
        switchScene(priceReportCreatorUIBuilder.getScene(), "Scraped Data Price Report Creator");
    }

    public void setSceneToConsolidateExcelFiles() throws FileNotFoundException {
        switchScene(consolidateExcelFilesUIBuilder.getScene(), "Price Report Consolidator");
    }

     private void switchScene(Scene newScene, String title) {
        if (stage.getScene() == null) {
            stage.setScene(newScene);
            stage.setTitle(title);
            return;
        }
        FadeTransition fadeOut = new FadeTransition(Duration.millis(350), stage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            stage.setScene(newScene);
            stage.setTitle(title);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(350), newScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

}
