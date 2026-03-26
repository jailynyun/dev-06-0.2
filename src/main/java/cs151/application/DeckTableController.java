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
import java.util.Comparator;
import java.util.List;

/**
 * Controller class for displaying all decks in a table view.
 * Handles loading, sorting, and presenting deck data from storage.
 *
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.4
 */
public class DeckTableController {

    @FXML
    private TableView<Deck> deckTable;

    @FXML
    private TableColumn<Deck, String> nameColumn;

    @FXML
    private TableColumn<Deck, String> descriptionColumn;

    /**
     * Initializes the table view by setting up columns and loading deck data.
     */
    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));

        descriptionColumn
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));

        ObservableList<Deck> decks = loadDecks();

        if (decks.isEmpty()) {

            decks.add(new Deck("Math", "Basic mathematics concepts"));
            decks.add(new Deck("History", "World history overview"));
            decks.add(new Deck("Science", "General science fundamentals"));

            saveDecks(decks);
        }

        decks.sort(Comparator.comparing(Deck::getName, String.CASE_INSENSITIVE_ORDER));

        deckTable.setItems(decks);
    }

    /**
     * Loads deck data from the decks.txt file.
     * Parses each line into Deck objects.
     *
     * @return ObservableList of Deck objects.
     */
    private ObservableList<Deck> loadDecks() {
        ObservableList<Deck> decks = FXCollections.observableArrayList();
        Path path = Path.of("decks.txt");

        try {
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split("\\|", 2);

                        String name = parts[0].trim();
                        String description = "";

                        if (parts.length > 1) {
                            description = parts[1].trim();
                        }

                        decks.add(new Deck(name, description));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decks;
    }

    /**
     * Navigates back to the Define Deck screen.
     *
     * @param event The action event triggered by the user.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("define-deck-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private void saveDecks(ObservableList<Deck> decks) {

        Path path = Path.of("decks.txt");

        try {
            StringBuilder builder = new StringBuilder();

            for (Deck deck : decks) {
                builder.append(deck.getName())
                        .append("|")
                        .append(deck.getDescription())
                        .append("\n");
            }

            Files.writeString(path, builder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddFlashcardClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("define-flashcard-view.fxml"));

        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
    }
}
