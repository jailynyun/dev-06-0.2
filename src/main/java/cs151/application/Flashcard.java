package cs151.application;

import java.time.LocalDateTime;

/**
 * Represents a flashcard containing a question and answer.
 * Each flashcard belongs to a specific deck and has timestamps.
 *
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.8
 */
public class Flashcard {

    private String question;
    private String answer;
    private String deckTitle;
    private LocalDateTime createdAt;

    // 0.6+ status
    private String status = "NEW";

    // 0.8: last reviewed timestamp (required for Review feature)
    private LocalDateTime lastReviewedAt;

    public Flashcard(String question, String answer, String deckTitle) {
        this.question = question;
        this.answer = answer;
        this.deckTitle = deckTitle;
        this.createdAt = LocalDateTime.now();
        this.lastReviewedAt = this.createdAt; // default
    }

    public Flashcard(String question, String answer, String deckTitle, LocalDateTime createdAt) {
        this.question = question;
        this.answer = answer;
        this.deckTitle = deckTitle;
        this.createdAt = createdAt;
        this.lastReviewedAt = createdAt; // default
    }

    // Full constructor (used when loading from file v0.8)
    public Flashcard(String question, String answer, String deckTitle,
                     String status, LocalDateTime createdAt, LocalDateTime lastReviewedAt) {
        this.question = question;
        this.answer = answer;
        this.deckTitle = deckTitle;
        this.status = (status == null || status.isBlank()) ? "NEW" : status;
        this.createdAt = createdAt;
        this.lastReviewedAt = (lastReviewedAt == null) ? createdAt : lastReviewedAt;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getDeckTitle() { return deckTitle; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // 0.8 required
    public LocalDateTime getLastReviewedAt() { return lastReviewedAt; }
    public void setLastReviewedAt(LocalDateTime t) {
        this.lastReviewedAt = (t == null) ? this.lastReviewedAt : t;
    }

    // SEARCH/REVIEW
    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = (status == null || status.isBlank()) ? "NEW" : status;
    }

    // setters for review edits
    public void setQuestion(String question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }

    // aliases
    public String getFrontText() { return question; }
    public String getBackText() { return answer; }
}
