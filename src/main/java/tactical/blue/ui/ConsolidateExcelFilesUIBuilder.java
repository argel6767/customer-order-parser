package tactical.blue.ui;

import java.util.List;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;
import tactical.blue.services.ReportConsolidator;

public class ConsolidateExcelFilesUIBuilder extends UIElements{

    private List<File> excelFilesUploaded = new ArrayList<>();

    public ConsolidateExcelFilesUIBuilder(UINavigation uiNavigation, Stage primaryStage) throws FileNotFoundException {
        super(uiNavigation);
        build(primaryStage);
    }

    public void build(Stage primaryStage) throws FileNotFoundException {
        VBox vBox = new VBox(createLogoBox(), createMulitpleFilesButton(new FileChooser(), primaryStage),createRunConsolidateProgramButton(),createGoBackAndEndProgramButtonsHBox());
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.TOP_CENTER);

        super.setScene(new Scene(vBox, getPageWidth(), getPageHeight()));
    }

    public Scene getScene() {
        return super.getScene();
    }


    private Button createRunConsolidateProgramButton() {
        Button button = new Button("Consolidate Files");
        
        button.setOnAction(e -> {
            ReportConsolidator reportConsolidator = new ReportConsolidator(excelFilesUploaded);
            reportConsolidator.consolidateReports();
        });
        button.setStyle(getButtonStyle());
        return button;
    }

    private Button createMulitpleFilesButton(FileChooser fileChooser, Stage stage) {
        Button button = new Button("Upload Files");

        button.setOnAction(e -> {
            List<File> files = fileChooser.showOpenMultipleDialog(stage);
            if (files != null) {
                this.excelFilesUploaded.addAll(files);
                excelFilesUploaded.forEach(file -> {
                    System.out.println("File Uploaded: " + file);
                });
            }
        });
        button.setStyle(getButtonStyle());
        return button;
    }


}
