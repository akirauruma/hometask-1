package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Domain interface, implemented by {@link Level}: tells whether a tile can be entered. */
public interface CollisionChecker {

    boolean isFree(GridPoint2 coordinates);
}
