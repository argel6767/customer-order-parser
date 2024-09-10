package tactical.blue.ui;

import java.io.FileNotFoundException;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;

public class MainPageUIBuilder extends UIElements{


    public MainPageUIBuilder(UINavigation uiNavigation, Stage primaryStage) throws FileNotFoundException {
        super(uiNavigation);
        build(primaryStage);
    }

    public Scene getScene() {
        return super.getScene();
    }

    public void build(Stage primaryStage) throws FileNotFoundException {
        HBox hBox = new HBox(createLogoImageView());
        Button buttonGoToPriceReport = createGoToPriceReportPage("Create A Price Report");
        Button buttonGoToConsolidateExcelFiles = createGoToConsolidateExcelFilesPage("Consolidate Price Reports Created");
        HBox buttonContainer = new HBox(buttonGoToPriceReport, buttonGoToConsolidateExcelFiles);
        buttonContainer.setSpacing(10);
        VBox vBox = new VBox(hBox, buttonContainer);
        super.setScene(new Scene(vBox, getPageWidth(), getPageHeight()));
    }
}
