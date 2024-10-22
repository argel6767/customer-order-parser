package tactical.blue.services;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;


public class StatusTextStateManager {

    private Text statusText;
    private final String originalTextValue;

    public StatusTextStateManager(Text statusText) {
        this.statusText = statusText;
        this.originalTextValue = statusText.getText();
    }

    /*
     * takes in a task, usually whatever Async Service is being used (Price Report Creator or Report Consolidator)
     * and then updates the text to show completion of task
     * then after a few seconds Text object is reset back to its initial state
     */
    public CompletableFuture<Void> updateTextStatus(CompletableFuture<Void> task, String newStatus) {
        if (task.isCancelled()) {
            return CompletableFuture.completedFuture(null);
        }
        return task.thenRun( () -> {
            Platform.runLater( () -> {
                statusText.setText(newStatus);
                pauseBeforeResettingStatusText();
            });
        });
    }

    /*
     * makes a pause object to allow time for the reader to be aware of
     * whatever Service is completed, and then resets Text Object
     */
    private void pauseBeforeResettingStatusText() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> resetStatusText());
        pause.play();
    }

    /*
     * resets text in Text object to original state
     */
    private void resetStatusText() {
        statusText.setText(originalTextValue);
        hideStatusText();
    }

    /*
     * makes statusText visible
     */
    public void showStatusText() {
        statusText.setVisible(true);
    }

    /*
     * makes statusText invisible
     */
    public void hideStatusText() {
        statusText.setVisible(false);
    }
    /*
     * getters and setters
     */
    public Text getStatusText() {
        return statusText;
    }

    public void setStatusText(Text statusText) {
        this.statusText = statusText;
    }

    public String getStatusTextValue() {
        return statusText.getText();
    }
}
