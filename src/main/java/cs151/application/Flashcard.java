package cs151.application;

import java.time.LocalDateTime;

public class Flashcard {

    private String question;
    private String answer;
    private String deckTitle;
    private LocalDateTime createdAt;

    public Flashcard(String question, String answer, String deckTitle) {
        this.question = question;
        this.answer = answer;
        this.deckTitle = deckTitle;
        this.createdAt = LocalDateTime.now();
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getDeckTitle() { return deckTitle; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
