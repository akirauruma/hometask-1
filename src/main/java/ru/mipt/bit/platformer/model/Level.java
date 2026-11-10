package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/** The field: its size and objects. Knows nothing about concrete object types. */
public class Level implements CollisionChecker, Battlefield {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();
    private final List<LevelObserver> observers = new ArrayList<>();
    /** Stay in {@link #objects} until the end of the update, but no longer act or take up a tile. */
    private final Set<GameObject> pendingRemoval = new LinkedHashSet<>();

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void add(GameObject object) {
        objects.add(object);
        observers.forEach(observer -> observer.objectAdded(object));
    }

    @Override
    public void remove(GameObject object) {
        if (objects.contains(object)) {
            pendingRemoval.add(object);
        }
    }

    public void subscribe(LevelObserver observer) {
        observers.add(observer);
        objects.forEach(observer::objectAdded);
    }

    public void update(float deltaTime) {
        for (GameObject object : List.copyOf(objects)) {
            if (!pendingRemoval.contains(object)) {
                object.update(deltaTime);
            }
        }
        List<GameObject> removed = List.copyOf(pendingRemoval);
        pendingRemoval.clear();
        for (GameObject object : removed) {
            if (objects.remove(object)) {
                observers.forEach(observer -> observer.objectRemoved(object));
            }
        }
    }

    /** Inside the field and no object stands on it or moves into it. */
    @Override
    public boolean isFree(GridPoint2 coordinates) {
        return isInside(coordinates) && objectAt(coordinates).isEmpty();
    }

    @Override
    public boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }

    @Override
    public Optional<GameObject> objectAt(GridPoint2 coordinates) {
        for (GameObject object : objects) {
            if (!pendingRemoval.contains(object) && occupies(object, coordinates)) {
                return Optional.of(object);
            }
        }
        return Optional.empty();
    }

    private static boolean occupies(GameObject object, GridPoint2 coordinates) {
        return object.getCoordinates().equals(coordinates) || object.getDestination().equals(coordinates);
    }
}
