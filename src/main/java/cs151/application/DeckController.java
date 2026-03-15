package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
/**
 * Controller class for the Deck Management screen.
 * Handles the logic for creating, validating, and persisting flashcard decks to storage.
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.2
 */
public class DeckController {

    @FXML
    private TextField deckNameField;

    @FXML
    private TextArea descriptionField;
    /**
     * Handles the creation of a new deck.
     * Validates that the name is not empty and is unique before saving to decks.txt.
     */
    @FXML
    public void onCreateClicked() {

        String deckName = deckNameField.getText().trim();
        String description = descriptionField.getText().trim();

        // Validate required field
        if (deckName.isEmpty()) {
            showAlert("Deck Name cannot be empty.");
            return;
        }

        try {
            Path path = Path.of("decks.txt");

            // Check uniqueness
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    if (line.split("\\|")[0].equalsIgnoreCase(deckName)) {
                        showAlert("Deck Name must be unique.");
                        return;
                    }
                }
            }

            // Persist to file
            String data = deckName + "|" + description + "\n";
            Files.write(path, data.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            showAlert("Deck created successfully!");

            deckNameField.clear();
            descriptionField.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Navigates the user back to the Home screen.
     * @param event The action event triggered by the user.
     * @throws IOException If the home-view.fxml file cannot be loaded.
     */
    public void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    /**
     * Utility method to display information alerts to the user.
     * @param message The text content to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
