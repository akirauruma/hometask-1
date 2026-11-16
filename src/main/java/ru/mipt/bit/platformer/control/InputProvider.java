package ru.mipt.bit.platformer.control;

/** Source of key presses, lets {@link KeyboardController} be tested without libGDX. */
public interface InputProvider {

    boolean isPressed(int keyCode);
}
