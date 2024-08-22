package tactical.blue;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import tactical.blue.excel.CleanExcelFile;

public class App extends Application {

    private File fileInOctoparse;
    private File fileInItemDescription;
    private WebView webView;
    private Stage primaryStage; // Hold a reference to the primary stage
    private String selectedEcommerceSite;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage; // Store the primary stage reference

        webView = new WebView();
        webView.getEngine().load(getClass().getResource("/tactical/blue/static/index.html").toExternalForm());
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("javabridge", this);
            }
        });

        Scene scene = new Scene(webView, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Excel File Processor");
        stage.show();
    }

    public void uploadOctoparseFile() {
        fileInOctoparse = uploadFile("Select Octoparse File");
        if (fileInOctoparse != null) {
            webView.getEngine().executeScript("updateOctoparseFileName('" + fileInOctoparse.getName() + "')");
        }
    }

    public void uploadItemDescriptionFile() {
        fileInItemDescription = uploadFile("Select Item Description File");
        if (fileInItemDescription != null) {
            webView.getEngine().executeScript("updateItemDescriptionFileName('" + fileInItemDescription.getName() + "')");
        }
    }

    private File uploadFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(primaryStage); // Use the primary stage for the dialog
    }

    public void processFiles(String selectedEccomerceSite) {
        this.selectedEcommerceSite = selectedEccomerceSite;
        if (fileInOctoparse != null && fileInItemDescription != null) {
            try {
                CleanExcelFile cleaner = new CleanExcelFile(fileInOctoparse.getPath(), fileInItemDescription.getPath(), selectedEccomerceSite);
                cleaner.makeNewExcelFile();
                webView.getEngine().executeScript("showMessage('Excel file created successfully!')");
            } catch (Exception e) {
                webView.getEngine().executeScript("showMessage('Error processing files: " + e.getMessage() + "')");
            }
        } else {
            webView.getEngine().executeScript("showMessage('Please upload both files before processing.')");
        }
    }

    public void endProgram() {
        Platform.exit(); // This will close the JavaFX application
    }


    public static void main(String[] args) {
        launch(args);
    }
}
