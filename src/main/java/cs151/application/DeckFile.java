package cs151.application;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing deck data to persistent storage.
 * <p>
 * Handles serialization of Deck objects to and from the decks.txt file
 * using a simple pipe-delimited format.
 */
public class DeckFile {

    private static final Path FILE = Path.of("decks.txt");

    /**
     * Loads all decks from the decks.txt file.
     * <p>
     * File format per line:
     * deckName|description
     *
     * @return list of Deck objects loaded from file
     */
    // decks.txt format:
    // deckName|description
    public static List<Deck> loadAll() {
        List<Deck> list = new ArrayList<>();
        if (!Files.exists(FILE)) return list;

        try {
            for (String line : Files.readAllLines(FILE)) {
                if (line == null || line.isBlank()) continue;

                String[] parts = line.split("\\|", 2);
                String name = parts[0].trim();
                String desc = (parts.length == 2) ? parts[1].trim() : "";

                list.add(new Deck(name, desc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Saves all decks to the decks.txt file.
     * <p>
     * Overwrites existing file contents.
     * Format per line:
     * deckName|description
     *
     * @param decks list of decks to persist
     */
    // Optional but useful for 0.7 later (edit/delete deck):
    public static void saveAll(List<Deck> decks) {
        List<String> out = new ArrayList<>();
        for (Deck d : decks) {
            out.add(d.getName() + "|" + d.getDescription());
        }

        try {
            Files.write(FILE, out,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
