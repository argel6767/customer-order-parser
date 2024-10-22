package tactical.blue.ui;

import java.util.List;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.concurrent.CompletableFuture;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;
import tactical.blue.services.ExecutorServiceHandler;

public class ConsolidateExcelFilesUIBuilder extends UIComponents{

    private final List<File> excelFilesUploaded = new ArrayList<>();
    private File customerOrderInfoFile;

    public ConsolidateExcelFilesUIBuilder(UINavigation uiNavigation, Stage primaryStage, ExecutorServiceHandler handler) throws FileNotFoundException {
        super(uiNavigation, handler);
        build(primaryStage);
    }

    
    private void build(Stage primaryStage) throws FileNotFoundException {
        VBox container = createConsolidatorUIVBox(primaryStage);
        super.setContainer(container);
        super.setScene(new Scene(container, getPageWidth(), getPageHeight()));
    }

    @Override
    public Scene getScene() {
        return super.getScene();
    }

    /*
     * Creates Button that allows uploading a customer order data file
     */
    private Button createCustomerOrderFileButton(Stage primaryStage, FileChooser fileChooser) {
        Button button = new Button("Upload Customer Order Data Here");
        
        button.setOnAction(e -> {
            this.customerOrderInfoFile= fileChooser.showOpenDialog(primaryStage); 
            System.out.println("Selected file: " +this.customerOrderInfoFile.getAbsolutePath());
        });
        button.setStyle(getButtonStyle());
        return button;
    }

    /*
     * Creates Button that allows the ReportConsolidator object to be made and run .consolidateReports() on separate thread
     */
    private Button createRunConsolidateProgramButton() {
        Button button = new Button("Consolidate Files");
        
        button.setOnAction(e -> {
            CompletableFuture<Void> task = getHandler().makeReportConsolidationAsync(excelFilesUploaded, customerOrderInfoFile);
            showStatusText();
            String originalText = getTextObject().getText();
            updateTextStatus(task,"Reports consolidated! Check the Weekly-Reports folder.", originalText);
        });
        button.setStyle(getButtonStyle());
        return button;
    }

    /*
     * Creates Button that allows user to upload all price reports created to combine
     */
    private Button createMulitpleFilesButton(FileChooser fileChooser, Stage stage) {
        Button button = new Button("Upload Files");

        button.setOnAction(e -> {
            List<File> files = fileChooser.showOpenMultipleDialog(stage);
            if (files != null) {
                this.excelFilesUploaded.addAll(files);
                excelFilesUploaded.forEach(file -> System.out.println("File Uploaded: " + file));
            }
        });
        button.setStyle(getButtonStyle());
        return button;
    }

    /*
     * Creates container that holds all UI Components 
     */
    private VBox createConsolidatorUIVBox(Stage primaryStage) throws FileNotFoundException {
        VBox vBox = new VBox(createLogoBox(), createMulitpleFilesButton(new FileChooser(), primaryStage),createCustomerOrderFileButton(primaryStage, createFileChooser("Upload Customer Information")),createRunConsolidateProgramButton(),createGoBackAndEndProgramButtonsHBox(), createStatusText("Consolidating files..."));
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle(getContainerStyle());
        return vBox;
    }

}
