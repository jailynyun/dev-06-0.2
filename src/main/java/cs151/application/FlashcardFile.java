package cs151.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlashcardFile {

    private static final Path FILE = Path.of("flashcards.txt");

    // Expected format per line:
    // deckTitle|question|answer|status|createdAt
    public static List<Flashcard> loadAll() {
        List<Flashcard> list = new ArrayList<>();
        if (!Files.exists(FILE)) return list;

        try {
            for (String line : Files.readAllLines(FILE)) {
                if (line == null || line.isBlank()) continue;

                String[] parts = line.split("\\|", 5);
                if (parts.length != 5) continue;

                String deck = parts[0].trim();
                String question = parts[1];
                String answer = parts[2];
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
