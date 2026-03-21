package cs151.application;

/**
 * Represents a flashcard deck with a name and description.
 * This is a simple data model used throughout the application.
 * 
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.2
 */
public class Deck {
    private final String name;
    private final String description;

    public Deck(String name, String description) {
        this.name = name;
        this.description = description;
    }

/**
 * Constructs a Deck object.
 * 
 * @param name The name of the deck.
 * @param description The description of the deck.
 */
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
