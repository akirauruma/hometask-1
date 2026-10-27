package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Grid direction: tile shift and sprite rotation. */
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

    /** Neighbour tile, the argument is not modified. */
    public GridPoint2 apply(GridPoint2 from) {
        return new GridPoint2(from).add(shiftX, shiftY);
    }

    public float getRotation() {
        return rotation;
    }
}
