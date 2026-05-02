package cs151.application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Controller for displaying flashcards in a table view.
 * Loads flashcards from storage, filters them by selected deck,
 * and displays them in a sorted table.
 *
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.5
 */
public class FlashcardTableController {

    @FXML
    private TableView<Flashcard> flashcardTable;

    @FXML
    private TableColumn<Flashcard, String> deckColumn;

    @FXML
    private TableColumn<Flashcard, String> questionColumn;

    @FXML
    private TableColumn<Flashcard, String> answerColumn;

    @FXML
    private TableColumn<Flashcard, String> createdAtColumn;

    private Deck selectedDeck;

/**
 * Initializes the Flashcard table view by binding table columns
 * to Flashcard properties.
 */
    @FXML
    public void initialize() {
        deckColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDeckTitle()));

        questionColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getQuestion()));

        answerColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getAnswer()));

        createdAtColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getCreatedAt().toString()));
    }

/**
 * Sets the selected deck and loads its flashcards into the table view.
 *
 * @param selectedDeck The deck selected from the previous screen.
 */
    public void setSelectedDeck(Deck selectedDeck) {
        this.selectedDeck = selectedDeck;
        loadFlashcardsForSelectedDeck();
    }

/**
 * Loads flashcards from flashcards.txt and filters them based on
 * the currently selected deck.
 * Flashcards are sorted by creation time in descending order.
 */
    private void loadFlashcardsForSelectedDeck() {
        ObservableList<Flashcard> flashcards = FXCollections.observableArrayList();
        Path path = Path.of("flashcards.txt");

        try {
            if (Files.exists(path) && selectedDeck != null) {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split("\\|", 6);

                        if (parts.length >= 5) {
                            String deckTitle = parts[0].trim();
                            String question = parts[1].replace("\\n", "\n");
                            String answer = parts[2].replace("\\n", "\n");
                            String status = parts[3].trim();
                            LocalDateTime createdAt = LocalDateTime.parse(parts[4].trim());

                            if (deckTitle.equalsIgnoreCase(selectedDeck.getName())) {
                                Flashcard flashcard = new Flashcard(question, answer, deckTitle, createdAt);
                                flashcard.setStatus(status);
                                flashcards.add(flashcard);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        flashcards.sort(Comparator.comparing(Flashcard::getCreatedAt).reversed());
        flashcardTable.setItems(flashcards);
    }

/**
 * Navigates back to the Deck Table view screen.
 *
 * @param event The action event triggered by the back button.
 * @throws IOException If the FXML file cannot be loaded.
 */
    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deck-table-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}