package tactical.blue.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tactical.blue.excel.PriceReportCreator;

public class ExcelFileCreatorUIBuilder {

    private String siteName;
    private File fileInWebScrapedData;
    private File fileInCustomerOrderData;
    private final String buttonStyle = "-fx-background-color: #2E5698; -fx-border: none; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-background-radius: 16px;";
                
    private final String container = "-fx-font-family: \"Arial\", sans-serif;  -fx-padding: 20px;  -fx-pref-width: 600px; -fx-background-color: #f0f0f0; -fx-padding: 20px; -fx-border-radius: 5px;";

    /*
     * Builds the entire Scene object
     */
    public Scene buildExcelFileCreatorUI(Stage primaryStage) throws FileNotFoundException {
        FileChooser fileChooserWebScraped = createFileChooser("Web Scraped Data File");
        Button buttonWebScrape = createWebScrapeFileButton(primaryStage, fileChooserWebScraped);

        FileChooser fileChooserCustomerOrder = createFileChooser("Customer Order Data");
        Button buttonCustomerOrder = createCustomerOrderFileButton(primaryStage, fileChooserCustomerOrder);

        ToggleGroup eccomerceSites = createRadioButtons();

        Button buttonMakeFile = createMakeExcelFileButton();
        Button buttonEndProgram = createEndProgramButton();

        HBox hBox = createRadioButtonHBox(eccomerceSites);
        HBox logoBox = createLogoBox();


        VBox vBox = createVbox(logoBox,buttonWebScrape, buttonCustomerOrder, hBox, buttonMakeFile, buttonEndProgram);
        StackPane root = new StackPane(vBox);
        Scene scene = new Scene(root, 950, 534);
        
        return scene;
    }

    /*
     * Creates a FileChooseObject
     */
    private FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser;
    }

    /*
     * Creates Button that allows uploading web scraped data file
     */
    private Button createWebScrapeFileButton(Stage primaryStage, FileChooser fileChooser) {
        Button button = new Button("Upload Web Scraped Data Here");
        
        button.setOnAction(e -> {
            this.fileInWebScrapedData = fileChooser.showOpenDialog(primaryStage); 
            System.out.println("Selected file: " +this.fileInWebScrapedData.getAbsolutePath());
        });
        
        button.setStyle(buttonStyle);
        return button;
    }


    /*
     * Creates Button that allows uploding a customer order data file
     */
    private Button createCustomerOrderFileButton(Stage primaryStage, FileChooser fileChooser) {
        Button button = new Button("Upload Customer Order Data Here");
        
        button.setOnAction(e -> {
            this.fileInCustomerOrderData = fileChooser.showOpenDialog(primaryStage); 
            System.out.println("Selected file: " +this.fileInCustomerOrderData.getAbsolutePath());
        });
        button.setStyle(buttonStyle);
        return button;
    }

    /*
     * Creates the Radio Buttons for Each Eccomerce Website and puts them into one ToggleGroup object
     */
    private ToggleGroup createRadioButtons() {
        ToggleGroup eccomerceSites = new ToggleGroup();
        RadioButton boundTree = new RadioButton("Bound Tree");
        boundTree.setToggleGroup(eccomerceSites);
        RadioButton henrySchein = new RadioButton("Henry Schein");
        henrySchein.setToggleGroup(eccomerceSites);
        RadioButton medco = new RadioButton("Medco Sports Medicine");
        medco.setToggleGroup(eccomerceSites);
        RadioButton naRescue = new RadioButton("North American Rescue");
        naRescue.setToggleGroup(eccomerceSites);

        eccomerceSites.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == boundTree) {
                    this.siteName = "Bound Tree";
            }
            else if (newValue == henrySchein) {
                    this.siteName = "Henry Schein";
            }
            else if (newValue == medco) {
                   this.siteName = "Medco";
            }
            else {
                   this.siteName = "NARescue";
                }

            System.out.println(this.siteName);
        });
       
        return eccomerceSites;
    }

    /*
     * The "Create Price Report Button"
     */
    private Button createMakeExcelFileButton() {
        Button button = new Button("Create Price Report");
        button.setOnAction(e -> {
            PriceReportCreator priceReportCreator = new PriceReportCreator(this.fileInWebScrapedData, this.fileInCustomerOrderData, this.siteName);
            priceReportCreator.makeNewExcelFile();  
        });
        button.setStyle(buttonStyle);
        return button;
    }
    
    /*
     * Makes Button that ends program when pressed
     */
    private Button createEndProgramButton() {
        Button button = new Button("End Program");
        button.setOnAction(e -> {
            Platform.exit();
        });
        button.setStyle(buttonStyle);
        return button;
    }

    /*
     * Makes HBox of RadioButtons through ToggleGroup
     */
    private HBox createRadioButtonHBox(ToggleGroup toggleGroup) {
        HBox hBox = new HBox();
        
        for (Toggle toggle: toggleGroup.getToggles()) {
            hBox.getChildren().add((RadioButton)toggle);
        }
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox createLogoBox() throws FileNotFoundException {
        HBox logoBox = new HBox(createLogoImageView());
        logoBox.setAlignment(Pos.TOP_LEFT);
        return logoBox;
    }

    private ImageView createLogoImageView() throws FileNotFoundException {
        InputStream logo = getClass().getResourceAsStream("/static/Blue-Tactical-Logo.png");
        ImageView blueTacticalLogo = new ImageView(new Image(logo));
        blueTacticalLogo.setFitWidth(250);
        blueTacticalLogo.setPreserveRatio(true);
        return blueTacticalLogo;
        
        
    }

    
    private VBox createVbox(HBox logoBox, Button buttonWebScrape, Button buttonCustomerOrder, HBox hBox, Button buttonMakeFile, Button buttonEndProgram) {
        VBox vBox = new VBox(logoBox, buttonWebScrape, buttonCustomerOrder, hBox, buttonMakeFile, buttonEndProgram);
        vBox.setStyle(container);
        vBox.setSpacing(15);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }


    
    
}