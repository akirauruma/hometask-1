package ru.mipt.bit.platformer.control;

/**
 * A single user action, decoupled both from the key that triggers it and from libGDX.
 * <p>
 * New handlers are added by implementing this interface (e.g. a future {@code FireCommand}
 * bound to SPACE) and binding it in {@link KeyboardController} — no existing code changes.
 */
public interface Command {

    void execute();
}
