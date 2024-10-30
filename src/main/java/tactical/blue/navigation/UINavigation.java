package tactical.blue.navigation;

import java.io.FileNotFoundException;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import tactical.blue.async.UIThreadExecutor;
import tactical.blue.ui.ConsolidateExcelFilesUIBuilder;
import tactical.blue.ui.MainPageUIBuilder;
import tactical.blue.ui.PriceReportCreatorUIBuilder;
import tactical.blue.ui.ShuttingDownUIBuilder;

/*
 * Will Handle the navigation between pages, as each method will be called by buttons such as back buttons
 * or option buttons, etc
 */
public class UINavigation {
    private Stage stage;
    private final int THREADS_COUNT = 5; //number of threads (currently a price report + 3 reports consolidated + 1)
    private UIThreadExecutor handler = new UIThreadExecutor(THREADS_COUNT);
    private MainPageUIBuilder mainPageUIBuilder;
    private PriceReportCreatorUIBuilder priceReportCreatorUIBuilder;
    private ConsolidateExcelFilesUIBuilder consolidateExcelFilesUIBuilder;
    private ShuttingDownUIBuilder shuttingDownUIBuilder;

    /*
    * For Testing Purposes
     */
    public UINavigation(Stage stage, UIThreadExecutor handler, MainPageUIBuilder mainPageUIBuilder, PriceReportCreatorUIBuilder priceReportCreatorUIBuilder, ConsolidateExcelFilesUIBuilder consolidateExcelFilesUIBuilder, ShuttingDownUIBuilder shuttingDownUIBuilder) {
        this.stage = stage;
        this.handler = handler;
        this.mainPageUIBuilder = mainPageUIBuilder;
        this.priceReportCreatorUIBuilder = priceReportCreatorUIBuilder;
        this.consolidateExcelFilesUIBuilder = consolidateExcelFilesUIBuilder;
        this.shuttingDownUIBuilder = shuttingDownUIBuilder;
    }

    public UINavigation(Stage stage) throws FileNotFoundException {
        this.stage = stage;
        createUIBuilders(stage);
    }

    /*
    * Creates each new UI Page using the UINavigation itself, the stage, and handler
     */
    private void createUIBuilders(Stage stage) throws FileNotFoundException {
        mainPageUIBuilder = new MainPageUIBuilder(this, stage, handler);
        priceReportCreatorUIBuilder = new PriceReportCreatorUIBuilder(this, stage, handler);
        consolidateExcelFilesUIBuilder = new ConsolidateExcelFilesUIBuilder(this, stage, handler);
    }

    /*
     * When called switches the scene to the Main page
     */
    public void setSceneToMainPage() throws FileNotFoundException {
        switchScene(mainPageUIBuilder.getScene(), "Blue Tactical Customer Order Parser");
    }

    /*
     * When called switches scene to the Price Report Creator page
     */
    public void setSceneToPriceReportCreator() throws FileNotFoundException {
        switchScene(priceReportCreatorUIBuilder.getScene(), "Scraped Data Price Report Creator");
    }

    /*
     * When called switches scene to the Consolidate Excel Files page
     */
    public void setSceneToConsolidateExcelFiles() throws FileNotFoundException {
        switchScene(consolidateExcelFilesUIBuilder.getScene(), "Price Report Consolidator");
    }

    public void setSceneToShuttingDown() {
        switchScene();
    }

    /*
     * Houses the switching of scenes logic
     * checks if the current scene is null currently (ie the application was just booted up), if so the newScene is set as the current scene
     * as well with the title parameter
     * Otherwise a FadeTransition object is made to allow for there to be a transation affect between pages to give a more natural transition feel
     */
     private void switchScene(Scene newScene, String title) {
        if (stage.getScene() == null) {
            setSceneValues(newScene, title);
            getFadeIn(newScene, 1000);
        }
        else {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(350), stage.getScene().getRoot());
            setFadeValues(fadeOut, 1, 0);
            fadeOut.setOnFinished(event -> {
                setSceneValues(newScene, title);
                getFadeIn(newScene, 350);
            });
            fadeOut.play();
        }
    }

    /*
     * Sets necessary values for stage
     */
    private void setSceneValues(Scene newScene, String title) {
        stage.setScene(newScene);
        stage.setTitle(title);
    }

    /*
     * Creates a FadeTransition object that transitions into scene
     */
    private void getFadeIn(Scene newScene, int time) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(time), newScene.getRoot());
        setFadeValues(fadeIn, 0, 1);
        fadeIn.play();
    }

    /*
     * Sets necessary fade values for transition
     */
    private void setFadeValues(FadeTransition fade, double from, double to) {
        fade.setFromValue(from);
        fade.setToValue(to);
    }

}
