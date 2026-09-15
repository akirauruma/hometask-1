package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * A tank: where it stands, where it is going, which way it looks.
 * Knows nothing about textures, keys or the level — it only owns its own state.
 */
public class Tank implements GameObject {

    /** Seconds it takes to cross one tile. */
    private static final float MOVEMENT_SPEED = 0.4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private Direction direction;
    private float movementProgress = 1f;

    public Tank(GridPoint2 startCoordinates, Direction startDirection) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.direction = startDirection;
    }

    /** Turn without leaving the tile — used when the way is blocked. */
    public void turn(Direction newDirection) {
        this.direction = newDirection;
    }

    /** Start moving to the neighbouring tile. Caller has already checked that the tile is free. */
    public void move(Direction newDirection) {
        turn(newDirection);
        destination.set(newDirection.apply(coordinates));
        movementProgress = 0f;
    }

    /** Advance the current move by the time passed since the previous frame. */
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            // the tank has reached its destination
            coordinates.set(destination);
        }
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getDestination() {
        return destination;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return direction.getRotation();
    }
}
