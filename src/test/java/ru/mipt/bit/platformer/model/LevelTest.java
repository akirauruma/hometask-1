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
}
