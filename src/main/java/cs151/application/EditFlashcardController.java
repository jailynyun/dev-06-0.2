package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for editing an existing flashcard.
 * <p>
 * Allows users to modify the deck, question, answer, and status of a flashcard,
 * then save updates back to persistent storage.
 */
public class EditFlashcardController {

    @FXML private ComboBox<String> deckCombo;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea frontArea;
    @FXML private TextArea backArea;

    private Flashcard original;

    /**
     * Loads flashcard data into the edit form and initializes dropdowns.
     *
     * @param fc the flashcard to be edited
     */
    public void setFlashcard(Flashcard fc) {
        this.original = fc;

        // decks
        deckCombo.getItems().setAll(DeckFile.loadAll().stream().map(Deck::getName).toList());

        // status
        statusCombo.getItems().setAll("New", "Learning", "Mastered");

        deckCombo.setValue(fc.getDeckTitle());
        statusCombo.setValue(fc.getStatus());
        frontArea.setText(fc.getQuestion());
        backArea.setText(fc.getAnswer());
    }

    /**
     * Saves updated flashcard data and writes changes to file storage.
     */
    @FXML
    private void onSaveClicked() {
        String newDeck = deckCombo.getValue();
        String newStatus = statusCombo.getValue();
        String newQ = frontArea.getText();
        String newA = backArea.getText();

        // Update file: load all, replace the matching original flashcard by createdAt + deck + question (or better unique id)
        List<Flashcard> all = FlashcardFile.loadAll();

        for (int i = 0; i < all.size(); i++) {
            Flashcard fc = all.get(i);
            if (fc.getDeckTitle().equalsIgnoreCase(original.getDeckTitle())
                    && fc.getCreatedAt().equals(original.getCreatedAt())
                    && fc.getQuestion().equals(original.getQuestion())
                    && fc.getAnswer().equals(original.getAnswer())) {

                Flashcard updated = new Flashcard(newQ, newA, newDeck, original.getCreatedAt());
                updated.setStatus(newStatus);

                all.set(i, updated);
                break;
            }
        }

        FlashcardFile.rewriteAll(all);
    }

    /**
     * Navigates back to the flashcard search screen without saving.
     *
     * @param event the button click event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("search-flashcard-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
