package ru.mipt.bit.platformer.model;

/** Something a bullet hurts when it runs into it. */
public interface Damageable {

    void takeDamage(int damage);
}
