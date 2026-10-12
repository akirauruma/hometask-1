package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileLevelGeneratorTest {

    /** The example from the task. */
    private static final List<String> EXAMPLE = List.of(
            "___T__T___",
            "__TT__TTTT",
            "_________T",
            "TTTT__T__T",
            "_____X____",
            "__________");

    @TempDir
    Path tempDir;

    private FileLevelGenerator generatorFor(List<String> lines) throws IOException {
        Path file = tempDir.resolve("level.txt");
        Files.write(file, lines);
        return new FileLevelGenerator(file);
    }

    @Test
    void readsThePlayerStartCountingRowsFromTheTop() throws IOException {
        LevelLayout layout = generatorFor(EXAMPLE).generate(10, 6);
        assertEquals(new GridPoint2(5, 1), layout.getPlayerStart());
    }

    @Test
    void readsAllTrees() throws IOException {
        LevelLayout layout = generatorFor(EXAMPLE).generate(10, 6);

        Set<GridPoint2> trees = new HashSet<>(layout.getTrees());
        assertEquals(15, layout.getTrees().size());
        assertEquals(15, trees.size());
        // top row
        assertTrue(trees.contains(new GridPoint2(3, 5)));
        assertTrue(trees.contains(new GridPoint2(6, 5)));
        // left end of "TTTT__T__T"
        assertTrue(trees.contains(new GridPoint2(0, 2)));
        assertFalse(trees.contains(new GridPoint2(0, 0)));
    }

    @Test
    void ignoresEmptyLinesAndSurroundingSpaces() throws IOException {
        LevelLayout layout = generatorFor(List.of("  T_ ", "_X", "", "")).generate(2, 2);
        assertEquals(new GridPoint2(1, 0), layout.getPlayerStart());
        assertEquals(List.of(new GridPoint2(0, 1)), layout.getTrees());
    }

    @Test
    void failsOnWrongHeight() throws IOException {
        FileLevelGenerator generator = generatorFor(EXAMPLE);
        assertThrows(IllegalArgumentException.class, () -> generator.generate(10, 8));
    }

    @Test
    void failsOnWrongWidth() throws IOException {
        FileLevelGenerator generator = generatorFor(List.of("X_", "___"));
        assertThrows(IllegalArgumentException.class, () -> generator.generate(2, 2));
    }

    @Test
    void failsWithoutPlayer() throws IOException {
        FileLevelGenerator generator = generatorFor(List.of("T_", "__"));
        assertThrows(IllegalArgumentException.class, () -> generator.generate(2, 2));
    }

    @Test
    void failsWithTwoPlayers() throws IOException {
        FileLevelGenerator generator = generatorFor(List.of("X_", "_X"));
        assertThrows(IllegalArgumentException.class, () -> generator.generate(2, 2));
    }

    @Test
    void failsOnUnknownTile() throws IOException {
        FileLevelGenerator generator = generatorFor(List.of("X?", "__"));
        assertThrows(IllegalArgumentException.class, () -> generator.generate(2, 2));
    }

    @Test
    void failsOnMissingFile() {
        FileLevelGenerator generator = new FileLevelGenerator(tempDir.resolve("missing.txt"));
        assertThrows(UncheckedIOException.class, () -> generator.generate(2, 2));
    }

    @Test
    void sampleLevelFitsTheMap() {
        LevelLayout layout = new FileLevelGenerator(Path.of("levels/level.txt")).generate(10, 8);
        assertEquals(new GridPoint2(5, 1), layout.getPlayerStart());
    }
}
