package tactical.blue;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //instantiated UINavigation object and set it to the mainpage
        UINavigation uiNavigation = new UINavigation(primaryStage);
        uiNavigation.setSceneToMainPage();
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
