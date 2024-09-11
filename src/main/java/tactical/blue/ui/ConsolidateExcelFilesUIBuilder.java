package tactical.blue.ui;

import java.util.List;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.File;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;

public class ConsolidateExcelFilesUIBuilder extends UIElements{

    private List<File> excelFilesUploaded = new ArrayList<>();

    public ConsolidateExcelFilesUIBuilder(UINavigation uiNavigation, Stage primaryStage) throws FileNotFoundException {
        super(uiNavigation);
        build(primaryStage);
    }

    

    public Scene getScene() {
        return super.getScene();
    }

    private FileChooser mutitpleFilesChooser() {
        FileChooser filesChooser = new FileChooser();
        return filesChooser;
    }

    private Button createMulitpleFilesButton() {
        Button button = new Button("Upload Files");
    }

    private void build(Stage primaryStage) throws FileNotFoundException {
        VBox vBox = new VBox(createLogoBox(), createGoBackAndEndProgramButtonsHBox());
        vBox.setSpacing(20);

        super.setScene(new Scene(vBox, getPageWidth(), getPageHeight()));
    }

}
