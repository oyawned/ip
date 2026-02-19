package sheng;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Sheng sheng;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image shengImage = new Image(this.getClass().getResourceAsStream("/images/DaSheng.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Sheng instance */
    public void setSheng(Sheng s) {
        sheng = s;
        showWelcomeMessage();
    }
    
    /**
     * Displays a welcome message when the application starts.
     */
    private void showWelcomeMessage() {
        String welcome = "Hello! I'm Sheng!\nWhat can I do for you today?";
        dialogContainer.getChildren().add(
            DialogBox.getShengDialog(welcome, shengImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Sheng's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }
        
        String response = sheng.getResponse(input);
        
        // Check if response is an error (contains common error indicators)
        boolean isError = response.contains("Oops") || response.contains("Error") 
                || response.contains("Invalid") || response.contains("Hmm")
                || response.contains("forgot") || response.contains("doesn't look");
        
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                isError ? DialogBox.getErrorDialog(response, shengImage) 
                        : DialogBox.getShengDialog(response, shengImage)
        );
        userInput.clear();
        
        // Exit the application if the user types "bye"
        if (input.trim().equalsIgnoreCase("bye")) {
            Platform.exit();
        }
    }
}
