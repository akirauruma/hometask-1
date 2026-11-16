package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.state.HealthState;
import ru.mipt.bit.platformer.model.state.TankState;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Domain entity. Asks {@link CollisionChecker} before moving, fires and leaves the level through {@link Battlefield}.
 * Knows nothing about the level itself. Speed and shooting depend on its {@link TankState}.
 * A destroyed tank does nothing.
 */
public class Tank implements GameObject, Movable, Shooter, Damageable, HasHealth {

    static final int BULLET_DAMAGE = 30;
    /** Seconds between shots: a bullet needs two tile times to clear the tile in front of the tank. */
    static final float RELOAD_TIME = 0.5f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    /** Seconds per tile. */
    private final float movementSpeed;
    private final CollisionChecker collisionChecker;
    private final Battlefield battlefield;
    private Direction direction;
    private final int maxHealth;
    private float movementProgress = 1f;
    private int health;
    private float reloadLeft = 0f;
    private TankState state;

    /** {@code health} is both the starting and the maximum health. */
    public Tank(GridPoint2 startCoordinates, Direction startDirection, float movementSpeed, int health,
                CollisionChecker collisionChecker, Battlefield battlefield) {
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive: " + health);
        }
        this.maxHealth = health;
        this.health = health;
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.direction = startDirection;
        this.movementSpeed = movementSpeed;
        this.collisionChecker = collisionChecker;
        this.battlefield = battlefield;
        this.state = HealthState.forHealth(health, health);
    }

    /**
     * The bullet starts on the tile the tank stands on or drives into and only hits what lies
     * ahead, so it never hurts its own tank.
     */
    @Override
    public void shoot() {
        if (!isAlive() || !state.canShoot() || reloadLeft > 0f) {
            return;
        }
        battlefield.add(new Bullet(destination, direction, BULLET_DAMAGE, battlefield));
        reloadLeft = state.reloadTime(RELOAD_TIME);
    }

    @Override
    public void takeDamage(int damage) {
        if (!isAlive()) {
            return;
        }
        health = Math.max(0, health - damage);
        state = state.onHealthChanged(health, maxHealth);
        if (!isAlive()) {
            battlefield.remove(this);
        }
    }

    public TankState getState() {
        return state;
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
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
        state = state.onTimePassed(deltaTime);
        reloadLeft = Math.max(0f, reloadLeft - deltaTime);
        movementProgress = continueProgress(movementProgress, deltaTime, state.movementTime(movementSpeed));
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
