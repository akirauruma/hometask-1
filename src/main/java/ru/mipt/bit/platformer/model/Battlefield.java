package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Optional;

/**
 * What objects that spawn, hit and destroy other objects need from the level.
 * Passed explicitly, so nobody has to downcast a {@link CollisionChecker}.
 */
public interface Battlefield {

    boolean isInside(GridPoint2 coordinates);

    /** The object standing on the tile or moving into it, if any. */
    Optional<GameObject> objectAt(GridPoint2 coordinates);

    void add(GameObject object);

    /** The object stops acting and blocking at once and leaves at the end of the current update. */
    void remove(GameObject object);
}
