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

public class DeckTableController {

    @FXML
    private TableView<Deck> deckTable;

    @FXML
    private TableColumn<Deck, String> nameColumn;

    @FXML
    private TableColumn<Deck, String> descriptionColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getName()));

        descriptionColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDescription()));

        ObservableList<Deck> decks = loadDecks();

        decks.sort(Comparator.comparing(Deck::getName, String.CASE_INSENSITIVE_ORDER));

        deckTable.setItems(decks);
    }

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

    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("define-deck-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
