package tactical.blue.excel.ui;

import java.io.File;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tactical.blue.excel.PriceReportCreator;

public class ExcelFileCreatorUIBuilder {

    private String siteName;
    private File fileInWebScrapedData;
    private File fileInCustomerOrderData;

    /*
     * Builds the entire Scene object
     */
    public Scene buildExcelFileCreatorUI(Stage primaryStage) {
        FileChooser fileChooserWebScraped = createFileChooser("Web Scraped Data File");
        Button buttonWebScrape = createWebScrapeFileButton(primaryStage, fileChooserWebScraped);

        FileChooser fileChooserCustomerOrder = createFileChooser("Customer Order Data");
        Button buttonCustomerOrder = createCustomerOrderFileButton(primaryStage, fileChooserCustomerOrder);

        ToggleGroup eccomerceSites = createRadioButtons();

        Button buttonMakeFile = createMakeExcelFileButton();
        Button buttonEndProgram = createEndProgramButton();

        HBox hBox = createRadioButtonHBox(eccomerceSites);
        VBox vBox = new VBox(buttonWebScrape, buttonCustomerOrder, hBox, buttonMakeFile, buttonEndProgram);

        Scene scene = new Scene(vBox, 950, 534);
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
        return hBox;
    }
    
}
