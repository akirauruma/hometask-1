package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

/** The field: its size and objects. Knows nothing about concrete object types. */
public class Level implements CollisionChecker {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();
    private final List<LevelObserver> observers = new ArrayList<>();
    private final List<GameObject> pendingRemoval = new ArrayList<>();

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void add(GameObject object) {
        objects.add(object);
        observers.forEach(observer -> observer.objectAdded(object));
    }

    public void remove(GameObject object) {
        if (objects.contains(object) && !pendingRemoval.contains(object)) {
            pendingRemoval.add(object);
        }
    }

    public void subscribe(LevelObserver observer) {
        observers.add(observer);
        objects.forEach(observer::objectAdded);
    }

    List<GameObject> objects() {
        return List.copyOf(objects);
    }

    public void update(float deltaTime) {
        for (GameObject object : List.copyOf(objects)) {
            object.update(deltaTime);
        }
        for (GameObject object : pendingRemoval) {
            if (objects.remove(object)) {
                observers.forEach(observer -> observer.objectRemoved(object));
            }
        }
        pendingRemoval.clear();
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
