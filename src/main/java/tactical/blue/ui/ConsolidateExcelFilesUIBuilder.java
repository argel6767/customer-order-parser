package tactical.blue.ui;

import java.io.FileNotFoundException;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;

public class ConsolidateExcelFilesUIBuilder extends UIElements{

    public ConsolidateExcelFilesUIBuilder(UINavigation uiNavigation, Stage primaryStage) throws FileNotFoundException {
        super(uiNavigation);
        build(primaryStage);
    }

    

    public Scene getScene() {
        return super.getScene();
    }

    private void build(Stage primaryStage) throws FileNotFoundException {
        HBox hBox = new HBox(createLogoImageView());

        super.setScene(new Scene(hBox));
    }

}
