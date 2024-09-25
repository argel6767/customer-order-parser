package tactical.blue.navigation;

import java.io.FileNotFoundException;

import javafx.stage.Stage;
import tactical.blue.ui.ConsolidateExcelFilesUIBuilder;
import tactical.blue.ui.MainPageUIBuilder;
import tactical.blue.ui.PriceReportCreatorUIBuilder;

/*
 * Will Handle the navigation between pages, as each method will be called by buttons such as back buttons
 * or option buttons, etc
 */
public class UINavigation {
    private Stage stage;

    public UINavigation(Stage stage) {
        this.stage = stage;
    }

    public void setSceneToMainPage() throws FileNotFoundException {
        MainPageUIBuilder mainPageUIBuilder = new MainPageUIBuilder(this, stage);
        stage.setScene(mainPageUIBuilder.getScene());
        stage.setTitle("Blue Tactical Customer Order Parser");
    }

    public void setSceneToPriceReportCreator() throws FileNotFoundException {
        PriceReportCreatorUIBuilder priceReportCreatorUIBuilder = new PriceReportCreatorUIBuilder(this, stage);
        stage.setScene(priceReportCreatorUIBuilder.getScene());
        stage.setTitle("Scraped Data Price Report Creator");
    }

    public void setSceneToConsolidateExcelFiles() throws FileNotFoundException {
        ConsolidateExcelFilesUIBuilder consolidateExcelFilesUIBuilder = new ConsolidateExcelFilesUIBuilder(this, stage);
        stage.setScene(consolidateExcelFilesUIBuilder.getScene());
        stage.setTitle("Price Report Consolidator");
    }

}
