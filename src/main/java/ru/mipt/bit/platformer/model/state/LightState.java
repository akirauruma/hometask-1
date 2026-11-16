package ru.mipt.bit.platformer.model.state;

/** Domain: more than 70% of health, behaves as undamaged. */
public class LightState extends HealthState {

    @Override
    public float movementTime(float normalTime) {
        return normalTime;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
