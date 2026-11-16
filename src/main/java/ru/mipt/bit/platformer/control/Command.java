package ru.mipt.bit.platformer.control;

/** Application input port: a user action, independent of keys and libGDX. */
public interface Command {

    void execute();
}
