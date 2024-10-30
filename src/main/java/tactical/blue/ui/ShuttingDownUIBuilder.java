package tactical.blue.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tactical.blue.async.UIThreadExecutor;
import tactical.blue.navigation.UINavigation;

public class ShuttingDownUIBuilder extends UIComponents{

    public ShuttingDownUIBuilder(UINavigation uiNavigation, UIThreadExecutor handler, Stage primaryStage) {
        super(uiNavigation, handler);
        build(primaryStage);
    }

    private void build(Stage primaryStage) {
        Text text = new Text("Shutting Down...");
        VBox container = new VBox(text);
        container.setStyle(getContainerStyle());
        container.setAlignment(Pos.CENTER);
        super.setContainer(container);
        super.setScene(new Scene(container, getPageWidth(), getPageHeight()));
    }

    public Scene getScene() {
        return super.getScene();
    }

}
