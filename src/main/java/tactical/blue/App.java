package tactical.blue;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;
import tactical.blue.ui.*;

public class App extends Application {
    private UINavigation uiNavigation;
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //create UI Builder then call the UI building method
        MainPageUIBuilder mainPageUI = new MainPageUIBuilder(uiNavigation, primaryStage);
        Scene mainScene = mainPageUI.getScene();
        PriceReportCreatorUIBuilder excelFileCreatorUIBuilder = new PriceReportCreatorUIBuilder(uiNavigation, primaryStage);
        Scene fileCreator = excelFileCreatorUIBuilder.getScene();
        
        // Set the scene and show the stage
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Product Information Excel File Generator");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
