package ru.mipt.bit.platformer.control;

/** A user action, independent of keys and libGDX. */
public interface Command {

    void execute();
}
