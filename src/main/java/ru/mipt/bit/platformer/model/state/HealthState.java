package ru.mipt.bit.platformer.model.state;

/** Domain: a state chosen by health alone, the thresholds are shared by all health states. */
public abstract class HealthState implements TankState {

    private static final int LIGHT_ABOVE_PERCENT = 70;
    private static final int HEAVY_BELOW_PERCENT = 15;

    public static TankState forHealth(int health, int maxHealth) {
        if (health * 100 > LIGHT_ABOVE_PERCENT * maxHealth) {
            return new LightState();
        }
        if (health * 100 < HEAVY_BELOW_PERCENT * maxHealth) {
            return new HeavyState();
        }
        return new MediumState();
    }

    @Override
    public TankState onHealthChanged(int health, int maxHealth) {
        return forHealth(health, maxHealth);
    }

    @Override
    public float reloadTime(float normalTime) {
        return normalTime;
    }
}
