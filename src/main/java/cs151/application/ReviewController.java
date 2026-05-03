package cs151.application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
/**
 * Controller for the deck review selection screen.
 * <p>
 * This class handles displaying all available flashcard decks,
 * enabling live search/filtering, and opening a selected deck
 * for review. It also supports navigation back to the home screen.
 */
public class ReviewController {

    @FXML private TextField searchField;

    @FXML private TableView<Deck> deckTable;
    @FXML private TableColumn<Deck, String> nameCol;
    @FXML private TableColumn<Deck, String> descCol;

    private final ObservableList<Deck> master = FXCollections.observableArrayList();
    private final ObservableList<Deck> filtered = FXCollections.observableArrayList();

    /**
     * Initializes the controller after the FXML fields are loaded.
     * <p>
     * Loads all decks from file, sets up table columns, binds data,
     * and configures live search and row click behavior.
     */
    @FXML
    public void initialize() {
        List<Deck> all = DeckFile.loadAll();
        master.setAll(all);

        nameCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getName()));
        descCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(firstLine(c.getValue().getDescription())));

        filtered.setAll(master);
        deckTable.setItems(filtered);

        // live search
        searchField.textProperty().addListener((obs, o, n) -> applyFilter(n));

        // single click opens deck review page
        deckTable.setRowFactory(tv -> {
            TableRow<Deck> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1 && !row.isEmpty()) {
                    openDeck(row.getItem());
                }
            });
            return row;
        });
    }

    /**
     * Handles search button click and applies filter based on input text.
     */
    @FXML
    private void onSearchClicked() {
        applyFilter(searchField.getText());
    }

    /**
     * Filters the deck list based on the search query.
     *
     * @param q the search string entered by the user
     */
    private void applyFilter(String q) {
        String query = (q == null) ? "" : q.trim().toLowerCase(Locale.ROOT);

        if (query.isEmpty()) {
            filtered.setAll(master);
            return;
        }

        filtered.setAll(master.filtered(d ->
                d.getName().toLowerCase(Locale.ROOT).contains(query)
                        || d.getDescription().toLowerCase(Locale.ROOT).contains(query)
        ));
    }

    /**
     * Extracts the first line from a multi-line string.
     *
     * @param s input string
     * @return first line of the string, or empty string if null
     */
    private String firstLine(String s) {
        if (s == null) return "";

        // convert stored "\n" into real newline
        String normalized = s.replace("\\n", "\n");

        // normalize Windows newlines too (optional but good)
        normalized = normalized.replace("\r\n", "\n");

        int idx = normalized.indexOf('\n');
        return (idx >= 0) ? normalized.substring(0, idx) : normalized;
    }

    /**
     * Opens the selected deck in the deck review screen.
     *
     * @param selected the deck selected from the table
     */
    private void openDeck(Deck selected) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("deck-review-view.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);

            DeckReviewController controller = loader.getController();
            controller.setDeck(selected);

            Stage stage = (Stage) deckTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the back button click and navigates to the home screen.
     *
     * @param event the action event triggered by button click
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
