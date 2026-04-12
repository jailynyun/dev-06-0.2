package cs151.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling flashcard file operations.
 * Responsible for loading, parsing, and deleting flashcards from persistent storage.
 *
 * Flashcards are stored in a flat file using the format:
 * deckTitle|question|answer|status|createdAt
 *
 * This class ensures data consistency between the UI and file system.
 *
 * @author Jailyn
 * @author Thi Phuc Thinh
 * @author Naman Kumar (JavaDoc Documentation)
 * @version 0.6
 */
public class FlashcardFile {

    private static final Path FILE = Path.of("flashcards.txt");

    /**
     * Loads all flashcards from the flashcards.txt file.
     *
     * Each line is parsed into a Flashcard object using the format:
     * deckTitle|question|answer|status|createdAt
     *
     * Supports multi-line text by converting escaped "\\n" back into real newlines.
     *
     * @return a list of all Flashcard objects stored in the file.
     */
    public static List<Flashcard> loadAll() {
        List<Flashcard> list = new ArrayList<>();
        if (!Files.exists(FILE)) return list;

        try {
            for (String line : Files.readAllLines(FILE)) {
                if (line == null || line.isBlank()) continue;

                String[] parts = line.split("\\|", 5);
                if (parts.length != 5) continue;

                String deck = parts[0].trim();
                String question = parts[1].replace("\\n","\n");
                String answer = parts[2].replace("\\n","\n");
                String status = parts[3].trim();
                LocalDateTime createdAt = LocalDateTime.parse(parts[4].trim());

                Flashcard fc = new Flashcard(question, answer, deck, createdAt);
                fc.setStatus(status);
                list.add(fc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Deletes a specific flashcard from persistent storage.
     *
     * The flashcard is matched using all of the following fields:
     * deck title, question, answer, status, and created timestamp.
     *
     * After removal, the file is rewritten with the remaining flashcards.
     *
     * @param target the flashcard to be deleted
     */
    public static void deleteOne(Flashcard target) {
        List<Flashcard> all = loadAll();

        all.removeIf(fc ->
                fc.getDeckTitle().equalsIgnoreCase(target.getDeckTitle())
                        && fc.getCreatedAt().equals(target.getCreatedAt())
                        && fc.getQuestion().equals(target.getQuestion())
                        && fc.getAnswer().equals(target.getAnswer())
                        && fc.getStatus().equalsIgnoreCase(target.getStatus())
        );

        try (BufferedWriter w = Files.newBufferedWriter(
                FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Flashcard fc : all) {
                String line = fc.getDeckTitle() + "|" +
                        fc.getQuestion() + "|" +
                        fc.getAnswer() + "|" +
                        fc.getStatus() + "|" +
                        fc.getCreatedAt();
                w.write(line);
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
