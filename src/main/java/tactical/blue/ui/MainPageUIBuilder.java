package tactical.blue.ui;

import java.io.FileNotFoundException;

import javafx.scene.Scene;
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
        VBox vBox = new VBox(createLogoImageView());
        super.setScene(new Scene(vBox));
    }
}
