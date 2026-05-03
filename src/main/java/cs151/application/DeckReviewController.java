package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for reviewing flashcards inside a selected deck.
 * <p>
 * Supports navigation (next/previous), filtering by status,
 * and updating flashcard progress during review sessions.
 */
public class DeckReviewController {

    @FXML private Label deckNameLabel;
    @FXML private ComboBox<String> filterCombo;

    @FXML private TextArea frontArea;
    @FXML private TextArea backArea;
    @FXML private ComboBox<String> statusCombo;

    @FXML private Label createdAtLabel;
    @FXML private Label lastReviewedLabel;

    @FXML private Button prevBtn;
    @FXML private Button nextBtn;

    private Deck deck;
    private List<Flashcard> allForDeck = new ArrayList<>();
    private List<Flashcard> filtered = new ArrayList<>();
    private int index = 0;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Sets the deck to be reviewed and initializes UI components.
     *
     * @param deck selected deck for review session
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
        deckNameLabel.setText(deck.getName());

        // setup combos
        filterCombo.getItems().setAll("All", "NEW", "Learning", "Mastered");
        filterCombo.setValue("All");

        statusCombo.getItems().setAll("NEW", "Learning", "Mastered");

        // load data
        reloadFromFile();

        // react to filter change
        filterCombo.setOnAction(e -> {
            applyFilter();
            index = 0;
            showCurrent();
        });

        applyFilter();
        showCurrent();
    }

    /**
     * Loads all flashcards belonging to the current deck from file storage.
     */
    private void reloadFromFile() {
        List<Flashcard> all = FlashcardFile.loadAll();
        allForDeck.clear();
        for (Flashcard fc : all) {
            if (fc.getDeckTitle().equalsIgnoreCase(deck.getName())) {
                allForDeck.add(fc);
            }
        }
    }

    /**
     * Applies status filter to flashcards.
     */
    private void applyFilter() {
        String f = filterCombo.getValue();
        filtered.clear();

        if (f == null || f.equalsIgnoreCase("All")) {
            filtered.addAll(allForDeck);
        } else {
            for (Flashcard fc : allForDeck) {
                if (fc.getStatus().equalsIgnoreCase(f)) {
                    filtered.add(fc);
                }
            }
        }
    }

    /**
     * Displays the current flashcard in the review view.
     * Handles edge cases such as empty lists.
     */
    private void showCurrent() {
        if (filtered.isEmpty()) {
            frontArea.setText("");
            backArea.setText("");
            statusCombo.setValue("-");
            createdAtLabel.setText("-");
            lastReviewedLabel.setText("-");
            prevBtn.setDisable(true);
            nextBtn.setDisable(true);
            return;
        }

        if (index < 0) index = 0;
        if (index >= filtered.size()) index = filtered.size() - 1;

        Flashcard fc = filtered.get(index);

        frontArea.setText(fc.getQuestion());
        backArea.setText(fc.getAnswer());
        statusCombo.setValue(fc.getStatus());

        createdAtLabel.setText(fc.getCreatedAt().format(fmt));

        // show last reviewed as "current date" when reviewing (rubric)
        lastReviewedLabel.setText(LocalDateTime.now().format(fmt));

        prevBtn.setDisable(index == 0);
        nextBtn.setDisable(index == filtered.size() - 1);
    }

    /**
     * Moves to previous flashcard in the list.
     */
    @FXML
    private void onPrevClicked() {
        if (index > 0) {
            index--;
            showCurrent();
        }
    }

    /**
     * Moves to next flashcard in the list.
     */
    @FXML
    private void onNextClicked() {
        if (index < filtered.size() - 1) {
            index++;
            showCurrent();
        }
    }

    /**
     * Saves updates made to the current flashcard and persists changes.
     */
    @FXML
    private void onSaveClicked() {
        if (filtered.isEmpty()) return;

        Flashcard current = filtered.get(index);

        // update in memory
        current.setQuestion(frontArea.getText());
        current.setAnswer(backArea.getText());
        current.setStatus(statusCombo.getValue());
        current.setLastReviewedAt(LocalDateTime.now());

        // persist: update matching record by deck + createdAt
        List<Flashcard> all = FlashcardFile.loadAll();
        for (int i = 0; i < all.size(); i++) {
            Flashcard fc = all.get(i);
            if (fc.getDeckTitle().equalsIgnoreCase(current.getDeckTitle())
                    && fc.getCreatedAt().equals(current.getCreatedAt())) {
                all.set(i, current);
                break;
            }
        }
        FlashcardFile.rewriteAll(all);

        // reload + show
        reloadFromFile();
        applyFilter();
        showCurrent();
    }

    /**
     * Navigates back to the main review screen.
     *
     * @param event button click event
     * @throws IOException if FXML fails to load
     */
    @FXML
    private void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("review-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 500);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
