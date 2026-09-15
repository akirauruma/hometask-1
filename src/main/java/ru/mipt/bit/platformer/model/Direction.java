package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Orientation on the grid.
 * <p>
 * Knows two things: where a step in this direction leads ({@link #apply(GridPoint2)})
 * and how the sprite has to be rotated to look that way ({@link #getRotation()}).
 * This replaces the four hard-coded pairs of "shift coordinates + set rotation"
 * that used to be copy-pasted in the render loop.
 */
public enum Direction {

    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int shiftX;
    private final int shiftY;
    private final float rotation;

    Direction(int shiftX, int shiftY, float rotation) {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.rotation = rotation;
    }

    /** Coordinates of the neighbouring tile in this direction. The argument is not modified. */
    public GridPoint2 apply(GridPoint2 from) {
        return new GridPoint2(from).add(shiftX, shiftY);
    }

    public float getRotation() {
        return rotation;
    }
}
