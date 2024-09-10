package tactical.blue.ui;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tactical.blue.navigation.UINavigation;

/*
 * Abstract Class that houses the styles and components that will exist throughout the program
 */
public abstract class UIElements {
    private Scene scene;
    private final String buttonStyle = "-fx-background-color: #2E5698; -fx-border: none; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-background-radius: 16px;";
    private final String containerStyle = "-fx-font-family: \"Arial\", sans-serif;  -fx-padding: 20px;  -fx-pref-width: 600px; -fx-background-color: #f0f0f0; -fx-padding: 20px; -fx-border-radius: 5px;";
    private final String logoAddress = "/static/Blue-Tactical-Logo.png";
    private final int pageWidth = 950;
    private final int pageHeight = 534;
    private UINavigation uiNavigation;

    public UIElements(UINavigation uiNavigation) {
        this.uiNavigation = uiNavigation;
    }

    protected int getPageWidth() {
        return pageWidth;
    }

    protected int getPageHeight() {
        return pageHeight;
    }

    protected Scene getScene() {
        return scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }

    protected String getButtonStyle() {
        return buttonStyle;
    }

    protected String getContainerStyle() {
        return containerStyle;
    }

    protected UINavigation geUINavigation() {
        return uiNavigation;
    }

      protected ImageView createLogoImageView() throws FileNotFoundException {
        InputStream logo = getClass().getResourceAsStream(logoAddress);
        ImageView blueTacticalLogo = new ImageView(new Image(logo));
        blueTacticalLogo.setFitWidth(250);
        blueTacticalLogo.setPreserveRatio(true);
        return blueTacticalLogo;
        
        
    }

     /*
     * Creates a FileChooseObject
     */
    protected FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser;
    }

    /*
     * Makes a button to naviagte to the Main Page
     * directionOfButton refers to whether button is a "back" or "go to" button
     */
    protected Button createGoToMainPage(String directionOfButton) {
        Button button = new Button(directionOfButton);
        button.setOnAction(e -> {
            try {
                uiNavigation.setSceneToMainPage();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        button.setStyle(buttonStyle);
        return button;
    }
    
    /*
     * Makes a button to navigate to the Price Report Creator Page
     */
    protected Button createGoToPriceReportPage(String directionOfButton) {
        Button button = new Button(directionOfButton);
        button.setOnAction(e -> {
            try {
                uiNavigation.setSceneToPriceReportCreator();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        button.setStyle(buttonStyle);
        return button;
        
    }

    /*
     * Creates button that navigates to the Consolidate Excel Files Page
     */
    protected Button createGoToConsolidateExcelFilesPage(String directionOfButton) {
        Button button = new Button(directionOfButton);
        button.setOnAction(e -> {
            try {
                uiNavigation.setSceneToConsolidateExcelFiles();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        button.setStyle(buttonStyle);
        return button;
    }
}
