package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Reads the level from a text file: T — tree, X — player, _ — empty tile. */
public class FileLevelGenerator implements LevelGenerator {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final Path file;

    public FileLevelGenerator(Path file) {
        this.file = file;
    }

    @Override
    public LevelLayout generate(int width, int height) {
        List<String> rows = readRows();
        checkSize(rows, width, height);

        GridPoint2 playerStart = null;
        List<GridPoint2> trees = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            // the first line is the top of the level
            int y = height - 1 - row;
            for (int x = 0; x < width; x++) {
                char cell = rows.get(row).charAt(x);
                switch (cell) {
                    case TREE:
                        trees.add(new GridPoint2(x, y));
                        break;
                    case PLAYER:
                        if (playerStart != null) {
                            throw new IllegalArgumentException("More than one player in " + file);
                        }
                        playerStart = new GridPoint2(x, y);
                        break;
                    case EMPTY:
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unknown tile '" + cell + "' in line " + (row + 1) + " of " + file);
                }
            }
        }
        if (playerStart == null) {
            throw new IllegalArgumentException("No player in " + file);
        }
        return new LevelLayout(playerStart, trees, List.of());
    }

    private List<String> readRows() {
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8).stream()
                    .map(String::strip)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read level " + file, e);
        }
    }

    private void checkSize(List<String> rows, int width, int height) {
        boolean fits = rows.size() == height && rows.stream().allMatch(row -> row.length() == width);
        if (!fits) {
            throw new IllegalArgumentException("Level in " + file + " must be " + width + "x" + height);
        }
    }
}
