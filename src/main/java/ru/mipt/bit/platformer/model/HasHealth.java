package ru.mipt.bit.platformer.model;

/** An object whose health can be shown, e.g. as a health bar. */
public interface HasHealth {

    int getHealth();

    int getMaxHealth();
}
