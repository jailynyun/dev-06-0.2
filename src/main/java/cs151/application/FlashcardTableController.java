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

    public void setSelectedDeck(Deck selectedDeck) {
        this.selectedDeck = selectedDeck;
        loadFlashcardsForSelectedDeck();
    }

    private void loadFlashcardsForSelectedDeck() {
        ObservableList<Flashcard> flashcards = FXCollections.observableArrayList();
        Path path = Path.of("flashcards.txt");

        try {
            if (Files.exists(path) && selectedDeck != null) {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split("\\|", 4);

                        if (parts.length == 4) {
                            String deckTitle = parts[0].trim();
                            String question = parts[1].trim();
                            String answer = parts[2].trim();
                            LocalDateTime createdAt = LocalDateTime.parse(parts[3].trim());

                            if (deckTitle.equalsIgnoreCase(selectedDeck.getName())) {
                                flashcards.add(new Flashcard(question, answer, deckTitle, createdAt));
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

    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deck-table-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}