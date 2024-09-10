package tactical.blue.navigation;

import java.io.FileNotFoundException;

import javafx.stage.Stage;
import tactical.blue.ui.MainPageUIBuilder;
import tactical.blue.ui.PriceReportCreatorUIBuilder;

public class UINavigation {
    private Stage stage;

    public UINavigation(Stage stage) {
        this.stage = stage;
    }

    public void setStageToMainPage() throws FileNotFoundException {
        MainPageUIBuilder mainPageUIBuilder = new MainPageUIBuilder(this, stage);
        stage.setScene(mainPageUIBuilder.getScene());
    }

    public void setStageToPriceReportCreator() throws FileNotFoundException {
        PriceReportCreatorUIBuilder priceReportCreatorUIBuilder = new PriceReportCreatorUIBuilder(this, stage);
        stage.setScene(priceReportCreatorUIBuilder.getScene());
    }

}
