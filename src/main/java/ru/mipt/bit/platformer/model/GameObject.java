package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** An object on a tile. The defaults describe a static object. */
public interface GameObject {

    GridPoint2 getCoordinates();

    /** Same as coordinates for a static object. */
    default GridPoint2 getDestination() {
        return getCoordinates();
    }

    /** 0 — move just started, 1 — arrived. */
    default float getMovementProgress() {
        return 1f;
    }

    /** Sprite rotation in degrees. */
    default float getRotation() {
        return 0f;
    }

    default void update(float deltaTime) {
    }
}
