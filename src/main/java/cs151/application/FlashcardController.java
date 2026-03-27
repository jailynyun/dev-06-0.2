package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FlashcardController {

    @FXML
    private ComboBox<String> deckComboBox;

    @FXML
    private TextField questionField;

    @FXML
    private TextField answerField;

    private Deck deck;

    private static final String FILE_PATH = "flashcards.txt";

    /**
     * Called automatically when FXML loads.
     */
    @FXML
    public void initialize() {
        deckComboBox.getItems().clear();
    }

    /**
     * Receives selected deck from DeckTableController.
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
     * Saves flashcard to file.
     */
    @FXML
    private void onSaveClicked() {

        String question = questionField.getText();
        String answer = answerField.getText();

        if (deck == null) {
            System.out.println("No deck selected.");
            return;
        }

        if (question == null || question.isBlank()
                || answer == null || answer.isBlank()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            writer.write(
                    deck.getName() + "|" +
                            question + "|" +
                            answer + "|" +
                            java.time.LocalDateTime.now()
            );
            writer.newLine();

            System.out.println("Flashcard saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear fields after saving
        questionField.clear();
        answerField.clear();
    }

    /**
     * Navigates back to Deck Table page.
     */
    @FXML
    private void onBackClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("deck-table-view.fxml"));

        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node)
                event.getSource()).getScene().getWindow();

        stage.setScene(scene);
    }
}
