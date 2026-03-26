package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FlashcardController {

    @FXML
    private ComboBox<String> deckComboBox;

    @FXML
    private TextField questionField;

    @FXML
    private TextField answerField;

    private static final String FILE_PATH = "flashcards.txt";

    /**
     * Populates the ComboBox with available deck names.
     * This method is called from DeckTableController via FXMLLoader.
     */
    public void setDecks(List<Deck> decks) {
        deckComboBox.getItems().clear();
        for (Deck deck : decks) {
            deckComboBox.getItems().add(deck.getName());
        }
    }

    /**
     * Optionally preselects a deck when navigating from the deck table.
     */
    public void setSelectedDeck(Deck deck) {
        if (deck != null) {
            deckComboBox.setValue(deck.getName());
        }
    }

    /**
     * Saves a flashcard to flashcards.txt.
     */
    @FXML
    private void onSaveClicked() {

        String deck = deckComboBox.getValue();
        String question = questionField.getText();
        String answer = answerField.getText();

        if (deck == null || question.isBlank() || answer.isBlank()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        Flashcard card = new Flashcard(question, answer, deck);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(
                    deck + "|" +
                            question + "|" +
                            answer + "|" +
                            card.getCreatedAt().toString());
            writer.newLine();
            System.out.println("Flashcard saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear input fields after saving
        questionField.clear();
        answerField.clear();
    }
}
