package tactical.blue.async;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

/*
 * handles state management of the Text object that holds the current status
 * of a task that was started
 */
public class StatusTextStateManager {

    private Text statusText;
    private String originalTextValue;

    public StatusTextStateManager(Text statusText) {
        this.statusText = statusText;
        this.originalTextValue = statusText.getText();
    }

    /*
       empty constructor
     */
    public StatusTextStateManager() {
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
        Platform.runLater( () -> statusText.setVisible(true));
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

    /*
     * sets both fields in one method call
     */
    public void setFields(Text statusText) {
        setStatusText(statusText);
        setOriginalTextValue(statusText);
    }

    public void setStatusText(Text statusText) {
        this.statusText = statusText;
    }

    public void setOriginalTextValue(Text statusText) {
        this.originalTextValue = statusText.getText();
    }

    public String getStatusTextValue() {
        return statusText.getText();
    }
}
