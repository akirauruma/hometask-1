package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/** Asks {@link CollisionChecker} before moving, knows nothing about the level. */
public class Tank implements GameObject, Movable {

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    /** Seconds per tile. */
    private final float movementSpeed;
    private final CollisionChecker collisionChecker;
    private Direction direction;
    private float movementProgress = 1f;

    public Tank(GridPoint2 startCoordinates, Direction startDirection,
                float movementSpeed, CollisionChecker collisionChecker) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.direction = startDirection;
        this.movementSpeed = movementSpeed;
        this.collisionChecker = collisionChecker;
    }

    /** Always turns; moves only if the tile is free. A started move is not interrupted. */
    @Override
    public void move(Direction newDirection) {
        if (isMoving()) {
            return;
        }
        direction = newDirection;
        GridPoint2 target = newDirection.apply(coordinates);
        if (collisionChecker.isFree(target)) {
            destination.set(target);
            movementProgress = 0f;
        }
    }

    @Override
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
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
