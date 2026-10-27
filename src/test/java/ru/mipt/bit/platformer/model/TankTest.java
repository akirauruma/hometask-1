package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float SPEED = 0.4f;
    private static final CollisionChecker EVERYTHING_FREE = coordinates -> true;
    private static final CollisionChecker EVERYTHING_BLOCKED = coordinates -> false;

    private Tank tankAt(int x, int y, CollisionChecker collisionChecker) {
        return new Tank(new GridPoint2(x, y), Direction.RIGHT, SPEED, collisionChecker);
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
        Tank tank = new Tank(start, Direction.RIGHT, SPEED, EVERYTHING_FREE);
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
        Tank slowTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 2f, EVERYTHING_FREE);
        slowTank.move(Direction.RIGHT);
        slowTank.update(SPEED);
        assertTrue(slowTank.isMoving());
        assertEquals(0.2f, slowTank.getMovementProgress(), 1e-6f);
    }
}
