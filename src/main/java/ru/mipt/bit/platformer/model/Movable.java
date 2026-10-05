package ru.mipt.bit.platformer.model;

/** Anything that can be ordered to move one tile. */
public interface Movable {

    void move(Direction direction);
}
