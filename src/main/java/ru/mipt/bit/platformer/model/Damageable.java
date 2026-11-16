package ru.mipt.bit.platformer.model;

/** Domain interface: something a bullet hurts when it runs into it. */
public interface Damageable {

    void takeDamage(int damage);
}
