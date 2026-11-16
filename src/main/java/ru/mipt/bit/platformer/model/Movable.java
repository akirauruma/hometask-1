package ru.mipt.bit.platformer.model;

/** Domain interface: anything that can be ordered to move one tile. */
public interface Movable {

    void move(Direction direction);
}
