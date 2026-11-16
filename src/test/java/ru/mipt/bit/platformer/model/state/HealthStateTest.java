package ru.mipt.bit.platformer.model.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthStateTest {

    @Test
    void moreThanSeventyPercentIsLight() {
        assertTrue(HealthState.forHealth(100, 100) instanceof LightState);
        assertTrue(HealthState.forHealth(71, 100) instanceof LightState);
        assertTrue(HealthState.forHealth(57, 80) instanceof LightState);
    }

    @Test
    void fromFifteenToSeventyPercentIsMedium() {
        assertTrue(HealthState.forHealth(70, 100) instanceof MediumState);
        assertTrue(HealthState.forHealth(56, 80) instanceof MediumState);
        assertTrue(HealthState.forHealth(15, 100) instanceof MediumState);
        assertTrue(HealthState.forHealth(12, 80) instanceof MediumState);
    }

    @Test
    void lessThanFifteenPercentIsHeavy() {
        assertTrue(HealthState.forHealth(14, 100) instanceof HeavyState);
        assertTrue(HealthState.forHealth(11, 80) instanceof HeavyState);
        assertTrue(HealthState.forHealth(0, 100) instanceof HeavyState);
    }

    @Test
    void lightBehavesAsUndamaged() {
        TankState state = new LightState();
        assertEquals(0.4f, state.movementTime(0.4f));
        assertEquals(0.5f, state.reloadTime(0.5f));
        assertTrue(state.canShoot());
    }

    @Test
    void mediumDrivesTwiceSlowerAndShoots() {
        TankState state = new MediumState();
        assertEquals(0.8f, state.movementTime(0.4f));
        assertEquals(0.5f, state.reloadTime(0.5f));
        assertTrue(state.canShoot());
    }

    @Test
    void heavyDrivesThreeTimesSlowerAndCannotShoot() {
        TankState state = new HeavyState();
        assertEquals(1.2f, state.movementTime(0.4f), 1e-6f);
        assertFalse(state.canShoot());
    }

    @Test
    void healthChangeMovesBetweenStatesBothWays() {
        TankState state = new LightState().onHealthChanged(50, 100);
        assertTrue(state instanceof MediumState);

        state = state.onHealthChanged(10, 100);
        assertTrue(state instanceof HeavyState);

        state = state.onHealthChanged(90, 100);
        assertTrue(state instanceof LightState);
    }

    @Test
    void healthStatesDoNotDependOnTime() {
        TankState state = new MediumState();
        assertSame(state, state.onTimePassed(100f));
    }
}
