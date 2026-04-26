package cs151.application;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DeckFile {

    private static final Path FILE = Path.of("decks.txt");

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
