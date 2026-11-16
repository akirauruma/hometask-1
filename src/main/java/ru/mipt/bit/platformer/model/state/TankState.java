package ru.mipt.bit.platformer.model.state;

/**
 * Domain, State pattern: how a tank drives and shoots right now. A new state, e.g. a booster,
 * is one more class that may wrap the current state; the tank itself does not change.
 */
public interface TankState {

    /** Seconds per tile, {@code normalTime} is the time of an undamaged tank. */
    float movementTime(float normalTime);

    /** Seconds between shots, {@code normalTime} is the reload of an undamaged tank. */
    float reloadTime(float normalTime);

    boolean canShoot();

    TankState onHealthChanged(int health, int maxHealth);

    /** For time-limited states such as a booster; health states do not depend on time. */
    default TankState onTimePassed(float deltaTime) {
        return this;
    }
}
