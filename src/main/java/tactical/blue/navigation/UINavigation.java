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
        stage.setScene(mainPageUIBuilder.getScene());
        stage.setTitle("Blue Tactical Customer Order Parser");
    }

    public void setSceneToPriceReportCreator() throws FileNotFoundException {
        stage.setScene(priceReportCreatorUIBuilder.getScene());
        stage.setTitle("Scraped Data Price Report Creator");
    }

    public void setSceneToConsolidateExcelFiles() throws FileNotFoundException {
        stage.setScene(consolidateExcelFilesUIBuilder.getScene());
        stage.setTitle("Price Report Consolidator");
    }


}
