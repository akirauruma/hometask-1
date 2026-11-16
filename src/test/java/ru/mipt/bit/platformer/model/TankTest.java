package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float SPEED = 0.4f;
    private static final CollisionChecker EVERYTHING_FREE = coordinates -> true;
    private static final CollisionChecker EVERYTHING_BLOCKED = coordinates -> false;

    /** Records what the tank adds and removes; the field itself is empty and endless. */
    private static class RecordingBattlefield implements Battlefield {
        private final List<GameObject> added = new ArrayList<>();
        private final List<GameObject> removed = new ArrayList<>();

        @Override
        public boolean isInside(GridPoint2 coordinates) {
            return true;
        }

        @Override
        public Optional<GameObject> objectAt(GridPoint2 coordinates) {
            return Optional.empty();
        }

        @Override
        public void add(GameObject object) {
            added.add(object);
        }

        @Override
        public void remove(GameObject object) {
            removed.add(object);
        }
    }

    private Tank tankAt(int x, int y, CollisionChecker collisionChecker) {
        return new Tank(new GridPoint2(x, y), Direction.RIGHT, SPEED, collisionChecker, new RecordingBattlefield());
    }

    private Tank tankOn(int x, int y, Battlefield battlefield) {
        return new Tank(new GridPoint2(x, y), Direction.RIGHT, SPEED, EVERYTHING_FREE, battlefield);
    }

    @Test
    void startsAtRestFacingItsInitialDirection() {
        Tank tank = tankAt(1, 1, EVERYTHING_FREE);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestination());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }

    @Test
    void copiesItsStartPointSoLaterMutationsDoNotLeakIn() {
        GridPoint2 start = new GridPoint2(1, 1);
        Tank tank = new Tank(start, Direction.RIGHT, SPEED, EVERYTHING_FREE, new RecordingBattlefield());
        start.set(9, 9);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void moveIntoAFreeTileStartsTheMove() {
        Tank tank = tankAt(1, 1, EVERYTHING_FREE);
        tank.move(Direction.UP);
        assertTrue(tank.isMoving());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestination());
        assertEquals(0f, tank.getMovementProgress());
    }

    @Test
    void moveIntoABlockedTileOnlyTurnsTheTank() {
        Tank tank = tankAt(1, 1, EVERYTHING_BLOCKED);
        tank.move(Direction.UP);
        assertFalse(tank.isMoving());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
        assertEquals(new GridPoint2(1, 1), tank.getDestination());
    }

    @Test
    void asksTheCollisionCheckerAboutTheNeighbourTile() {
        List<GridPoint2> asked = new ArrayList<>();
        Tank tank = tankAt(1, 1, coordinates -> asked.add(new GridPoint2(coordinates)));
        tank.move(Direction.LEFT);
        assertEquals(List.of(new GridPoint2(0, 1)), asked);
    }

    @Test
    void aStartedMoveIsNotInterruptedByNewInput() {
        Tank tank = tankAt(1, 1, EVERYTHING_FREE);
        tank.move(Direction.RIGHT);
        tank.move(Direction.UP);
        assertEquals(new GridPoint2(2, 1), tank.getDestination());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }

    @Test
    void updateAdvancesProgressAndArrivesAfterOneTileTime() {
        Tank tank = tankAt(1, 1, EVERYTHING_FREE);
        tank.move(Direction.RIGHT);

        tank.update(SPEED / 2);
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());

        tank.update(SPEED / 2);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    void movementSpeedIsAParameter() {
        Tank slowTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 2f, EVERYTHING_FREE,
                new RecordingBattlefield());
        slowTank.move(Direction.RIGHT);
        slowTank.update(SPEED);
        assertTrue(slowTank.isMoving());
        assertEquals(0.2f, slowTank.getMovementProgress(), 1e-6f);
    }

    @Test
    void shootPutsABulletOnTheTanksTileFlyingTheSameWay() {
        RecordingBattlefield battlefield = new RecordingBattlefield();
        Tank tank = tankOn(1, 1, battlefield);

        tank.shoot();

        assertEquals(1, battlefield.added.size());
        GameObject bullet = battlefield.added.get(0);
        assertTrue(bullet instanceof Bullet);
        assertEquals(new GridPoint2(1, 1), bullet.getCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), bullet.getRotation());
    }

    @Test
    void movingTankShootsFromTheTileItDrivesInto() {
        RecordingBattlefield battlefield = new RecordingBattlefield();
        Tank tank = tankOn(1, 1, battlefield);

        tank.move(Direction.UP);
        tank.shoot();

        assertEquals(new GridPoint2(1, 2), battlefield.added.get(0).getCoordinates());
    }

    @Test
    void shootsAgainOnlyAfterReloading() {
        RecordingBattlefield battlefield = new RecordingBattlefield();
        Tank tank = tankOn(1, 1, battlefield);

        tank.shoot();
        tank.shoot();
        assertEquals(1, battlefield.added.size());

        tank.update(Tank.RELOAD_TIME / 2);
        tank.shoot();
        assertEquals(1, battlefield.added.size());

        tank.update(Tank.RELOAD_TIME / 2);
        tank.shoot();
        assertEquals(2, battlefield.added.size());
    }

    @Test
    void leavesTheBattlefieldOnceWhenHealthRunsOut() {
        RecordingBattlefield battlefield = new RecordingBattlefield();
        Tank tank = tankOn(1, 1, battlefield);

        for (int hit = 1; hit < Tank.INITIAL_HEALTH; hit++) {
            tank.takeDamage(1);
        }
        assertTrue(tank.isAlive());
        assertEquals(List.of(), battlefield.removed);

        tank.takeDamage(1);
        tank.takeDamage(1);
        assertFalse(tank.isAlive());
        assertEquals(List.of(tank), battlefield.removed);
    }

    @Test
    void destroyedTankNeitherShootsNorMovesNorTurns() {
        RecordingBattlefield battlefield = new RecordingBattlefield();
        Tank tank = tankOn(1, 1, battlefield);
        tank.takeDamage(Tank.INITIAL_HEALTH);

        tank.shoot();
        tank.move(Direction.UP);

        assertEquals(List.of(), battlefield.added);
        assertFalse(tank.isMoving());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }
}
