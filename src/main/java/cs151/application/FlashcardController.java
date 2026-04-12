package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Controller for creating and saving flashcards.
 * Handles user input, validation, file storage, and navigation
 * between flashcard creation and deck selection screens.
 *
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.5
 */
public class FlashcardController {

    @FXML
    private ComboBox<String> deckComboBox;

    @FXML
    private TextArea questionArea;

    @FXML
    private TextArea answerArea;

    private Deck deck;

    private static final String FILE_PATH = "flashcards.txt";

    /**
     * Initializes the Flashcard view when the FXML is loaded.
     * Clears and prepares the deck selection ComboBox.
     */
    @FXML
    public void initialize() {
        deckComboBox.getItems().clear();
    }

    /**
     * Sets the currently selected deck and updates the ComboBox
     * to reflect the chosen deck.
     *
     * @param deck The deck selected from the previous screen.
     */
    public void setDeck(Deck deck) {
        this.deck = deck;

        if (deck != null) {
            deckComboBox.getItems().clear();
            deckComboBox.getItems().add(deck.getName());
            deckComboBox.setValue(deck.getName());
        }
    }

    /**
     * Saves a new flashcard to the flashcards.txt file.
     * Validates that a deck is selected and that question/answer fields are not
     * empty.
     * Stores data in the format: deckName|question|answer|timestamp.
     */
    @FXML
    private void onSaveClicked() {
        String question = questionArea.getText();
        String answer = answerArea.getText();

        if (deck == null) {
            System.out.println("No deck selected.");
            return;
        }

        if (question == null || question.isBlank()
                || answer == null || answer.isBlank()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            writer.write(
                    deck.getName() + "|" +
                            question + "|" +
                            answer + "|" +
                            java.time.LocalDateTime.now());
            writer.newLine();

            System.out.println("Flashcard saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear fields after saving
        questionArea.clear();
        answerArea.clear();
    }

    /**
     * Navigates back to the Deck Table view screen.
     *
     * @param event The action event triggered by the back button click.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @FXML
    private void onBackClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("deck-table-view.fxml"));

        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
    }
}
