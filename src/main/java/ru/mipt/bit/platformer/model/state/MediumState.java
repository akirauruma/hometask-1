package ru.mipt.bit.platformer.model.state;

/** Domain: 15–70% of health, drives twice slower. */
public class MediumState extends HealthState {

    private static final float SLOWDOWN = 2f;

    @Override
    public float movementTime(float normalTime) {
        return normalTime * SLOWDOWN;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
