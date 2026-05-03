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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Controller for searching, viewing, editing, and deleting flashcards.
 * <p>
 * This screen allows users to search across all flashcards by deck name,
 * front text, back text, or status. Users can also edit or delete flashcards,
 * and navigate back to the home screen.
 */
public class SearchFlashcardController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Flashcard> table;
    @FXML
    private TableColumn<Flashcard, String> deckCol;
    @FXML
    private TableColumn<Flashcard, String> frontCol;
    @FXML
    private TableColumn<Flashcard, String> backCol;
    @FXML
    private TableColumn<Flashcard, String> statusCol;
    @FXML
    private TableColumn<Flashcard, String> createdCol;

    private final ObservableList<Flashcard> master = FXCollections.observableArrayList();
    private final ObservableList<Flashcard> filtered = FXCollections.observableArrayList();

    /**
     * Initializes the controller after FXML loading.
     * <p>
     * Loads all flashcards from file, sets up table columns,
     * enables live search, and configures double-click editing.
     */
    @FXML
    public void initialize() {
        // Load all flashcards once when opening this page
        List<Flashcard> all = FlashcardFile.loadAll();
        master.setAll(all);

        deckCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getDeckTitle()));
        frontCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(firstLine(c.getValue().getFrontText())));
        backCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(firstLine(c.getValue().getBackText())));
        statusCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getStatus()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        createdCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCreatedAt().format(fmt)));

        // default: show all
        filtered.setAll(master);
        table.setItems(filtered);

        // Double-click a row to edit flashcard
        table.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Flashcard> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Flashcard selected = row.getItem();
                    openEditFlashcard(selected);
                }
            });
            return row;
        });

        // optional live-search as you type
        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilter(newV));
    }
    /**
     * Handles search button click.
     */
    @FXML
    private void onSearchClicked() {
        applyFilter(searchField.getText());
    }

    /**
     * Filters flashcards based on search query.
     *
     * @param q search input string
     */
    private void applyFilter(String q) {
        String query = (q == null) ? "" : q.trim().toLowerCase(Locale.ROOT);

        if (query.isEmpty()) {
            filtered.setAll(master);
            return;
        }

        filtered.setAll(master.filtered(fc -> containsIgnoreCase(fc.getDeckTitle(), query)
                || containsIgnoreCase(fc.getFrontText(), query)
                || containsIgnoreCase(fc.getBackText(), query)
                || containsIgnoreCase(fc.getStatus(), query)));
    }

    /**
     * Checks if a string contains a query (case-insensitive).
     *
     * @param text original text
     * @param queryLower lowercase query string
     * @return true if text contains query
     */
    private boolean containsIgnoreCase(String text, String queryLower) {
        if (text == null)
            return false;
        return text.toLowerCase(Locale.ROOT).contains(queryLower);
    }

    /**
     * Returns the first line of a multi-line string.
     *
     * @param s input string
     * @return first line or empty string if null
     */
    private String firstLine(String s) {
        if (s == null)
            return "";
        String normalized = s.replace("\r\n", "\n");
        int idx = normalized.indexOf('\n');
        return (idx >= 0) ? normalized.substring(0, idx) : normalized;
    }

    /**
     * Deletes the selected flashcard permanently from storage
     * and refreshes the table view.
     */
    @FXML
    private void onDeleteClicked() {
        Flashcard selected = table.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        // Remove permanently from file (requirement)
        FlashcardFile.deleteOne(selected);

        // Reload master list from file so UI is always consistent
        master.setAll(FlashcardFile.loadAll());
        applyFilter(searchField.getText());
    }

    /**
     * Navigates back to the home screen.
     *
     * @param event button click event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * Opens the edit flashcard screen for the selected flashcard.
     *
     * @param selected flashcard to edit
     */
    private void openEditFlashcard(Flashcard selected) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("edit-flashcard-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 500);

            EditFlashcardController controller = loader.getController();
            controller.setFlashcard(selected);

            Stage stage = (Stage) table.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
