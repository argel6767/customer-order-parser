package tactical.blue.excel.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;

public class ExcelCleanerUIBuilder {

    private Scene scene;

    private FileChooser createWebScrapedDataButton() {
        FileChooser fileChooserCustomerOrder = new FileChooser();
        fileChooserCustomerOrder.setTitle("Customer Order Data");
        Button buttonCustomerOrder = new Button("Upload Customer Order Data");
        buttonCustomerOrder.setOnAction(e -> {
            File file = fileChooserCustomerOrder.showOpenDialog(primaryStage);
            System.out.println("Selected file: " + file.getAbsolutePath());
        });
    }

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
        return eccomerceSites;
    }

    
}
