package cs151.application;

import java.time.LocalDateTime;

/**
 * Represents a flashcard containing a question and answer.
 * Each flashcard belongs to a specific deck and has a creation timestamp.
 *
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.5
 */
public class Flashcard {

    private String question;
    private String answer;
    private String deckTitle;
    private LocalDateTime createdAt;
    private String status = "NEW";

/**
 * Constructs a Flashcard with the given question, answer, and deck title.
 * The creation time is set to the current date and time.
 *
 * @param question The question text of the flashcard.
 * @param answer The answer text of the flashcard.
 * @param deckTitle The title of the deck this flashcard belongs to.
 */
    public Flashcard(String question, String answer, String deckTitle) {
        this.question = question;
        this.answer = answer;
        this.deckTitle = deckTitle;
        this.createdAt = LocalDateTime.now();
    }

/**
 * Constructs a Flashcard with all fields specified.
 *
 * @param question The question text of the flashcard.
 * @param answer The answer text of the flashcard.
 * @param deckTitle The title of the deck this flashcard belongs to.
 * @param createdAt The date and time when the flashcard was created.
 */
    public Flashcard(String question, String answer, String deckTitle, LocalDateTime createdAt) {
        this.question = question;
        this.answer = answer;
        this.deckTitle = deckTitle;
        this.createdAt = createdAt;
    }


/**
 * Gets the question of the flashcard.
 * @return The question text.
 */
    public String getQuestion() { return question; }

/**
 * Gets the answer of the flashcard.
 * @return The answer text.
 */
    public String getAnswer() { return answer; }

/**
 * Gets the deck title associated with this flashcard.
 * @return The deck title.
 */
    public String getDeckTitle() { return deckTitle; }

/**
 * Gets the creation timestamp of the flashcard.
 * @return The date and time the flashcard was created.
 */
    public LocalDateTime getCreatedAt() { return createdAt; }

    // SEARCH feature
    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = (status == null || status.isBlank()) ? "NEW" : status;
    }
    public String getFrontText() { return question; }
    public String getBackText() { return answer; }

}
