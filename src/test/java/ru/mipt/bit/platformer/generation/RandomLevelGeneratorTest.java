package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    @Test
    void placesTheRequestedNumberOfTrees() {
        LevelLayout layout = new RandomLevelGenerator(10, 0, new Random(42)).generate(10, 8);
        assertEquals(10, layout.getTrees().size());
    }

    @Test
    void everythingIsInsideTheLevelAndOnDifferentTiles() {
        LevelLayout layout = new RandomLevelGenerator(20, 0, new Random(42)).generate(10, 8);

        Set<GridPoint2> occupied = new HashSet<>(layout.getTrees());
        assertEquals(20, occupied.size());
        assertFalse(occupied.contains(layout.getPlayerStart()));

        occupied.add(layout.getPlayerStart());
        for (GridPoint2 tile : occupied) {
            assertTrue(tile.x >= 0 && tile.x < 10 && tile.y >= 0 && tile.y < 8, tile.toString());
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        LevelLayout first = new RandomLevelGenerator(10, 0, new Random(7)).generate(10, 8);
        LevelLayout second = new RandomLevelGenerator(10, 0, new Random(7)).generate(10, 8);
        assertEquals(first.getPlayerStart(), second.getPlayerStart());
        assertEquals(first.getTrees(), second.getTrees());
    }

    @Test
    void canFillEveryTileButThePlayerOne() {
        LevelLayout layout = new RandomLevelGenerator(3, 0, new Random(42)).generate(2, 2);
        Set<GridPoint2> all = new HashSet<>(layout.getTrees());
        all.add(layout.getPlayerStart());
        assertEquals(Set.of(new GridPoint2(0, 0), new GridPoint2(0, 1), new GridPoint2(1, 0), new GridPoint2(1, 1)), all);
    }

    @Test
    void failsWhenThereIsNoTileLeftForThePlayer() {
        RandomLevelGenerator generator = new RandomLevelGenerator(4, 0, new Random(42));
        assertThrows(IllegalArgumentException.class, () -> generator.generate(2, 2));
    }

    @Test
    void rejectsNegativeTreeCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(-1, 0, new Random()));
    }

    @Test
    void zeroTreesGivesOnlyThePlayer() {
        LevelLayout layout = new RandomLevelGenerator(0, 0, new Random(42)).generate(10, 8);
        assertEquals(List.of(), layout.getTrees());
        assertEquals(List.of(), layout.getEnemyTanks());
    }

    @Test
    void placesEnemyTanksOnTilesFreeOfTreesAndThePlayer() {
        LevelLayout layout = new RandomLevelGenerator(20, 5, new Random(42)).generate(10, 8);

        assertEquals(5, layout.getEnemyTanks().size());
        Set<GridPoint2> occupied = new HashSet<>(layout.getTrees());
        occupied.add(layout.getPlayerStart());
        for (GridPoint2 enemy : layout.getEnemyTanks()) {
            assertTrue(occupied.add(enemy), enemy.toString());
        }
    }

    @Test
    void enemiesAlsoNeedRoom() {
        RandomLevelGenerator generator = new RandomLevelGenerator(2, 2, new Random(42));
        assertThrows(IllegalArgumentException.class, () -> generator.generate(2, 2));
    }

    @Test
    void rejectsNegativeEnemyCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(0, -1, new Random()));
    }
}
