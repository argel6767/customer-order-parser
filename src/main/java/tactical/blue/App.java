package tactical.blue;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tactical.blue.excel.ui.ExcelFileCreatorUIBuilder;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //create UI Builder then call the UI building method
        ExcelFileCreatorUIBuilder excelFileCreatorUIBuilder = new ExcelFileCreatorUIBuilder();
        Scene scene = excelFileCreatorUIBuilder.buildExcelFileCreatorUI(primaryStage);
        // Set the scene and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Product Information Excel File Generator");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
