package tactical.blue.navigation;

import java.io.FileNotFoundException;

import javafx.stage.Stage;
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
    private final Stage stage;
    private final int THREADS_COUNT = 5; //number of threads (currently a price report + 3 reports consolidated + 1)
    private UIThreadExecutor handler = new UIThreadExecutor(THREADS_COUNT);
    private MainPageUIBuilder mainPageUIBuilder;
    private PriceReportCreatorUIBuilder priceReportCreatorUIBuilder;
    private ConsolidateExcelFilesUIBuilder consolidateExcelFilesUIBuilder;
    private ShuttingDownUIBuilder shuttingDownUIBuilder;
    private final SceneSwitchHandler sceneSwitchHandler = new SceneSwitchHandler();

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
        shuttingDownUIBuilder = new ShuttingDownUIBuilder(this, handler);
    }

    /*
     * When called switches the scene to the Main page
     */
    public void setSceneToMainPage() throws FileNotFoundException {
        sceneSwitchHandler.switchScene(mainPageUIBuilder.getScene(), "Blue Tactical Customer Order Parser", stage);
    }

    /*
     * When called switches scene to the Price Report Creator page
     */
    public void setSceneToPriceReportCreator() throws FileNotFoundException {
        sceneSwitchHandler.switchScene(priceReportCreatorUIBuilder.getScene(), "Scraped Data Price Report Creator", stage);
    }

    /*
     * When called switches scene to the Consolidate Excel Files page
     */
    public void setSceneToConsolidateExcelFiles() throws FileNotFoundException {
        sceneSwitchHandler.switchScene(consolidateExcelFilesUIBuilder.getScene(), "Price Report Consolidator", stage);
    }

    public void setSceneToShuttingDown() {
        sceneSwitchHandler.switchScene(shuttingDownUIBuilder.getScene(), "Shutting Down", stage);
        sceneSwitchHandler.fadeOut(stage);
    }

}