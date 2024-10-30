package tactical.blue.navigation;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneSwitchHandler {

    /*
      * Houses the switching of scenes logic
     * checks if the current scene is null currently (ie the application was just booted up), if so the newScene is set as the current scene
     * as well with the title parameter
     * Otherwise a FadeTransition object is made to allow for there to be a transition affect between pages to give a more natural transition feel
     */
    public void switchScene(Scene newScene, String title, Stage stage) {
        if (stage.getScene() == null) {
            setSceneValues(newScene, title, stage);
            getFadeIn(newScene, 1000);
        }
        else {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(350), stage.getScene().getRoot());
            setFadeValues(fadeOut, 1, 0);
            fadeOut.setOnFinished(event -> {
                setSceneValues(newScene, title, stage);
                getFadeIn(newScene, 350);
            });
            fadeOut.play();
        }
    }

    public void fadeOut(Stage stage) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(700), stage.getScene().getRoot());
        setFadeValues(fadeOut, 1, 0);
        fadeOut.setOnFinished(event -> {
            Platform.exit();
            System.exit(0);
        });
        fadeOut.play();
    }

    /*
     * Sets necessary values for stage
     */
    private void setSceneValues(Scene newScene, String title, Stage stage) {
        stage.setScene(newScene);
        stage.setTitle(title);
    }

    /*
     * Creates a FadeTransition object that transitions into scene
     */
    private void getFadeIn(Scene newScene, int time) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(time), newScene.getRoot());
        setFadeValues(fadeIn, 0, 1);
        fadeIn.play();
    }

    /*
     * Sets necessary fade values for transition
     */
    private void setFadeValues(FadeTransition fade, double from, double to) {
        fade.setFromValue(from);
        fade.setToValue(to);
    }

}
