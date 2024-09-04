package tactical.blue;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tactical.blue.excel.CleanExcelFile;

public class App extends Application {

    private File fileInWebScraped;
    private File fileInItemDescriptions;
    private String siteName;

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooserWebScraped = new FileChooser();

        // Set the title for the FileChooser dialog
        fileChooserWebScraped.setTitle("Web Scraped Data File");

        Button buttonWebScrape = new Button("Upload Web Scraped Data");
        buttonWebScrape.setOnAction(e -> {
            // Show the open file dialog
            this.fileInWebScraped = fileChooserWebScraped.showOpenDialog(primaryStage);
            if (fileInWebScraped != null) {
                // Handle the selected file (e.g., display the file name)
                System.out.println("Selected file: " + fileInWebScraped.getAbsolutePath());
            }
        });

        FileChooser fileChooserCustomerOrder = new FileChooser();
        fileChooserCustomerOrder.setTitle("Customer Order Data");
        Button buttonCustomerOrder = new Button("Upload Customer Order Data");
        buttonCustomerOrder.setOnAction(e -> {
            fileInItemDescriptions = fileChooserCustomerOrder.showOpenDialog(primaryStage);
            System.out.println("Selected file: " + fileInItemDescriptions.getAbsolutePath());
        });

        Button buttonEndProgram = new Button("End Program");
        buttonEndProgram.setOnAction(e -> {
            Platform.exit();
        });

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

       Button buttonCreateExcelFile = new Button("Create Price Report");
       buttonCreateExcelFile.setOnAction(e -> {
        CleanExcelFile cleanExcelFile = new CleanExcelFile(fileInWebScraped, fileInItemDescriptions, siteName);
        cleanExcelFile.makeNewExcelFile();
       }); 

        HBox radioButtons = new HBox(boundTree, henrySchein, medco, naRescue);
        VBox vbox = new VBox(buttonWebScrape, buttonCustomerOrder, radioButtons, buttonCreateExcelFile, buttonEndProgram);
        
        Scene scene = new Scene(vbox, 950, 534);

        // Set the scene and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Excel File Generator");
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
