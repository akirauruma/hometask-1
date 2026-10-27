package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

/** The field: its size and objects. Knows nothing about concrete object types. */
public class Level implements CollisionChecker {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void add(GameObject object) {
        objects.add(object);
    }

    public void update(float deltaTime) {
        for (GameObject object : objects) {
            object.update(deltaTime);
        }
    }

    /** Inside the field and no object stands on it or moves into it. */
    @Override
    public boolean isFree(GridPoint2 coordinates) {
        return isInside(coordinates) && !isOccupied(coordinates);
    }

    private boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }

    private boolean isOccupied(GridPoint2 coordinates) {
        for (GameObject object : objects) {
            if (object.getCoordinates().equals(coordinates) || object.getDestination().equals(coordinates)) {
                return true;
            }
        }
        return false;
    }
}
