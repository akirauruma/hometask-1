package ru.mipt.bit.platformer.model.state;

/** Domain: less than 15% of health, drives three times slower and cannot shoot. */
public class HeavyState extends HealthState {

    private static final float SLOWDOWN = 3f;

    @Override
    public float movementTime(float normalTime) {
        return normalTime * SLOWDOWN;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
