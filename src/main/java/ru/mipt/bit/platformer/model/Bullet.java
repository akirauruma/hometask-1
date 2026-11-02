package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Bullet implements GameObject {

    private static final float MOVEMENT_SPEED = 0.1f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private final Direction direction;
    private final int damage;
    private final Level level;
    private float movementProgress = 1f;

    public Bullet(GridPoint2 startCoordinates, Direction direction, int damage, Level level) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destination = new GridPoint2(startCoordinates);
        this.direction = direction;
        this.damage = damage;
        this.level = level;
    }

    @Override
    public void update(float deltaTime) {
        if (collide()) {
            return;
        }
        if (!isMoving()) {
            GridPoint2 next = direction.apply(coordinates);
            if (!level.isFree(next)) {
                level.remove(this);
                return;
            }
            destination.set(next);
            movementProgress = 0f;
        }
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            coordinates.set(destination);
            collide();
        }
    }

    private boolean collide() {
        for (GameObject object : level.objects()) {
            if (object == this || !object.getCoordinates().equals(coordinates)
                    && !object.getDestination().equals(coordinates)) {
                continue;
            }
            if (object instanceof Tank) {
                ((Tank) object).takeDamage(damage);
            }
            level.remove(this);
            if (object instanceof Bullet) {
                level.remove(object);
            }
            return true;
        }
        return false;
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
