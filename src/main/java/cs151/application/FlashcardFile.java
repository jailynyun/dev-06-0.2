package cs151.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Flashcards file format (v0.8):
 * deckTitle|question|answer|status|createdAt|lastReviewedAt
 *
 * Older format (v0.6/v0.7):
 * deckTitle|question|answer|status|createdAt
 */
public class FlashcardFile {

    private static final Path FILE = Path.of("flashcards.txt");

    public static List<Flashcard> loadAll() {
        List<Flashcard> list = new ArrayList<>();
        if (!Files.exists(FILE)) return list;

        try {
            for (String line : Files.readAllLines(FILE)) {
                if (line == null || line.isBlank()) continue;

                // Use split without limit to support both 5 and 6 fields
                String[] parts = line.split("\\|");
                if (parts.length != 5 && parts.length != 6) continue;

                String deck = parts[0].trim();
                String question = parts[1].replace("\\n", "\n");
                String answer = parts[2].replace("\\n", "\n");
                String status = parts[3].trim();
                LocalDateTime createdAt = LocalDateTime.parse(parts[4].trim());

                LocalDateTime lastReviewedAt = createdAt; // default for older files
                if (parts.length == 6) {
                    lastReviewedAt = LocalDateTime.parse(parts[5].trim());
                }

                Flashcard fc = new Flashcard(question, answer, deck, status, createdAt, lastReviewedAt);
                list.add(fc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Deletes a specific flashcard from the file based on its content and metadata.
     *
     * @param target flashcard to remove
     */
    public static void deleteOne(Flashcard target) {
        List<Flashcard> all = loadAll();

        all.removeIf(fc ->
                fc.getDeckTitle().equalsIgnoreCase(target.getDeckTitle())
                        && fc.getCreatedAt().equals(target.getCreatedAt())
                        && fc.getQuestion().equals(target.getQuestion())
                        && fc.getAnswer().equals(target.getAnswer())
        );

        rewriteAll(all);
    }

    /**
     * Overwrites the entire flashcard file with the provided list.
     * <p>
     * Each flashcard is written in a pipe-delimited format, with
     * newline characters escaped.
     *
     * @param all list of flashcards to write to file
     */
    public static void rewriteAll(List<Flashcard> all) {
        try (BufferedWriter w = Files.newBufferedWriter(
                FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Flashcard fc : all) {

                // encode multi-line textareas
                String q = fc.getQuestion().replace("\n", "\\n");
                String a = fc.getAnswer().replace("\n", "\\n");

                String line = fc.getDeckTitle() + "|" +
                        q + "|" +
                        a + "|" +
                        fc.getStatus() + "|" +
                        fc.getCreatedAt() + "|" +
                        fc.getLastReviewedAt();

                w.write(line);
                w.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
