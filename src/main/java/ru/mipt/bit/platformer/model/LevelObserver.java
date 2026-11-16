package ru.mipt.bit.platformer.model;

/** Domain output port: told when an object joins or leaves the level. */
public interface LevelObserver {

    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
