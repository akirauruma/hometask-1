package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * A tree: a static obstacle that occupies exactly one tile.
 */
public class Tree implements GameObject {

    private final GridPoint2 coordinates;

    public Tree(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
