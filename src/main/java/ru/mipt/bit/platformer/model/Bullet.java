package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Optional;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Flies tile by tile. Before entering a tile it ends at the border, or hits whatever stands
 * on the tile or moves into it, or flies on. It never looks at the tile it is already on.
 */
public class Bullet implements GameObject, Damageable {

    /** Seconds per tile. */
    static final float MOVEMENT_SPEED = 0.1f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private final Direction direction;
    private final int damage;
    private final Battlefield battlefield;
    private float movementProgress = 1f;

    public Bullet(GridPoint2 startCoordinates, Direction direction, int damage, Battlefield battlefield) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.direction = direction;
        this.damage = damage;
        this.battlefield = battlefield;
    }

    @Override
    public void update(float deltaTime) {
        if (!isMoving()) {
            GridPoint2 next = direction.apply(coordinates);
            if (!battlefield.isInside(next)) {
                battlefield.remove(this);
                return;
            }
            Optional<GameObject> obstacle = battlefield.objectAt(next);
            if (obstacle.isPresent()) {
                hit(obstacle.get());
                return;
            }
            destination.set(next);
            movementProgress = 0f;
        }
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            coordinates.set(destination);
        }
    }

    /** The target takes the damage first (another bullet is destroyed by it), then this bullet is gone. */
    private void hit(GameObject target) {
        if (target instanceof Damageable damageable) {
            damageable.takeDamage(damage);
        }
        battlefield.remove(this);
    }

    /** Anything that hits a bullet destroys it. */
    @Override
    public void takeDamage(int damage) {
        battlefield.remove(this);
    }

    private boolean isMoving() {
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
