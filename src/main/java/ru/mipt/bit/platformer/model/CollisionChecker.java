package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Tells whether a tile can be entered. */
public interface CollisionChecker {

    boolean isFree(GridPoint2 coordinates);
}
