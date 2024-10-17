package tactical.blue.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tactical.blue.navigation.UINavigation;
import tactical.blue.services.ExecutorServiceHandler;

public class PriceReportCreatorUIBuilder extends UIComponents{

    private String siteName;
    private File fileInWebScrapedData;
    private File fileInCustomerOrderData;


    public PriceReportCreatorUIBuilder(UINavigation uiNavigation, Stage primaryStage, ExecutorServiceHandler handler) throws FileNotFoundException {
        super(uiNavigation, handler);
        build(primaryStage);
    }

    @Override
    public Scene getScene() {
        return super.getScene();
    }

    
    /*
     * Builds the entire Scene object
     */
    private void build(Stage primaryStage) throws FileNotFoundException {
        FileChooser fileChooserWebScraped = createFileChooser("Web Scraped Data File");
        Button buttonWebScrape = createWebScrapeFileButton(primaryStage, fileChooserWebScraped);

        FileChooser fileChooserCustomerOrder = createFileChooser("Customer Order Data");
        Button buttonCustomerOrder = createCustomerOrderFileButton(primaryStage, fileChooserCustomerOrder);

        ToggleGroup ecommerceSites = createRadioButtons();

        VBox vBox = createVbox(createLogoBox(),buttonWebScrape, buttonCustomerOrder, createRadioButtonHBox(ecommerceSites), createMakeExcelFileButton(), createGoBackAndEndProgramButtonsHBox(), createStatusText("Creating Price Report..."));
        setContainer(vBox);

        Scene scene = new Scene(vBox, getPageWidth(), getPageHeight());
        
        super.setScene(scene);
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
        
        button.setStyle(getButtonStyle());
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
        button.setStyle(getButtonStyle());
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
     * The "Create Price Report Button" runs makePriceReport on new thread
     */
    private Button createMakeExcelFileButton() {
        Button button = new Button("Create Price Report");
        button.setOnAction(e -> {
            CompletableFuture<Void> task = getHandler().makePriceReportAsync(fileInWebScrapedData, fileInCustomerOrderData, siteName);
            showStatusText();
            updateTextStatus(task, "Price Report complete! Check the Weekly-Reports folder.");
        });
        button.setStyle(getButtonStyle());
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


    /*
    * Creates Container that houses everything
    */    
    private VBox createVbox(HBox logoBox, Button buttonWebScrape, Button buttonCustomerOrder, HBox hBox, Button buttonMakeFile, HBox goBackAndEndProgramHBox, Text statusText)  {
        VBox vBox = new VBox(logoBox, buttonWebScrape, buttonCustomerOrder, hBox, buttonMakeFile, goBackAndEndProgramHBox, statusText);
        vBox.setStyle(getContainerStyle());
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(15));
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }


    
    
}
