package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static class RecordingObject implements GameObject {
        private final List<Float> updates = new ArrayList<>();

        @Override
        public GridPoint2 getCoordinates() {
            return new GridPoint2(0, 0);
        }

        @Override
        public void update(float deltaTime) {
            updates.add(deltaTime);
        }
    }

    @Test
    void emptyTileInsideTheFieldIsFree() {
        Level level = new Level(5, 5);
        assertTrue(level.isFree(new GridPoint2(3, 3)));
    }

    @Test
    void tilesOutsideTheFieldAreNotFree() {
        Level level = new Level(5, 5);
        assertFalse(level.isFree(new GridPoint2(-1, 0)));
        assertFalse(level.isFree(new GridPoint2(0, -1)));
        assertFalse(level.isFree(new GridPoint2(5, 0)));
        assertFalse(level.isFree(new GridPoint2(0, 5)));
    }

    @Test
    void tileWithAnyObjectIsNotFree() {
        Level level = new Level(5, 5);
        level.add(new Tree(new GridPoint2(2, 2)));
        level.add(() -> new GridPoint2(4, 4));
        assertFalse(level.isFree(new GridPoint2(2, 2)));
        assertFalse(level.isFree(new GridPoint2(4, 4)));
    }

    @Test
    void destinationOfAMovingObjectIsNotFree() {
        Level level = new Level(5, 5);
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.4f, level);
        level.add(tank);

        tank.move(Direction.UP);

        assertFalse(level.isFree(new GridPoint2(1, 1)));
        assertFalse(level.isFree(new GridPoint2(1, 2)));
    }

    @Test
    void tankOnTheLevelOnlyTurnsInFrontOfATree() {
        Level level = new Level(5, 5);
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.4f, level);
        level.add(tank);
        level.add(new Tree(new GridPoint2(1, 2)));

        tank.move(Direction.UP);

        assertFalse(tank.isMoving());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void updateIsPassedToEveryObject() {
        Level level = new Level(5, 5);
        RecordingObject first = new RecordingObject();
        RecordingObject second = new RecordingObject();
        level.add(first);
        level.add(second);

        level.update(0.1f);

        assertEquals(List.of(0.1f), first.updates);
        assertEquals(List.of(0.1f), second.updates);
    }

    private static Tank tankAt(int x, int y, Level level) {
        Tank tank = new Tank(new GridPoint2(x, y), Direction.RIGHT, 0.4f, level);
        level.add(tank);
        return tank;
    }

    @Test
    void movingTankBlocksBothItsTilesForOtherTanks() {
        Level level = new Level(5, 5);
        Tank mover = tankAt(1, 1, level);
        Tank behind = tankAt(0, 1, level);
        Tank above = tankAt(2, 2, level);

        // (1,1) -> (2,1)
        mover.move(Direction.RIGHT);
        behind.move(Direction.RIGHT);
        above.move(Direction.DOWN);

        assertTrue(mover.isMoving());
        assertFalse(behind.isMoving());
        assertFalse(above.isMoving());
    }

    @Test
    void tileIsFreedWhenTheTankArrives() {
        Level level = new Level(5, 5);
        Tank tank = tankAt(1, 1, level);

        tank.move(Direction.RIGHT);
        level.update(1f);

        assertFalse(tank.isMoving());
        assertTrue(level.isFree(new GridPoint2(1, 1)));
        assertFalse(level.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void twoTanksCannotDriveIntoTheSameTile() {
        Level level = new Level(5, 5);
        Tank left = tankAt(0, 1, level);
        Tank right = tankAt(2, 1, level);

        left.move(Direction.RIGHT);
        right.move(Direction.LEFT);

        assertTrue(left.isMoving());
        assertFalse(right.isMoving());
    }

    @Test
    void tankStopsAtTheLevelBorders() {
        Level level = new Level(5, 5);
        Tank bottomLeft = tankAt(0, 0, level);
        Tank topRight = tankAt(4, 4, level);

        for (Direction direction : new Direction[]{Direction.LEFT, Direction.DOWN}) {
            bottomLeft.move(direction);
            assertFalse(bottomLeft.isMoving(), direction.toString());
            assertEquals(direction.getRotation(), bottomLeft.getRotation());
        }
        for (Direction direction : new Direction[]{Direction.RIGHT, Direction.UP}) {
            topRight.move(direction);
            assertFalse(topRight.isMoving(), direction.toString());
        }
    }
}
