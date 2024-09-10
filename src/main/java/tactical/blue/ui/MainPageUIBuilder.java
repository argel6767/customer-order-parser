package tactical.blue.ui;

import java.io.FileNotFoundException;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPageUIBuilder extends UIElements{

    public Scene build(Stage primaryStage) throws FileNotFoundException {
        VBox vBox = new VBox(createLogoImageView());
        return new Scene(vBox);
    }
}
