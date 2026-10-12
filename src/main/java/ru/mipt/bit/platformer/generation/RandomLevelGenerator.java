package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Random trees and a random player start, all on different tiles. */
public class RandomLevelGenerator implements LevelGenerator {

    private final int treeCount;
    private final Random random;

    public RandomLevelGenerator(int treeCount, Random random) {
        if (treeCount < 0) {
            throw new IllegalArgumentException("Negative tree count: " + treeCount);
        }
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate(int width, int height) {
        List<GridPoint2> tiles = allTiles(width, height);
        // one tile is left for the player
        if (treeCount >= tiles.size()) {
            throw new IllegalArgumentException(
                    treeCount + " trees and the player do not fit into " + width + "x" + height);
        }
        Collections.shuffle(tiles, random);
        return new LevelLayout(tiles.get(0), tiles.subList(1, treeCount + 1));
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
