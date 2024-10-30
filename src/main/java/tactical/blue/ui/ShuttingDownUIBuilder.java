package tactical.blue.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tactical.blue.async.UIThreadExecutor;
import tactical.blue.navigation.UINavigation;

public class ShuttingDownUIBuilder extends UIComponents{

    public ShuttingDownUIBuilder(UINavigation uiNavigation, UIThreadExecutor handler) {
        super(uiNavigation, handler);
        build();
    }

    private void build() {
        VBox container = createContainer(createShuttingDownText());
        super.setContainer(container);
        super.setScene(new Scene(container, getPageWidth(), getPageHeight()));
    }

    public Scene getScene() {
        return super.getScene();
    }

    private VBox createContainer(Text text) {
        VBox container = new VBox(text);
        container.setStyle(getContainerStyle());
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private Text createShuttingDownText() {
        Text text = new Text("Shutting Down...");
        text.setStyle(getHeaderStyle());
        return text;
    }
}
