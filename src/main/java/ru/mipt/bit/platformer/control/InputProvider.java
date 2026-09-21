package ru.mipt.bit.platformer.control;

/**
 * Source of key presses.
 * <p>
 * Extracting this seam keeps {@link KeyboardController} free of libGDX, so the mapping
 * of keys to commands can be unit-tested with a fake provider instead of a real window.
 */
public interface InputProvider {

    boolean isPressed(int keyCode);
}
