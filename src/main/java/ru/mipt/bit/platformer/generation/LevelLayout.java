package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

/** What stands on the level: the player start and the trees. */
public class LevelLayout {

    private final GridPoint2 playerStart;
    private final List<GridPoint2> trees;

    public LevelLayout(GridPoint2 playerStart, List<GridPoint2> trees) {
        this.playerStart = new GridPoint2(playerStart);
        this.trees = List.copyOf(trees);
    }

    public GridPoint2 getPlayerStart() {
        return playerStart;
    }

    public List<GridPoint2> getTrees() {
        return trees;
    }
}
