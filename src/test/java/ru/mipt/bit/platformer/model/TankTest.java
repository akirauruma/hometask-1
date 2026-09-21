package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    @Test
    void startsAtRestFacingItsInitialDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestination());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }

    @Test
    void copiesItsStartPointSoLaterMutationsDoNotLeakIn() {
        GridPoint2 start = new GridPoint2(1, 1);
        Tank tank = new Tank(start, Direction.RIGHT);
        start.set(9, 9);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void turnChangesOrientationButNotPosition() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        tank.turn(Direction.UP);
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertFalse(tank.isMoving());
    }

    @Test
    void moveAimsAtTheNeighbourTileAndStartsMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        tank.move(Direction.UP);
        assertTrue(tank.isMoving());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
        // still on the old tile until the move finishes...
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        // ...but already heading to the tile above
        assertEquals(new GridPoint2(1, 2), tank.getDestination());
        assertEquals(0f, tank.getMovementProgress());
    }

    @Test
    void updateAdvancesProgressAndArrivesAfterOneTileTime() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        tank.move(Direction.RIGHT);

        // halfway through (speed is 0.4s per tile)
        tank.update(0.2f);
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());

        // finish the move
        tank.update(0.2f);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }
}
