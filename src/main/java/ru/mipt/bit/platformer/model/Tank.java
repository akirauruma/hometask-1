package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Asks {@link CollisionChecker} before moving, fires and leaves the level through {@link Battlefield}.
 * Knows nothing about the level itself. A destroyed tank does nothing.
 */
public class Tank implements GameObject, Movable, Shooter, Damageable {

    static final int INITIAL_HEALTH = 3;
    static final int BULLET_DAMAGE = 1;
    /** Seconds between shots: a bullet needs two tile times to clear the tile in front of the tank. */
    static final float RELOAD_TIME = 0.5f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    /** Seconds per tile. */
    private final float movementSpeed;
    private final CollisionChecker collisionChecker;
    private final Battlefield battlefield;
    private Direction direction;
    private float movementProgress = 1f;
    private int health = INITIAL_HEALTH;
    private float reloadLeft = 0f;

    public Tank(GridPoint2 startCoordinates, Direction startDirection, float movementSpeed,
                CollisionChecker collisionChecker, Battlefield battlefield) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.direction = startDirection;
        this.movementSpeed = movementSpeed;
        this.collisionChecker = collisionChecker;
        this.battlefield = battlefield;
    }

    /**
     * The bullet starts on the tile the tank stands on or drives into and only hits what lies
     * ahead, so it never hurts its own tank.
     */
    @Override
    public void shoot() {
        if (!isAlive() || reloadLeft > 0f) {
            return;
        }
        battlefield.add(new Bullet(destination, direction, BULLET_DAMAGE, battlefield));
        reloadLeft = RELOAD_TIME;
    }

    @Override
    public void takeDamage(int damage) {
        if (!isAlive()) {
            return;
        }
        health -= damage;
        if (!isAlive()) {
            battlefield.remove(this);
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    /** Always turns; moves only if the tile is free. A started move is not interrupted. */
    @Override
    public void move(Direction newDirection) {
        if (!isAlive() || isMoving()) {
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
        reloadLeft = Math.max(0f, reloadLeft - deltaTime);
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
