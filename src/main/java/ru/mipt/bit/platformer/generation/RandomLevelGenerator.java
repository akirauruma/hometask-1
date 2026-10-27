package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Random player start, trees and enemy tanks, all on different tiles. */
public class RandomLevelGenerator implements LevelGenerator {

    private final int treeCount;
    private final int enemyCount;
    private final Random random;

    public RandomLevelGenerator(int treeCount, int enemyCount, Random random) {
        if (treeCount < 0 || enemyCount < 0) {
            throw new IllegalArgumentException("Negative count: " + treeCount + " trees, " + enemyCount + " enemies");
        }
        this.treeCount = treeCount;
        this.enemyCount = enemyCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate(int width, int height) {
        List<GridPoint2> tiles = allTiles(width, height);
        // one tile is left for the player
        if (treeCount + enemyCount >= tiles.size()) {
            throw new IllegalArgumentException(treeCount + " trees, " + enemyCount
                    + " enemies and the player do not fit into " + width + "x" + height);
        }
        Collections.shuffle(tiles, random);
        int treesEnd = 1 + treeCount;
        return new LevelLayout(tiles.get(0),
                tiles.subList(1, treesEnd),
                tiles.subList(treesEnd, treesEnd + enemyCount));
    }

    private static List<GridPoint2> allTiles(int width, int height) {
        List<GridPoint2> tiles = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles.add(new GridPoint2(x, y));
            }
        }
        return tiles;
    }
}
