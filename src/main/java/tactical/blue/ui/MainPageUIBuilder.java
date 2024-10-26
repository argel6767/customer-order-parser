package tactical.blue.ui;

import java.io.FileNotFoundException;
import java.time.LocalTime;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;
import tactical.blue.async.UIThreadExecutor;

public class MainPageUIBuilder extends UIComponents{


    public MainPageUIBuilder(UINavigation uiNavigation, Stage primaryStage, UIThreadExecutor handler) throws FileNotFoundException {
        super(uiNavigation, handler);
        build(primaryStage);
    }

    @Override
    public Scene getScene() {
        return super.getScene();
    }

    /*
     * builds the UI
     */
    private void build(Stage primaryStage) throws FileNotFoundException {
        super.setScene(new Scene(createVboxContainer(), getPageWidth(), getPageHeight()));
    }

    /*
     * Creates the VBox that holds everything
     */
    private VBox createVboxContainer() throws FileNotFoundException {
        VBox vBox = new VBox(createLogoAndHeadingBox(createGreetingHeading()),createGoToPriceReportPage("Create A Price Report"), createGoToConsolidateExcelFilesPage("Consolidate Price Reports"), createEndProgramButton());
        vBox.setStyle(getContainerStyle());
        vBox.setSpacing(25);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    /*
     * Creates a Label that greets the user based off time
     */
    private Text createGreetingHeading() {
        LocalTime time = LocalTime.now();
        Text heading = new Text();
        heading.setFill(Color.web("#2E5698"));
        heading.setStyle(getHeaderStyle());
        
        if(time.isBefore(LocalTime.of(12, 0))) {
            heading.setText("Good Morning");
        }
        else if (time.isBefore(LocalTime.of(18,0))) {
            heading.setText("Good Afternoon");
        }
        else {
            heading.setText("Good Evening");
        }

        return heading;
    }


}
