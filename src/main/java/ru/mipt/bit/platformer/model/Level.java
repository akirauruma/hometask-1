package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The playing field: its size, everything standing on it, and the rules of who may go where.
 * <p>
 * The level is the only place that knows about collisions, so a tank does not have to
 * ask the trees anything and the trees do not have to know about tanks.
 */
public class Level {

    private final int width;
    private final int height;
    private final Tank playerTank;
    private final List<Tree> trees;

    public Level(int width, int height, Tank playerTank, List<Tree> trees) {
        this.width = width;
        this.height = height;
        this.playerTank = playerTank;
        this.trees = new ArrayList<>(trees);
    }

    /**
     * Send the player tank one tile in the given direction.
     * If the tile is taken or outside the field, the tank only turns, exactly as before.
     */
    public void movePlayerTank(Direction direction) {
        if (playerTank.isMoving()) {
            // a started move is never interrupted
            return;
        }
        if (isFree(direction.apply(playerTank.getCoordinates()))) {
            playerTank.move(direction);
        } else {
            playerTank.turn(direction);
        }
    }

    public void update(float deltaTime) {
        playerTank.update(deltaTime);
    }

    /** A tile is free if it is inside the field and nothing stands on it. */
    public boolean isFree(GridPoint2 coordinates) {
        return isInside(coordinates) && !isOccupied(coordinates);
    }

    private boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }

    private boolean isOccupied(GridPoint2 coordinates) {
        for (Tree tree : trees) {
            if (tree.getCoordinates().equals(coordinates)) {
                return true;
            }
        }
        return false;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public List<Tree> getTrees() {
        return Collections.unmodifiableList(trees);
    }
}
