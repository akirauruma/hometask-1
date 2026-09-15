package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Anything that occupies a tile and can be drawn.
 * <p>
 * A static object is just a degenerate moving one: it is always "at its destination",
 * so the renderer can treat trees and tanks with the same code.
 */
public interface GameObject {

    /** Tile the object is leaving (or standing on). */
    GridPoint2 getCoordinates();

    /** Tile the object is heading to. For a static object it is the same tile. */
    default GridPoint2 getDestination() {
        return getCoordinates();
    }

    /** 0 — just left the current tile, 1 — arrived at the destination. */
    default float getMovementProgress() {
        return 1f;
    }

    /** Sprite rotation in degrees. */
    default float getRotation() {
        return 0f;
    }
}
