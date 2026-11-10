package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

/** What stands on the level: the player start, the trees and the enemy tanks. */
public class LevelLayout {

    private final GridPoint2 playerStart;
    private final List<GridPoint2> trees;
    private final List<GridPoint2> enemyTanks;

    public LevelLayout(GridPoint2 playerStart, List<GridPoint2> trees, List<GridPoint2> enemyTanks) {
        this.playerStart = new GridPoint2(playerStart);
        this.trees = List.copyOf(trees);
        this.enemyTanks = List.copyOf(enemyTanks);
    }

    public GridPoint2 getPlayerStart() {
        return playerStart;
    }

    public List<GridPoint2> getTrees() {
        return trees;
    }

    public List<GridPoint2> getEnemyTanks() {
        return enemyTanks;
    }
}
