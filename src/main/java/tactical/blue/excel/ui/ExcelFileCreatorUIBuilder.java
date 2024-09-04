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

    /*
     * Builds the entire Scene object
     */
    public Scene buildExcelFileCreatorUI(Stage primaryStage, File fileInWebScrapedData, File fileInItemDescriptions, String siteName) {
        FileChooser fileChooserWebScraped = createFileChooser("Web Scraped Data File");
        Button buttonWebScrape = createFileButton(primaryStage, fileInWebScrapedData, fileChooserWebScraped, "Upload Web Scraped Data");

        FileChooser fileChooserCustomerOrder = createFileChooser("Customer Order Data");
        Button buttonCustomerOrder = createFileButton(primaryStage, fileInItemDescriptions, fileChooserCustomerOrder, "Upload Customer Order Data");

        ToggleGroup eccomerceSites = createRadioButtons(siteName);

        Button buttonMakeFile = createMakeExcelFileButton(fileInWebScrapedData, fileInItemDescriptions, siteName);
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
     * Creates Button that allows uploading a file
     */
    private Button createFileButton(Stage primaryStage, File file, FileChooser fileChooser, String buttonName) {
        Button button = new Button(buttonName);
        File[] fileContainer = {file};
        button.setOnAction(e -> {
            fileContainer[0] = fileChooser.showOpenDialog(primaryStage); //TODO FIND HOW TO SAVE FILE PATH
            System.out.println("Selected file: " + fileContainer[0].getAbsolutePath());
        });
        file = fileContainer[0];
        return button;
    }

    /*
     * Creates the Radio Buttons for Each Eccomerce Website and puts them into one ToggleGroup object
     */
    private ToggleGroup createRadioButtons(String siteName) {
        ToggleGroup eccomerceSites = new ToggleGroup();
        RadioButton boundTree = new RadioButton("Bound Tree");
        boundTree.setToggleGroup(eccomerceSites);
        RadioButton henrySchein = new RadioButton("Henry Schein");
        henrySchein.setToggleGroup(eccomerceSites);
        RadioButton medco = new RadioButton("Medco Sports Medicine");
        medco.setToggleGroup(eccomerceSites);
        RadioButton naRescue = new RadioButton("North American Rescue");
        naRescue.setToggleGroup(eccomerceSites);

        String[] siteNameHolder = new String[1]; //wrapper to allow for lamda

        eccomerceSites.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == boundTree) {
                    siteNameHolder[0] = "Bound Tree";
            }
            else if (newValue == henrySchein) {
                    siteNameHolder[0] = "Henry Schein";
            }
            else if (newValue == medco) {
                    siteNameHolder[0] = "Medco";
            }
            else {
                    siteNameHolder[0] = "NARescue";
                }

            System.out.println(siteNameHolder[0]);
        });
        siteName = siteNameHolder[0];
        return eccomerceSites;
    }

    /*
     * The "Create Price Report Button"
     */
    private Button createMakeExcelFileButton(File fileInWebScraped, File fileInItemDescriptions, String siteName) {
        Button button = new Button("Create Price Report");
        button.setOnAction(e -> {
            PriceReportCreator priceReportCreator = new PriceReportCreator(fileInWebScraped, fileInItemDescriptions, siteName);
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
