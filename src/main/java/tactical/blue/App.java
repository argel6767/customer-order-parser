package tactical.blue;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tactical.blue.excel.PriceReportCreator;
import tactical.blue.ui.*;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //create UI Builder then call the UI building method
        MainPageUIBuilder mainPageUI = new MainPageUIBuilder();
        Scene mainPage = mainPageUI.build(primaryStage);
        PriceReportCreatorUIBuilder excelFileCreatorUIBuilder = new PriceReportCreatorUIBuilder();
        Scene fileCreator = excelFileCreatorUIBuilder.build(primaryStage);
        
        // Set the scene and show the stage
        primaryStage.setScene(mainPage);
        primaryStage.setTitle("Product Information Excel File Generator");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
