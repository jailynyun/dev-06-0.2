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
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for editing an existing deck.
 * <p>
 * Allows users to modify the deck name and description,
 * ensures uniqueness of deck names, updates related flashcards,
 * and saves changes to persistent storage.
 */
public class EditDeckController {

    @FXML
    private TextField deckNameField;

    @FXML
    private TextArea descriptionField;

    private Deck originalDeck;

    /**
     * Loads the selected deck into the edit form.
     *
     * @param deck the deck to be edited
     */
    public void setDeck(Deck deck) {
        this.originalDeck = deck;

        if (deck != null) {
            deckNameField.setText(deck.getName());
            descriptionField.setText(deck.getDescription().replace("\\n", "\n"));
        }
    }

    /**
     * Saves updated deck information, validates uniqueness,
     * updates flashcards referencing the deck, and writes changes to file.
     *
     * @param event button click event
     * @throws IOException if file operations fail
     */
    @FXML
    public void onSaveClicked(ActionEvent event) throws IOException {
        if (originalDeck == null) {
            return;
        }

        String newName = deckNameField.getText().trim();
        String newDescription = descriptionField.getText().replace("\n", "\\n").trim();

        if (newName.isEmpty()) {
            showAlert("Deck name cannot be empty.");
            return;
        }

        Path deckPath = Path.of("decks.txt");
        List<String> updatedDeckLines = new ArrayList<>();

        if (Files.exists(deckPath)) {
            //make sure deck name is unique
            for (String line : Files.readAllLines(deckPath)) {
                String[] parts = line.split("\\|", 2);
                String existingName = parts[0].trim();

                if (!existingName.equalsIgnoreCase(originalDeck.getName())
                        && existingName.equalsIgnoreCase(newName)) {
                    showAlert("Deck name must be unique.");
                    return;
                }
            }

            List<String> lines = Files.readAllLines(deckPath);

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|", 2);
                    String currentName = parts[0].trim();

                    if (currentName.equalsIgnoreCase(originalDeck.getName())) {
                        updatedDeckLines.add(newName + "|" + newDescription);
                    } else {
                        updatedDeckLines.add(line);
                    }
                }
            }

            Files.write(deckPath, updatedDeckLines);
        }

        updateFlashcardsDeckName(originalDeck.getName(), newName);

        showAlert("Deck updated successfully.");

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deck-table-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Updates all flashcards that reference the old deck name to the new name.
     *
     * @param oldName previous deck name
     * @param newName updated deck name
     */
    private void updateFlashcardsDeckName(String oldName, String newName) {
        Path flashcardPath = Path.of("flashcards.txt");

        try {
            if (!Files.exists(flashcardPath)) {
                return;
            }

            List<String> lines = Files.readAllLines(flashcardPath);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|", 5);

                    if (parts.length == 5) {
                        String deckTitle = parts[0].trim();

                        if (deckTitle.equalsIgnoreCase(oldName)) {
                            parts[0] = newName;
                            updatedLines.add(String.join("|", parts));
                        } else {
                            updatedLines.add(line);
                        }
                    } else {
                        updatedLines.add(line);
                    }
                }
            }

            Files.write(flashcardPath, updatedLines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the deck table view without saving changes.
     *
     * @param event button click event
     * @throws IOException if FXML loading fails
     */
    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deck-table-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }


    /**
     * Displays an information alert dialog.
     *
     * @param message message to display
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}