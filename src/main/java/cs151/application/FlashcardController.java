package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlashcardController {

    @FXML
    private ComboBox<String> deckComboBox;

    @FXML
    private TextField questionField;

    @FXML
    private TextField answerField;

    private static final String FILE_PATH = "flashcards.txt";

    @FXML
    public void initialize() {
        List<Deck> decks = DeckTableController.deckList;
        for (Deck deck : decks) {
            deckComboBox.getItems().add(deck.getTitle());
        }
    }

    @FXML
    private void onSaveClicked() {

        String deck = deckComboBox.getValue();
        String question = questionField.getText();
        String answer = answerField.getText();

        Flashcard card = new Flashcard(question, answer, deck);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(
                    deck + "|" +
                            question + "|" +
                            answer + "|" +
                            card.getCreatedAt().toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
