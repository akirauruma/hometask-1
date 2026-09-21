package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private Tank tankAt(int x, int y) {
        return new Tank(new GridPoint2(x, y), Direction.RIGHT);
    }

    @Test
    void moveIntoAFreeTileStartsTheMove() {
        Tank tank = tankAt(1, 1);
        Level level = new Level(5, 5, tank, Collections.emptyList());

        level.movePlayerTank(Direction.RIGHT);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestination());
    }

    @Test
    void moveIntoATreeOnlyTurnsTheTank() {
        Tank tank = tankAt(1, 1);
        Level level = new Level(5, 5, tank, Collections.singletonList(new Tree(new GridPoint2(1, 2))));

        level.movePlayerTank(Direction.UP);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void moveOutsideTheFieldOnlyTurnsTheTank() {
        Tank tank = tankAt(0, 1);
        Level level = new Level(5, 5, tank, Collections.emptyList());

        level.movePlayerTank(Direction.LEFT);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation());
    }

    @Test
    void aStartedMoveIsNotInterruptedByNewInput() {
        Tank tank = tankAt(1, 1);
        Level level = new Level(5, 5, tank, Collections.emptyList());

        level.movePlayerTank(Direction.RIGHT); // starts moving right
        level.movePlayerTank(Direction.UP);    // ignored while moving

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestination());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }

    @Test
    void isFreeIsTrueInsideAndFalseOnTreesOrOutside() {
        Level level = new Level(5, 5, tankAt(0, 0), Collections.singletonList(new Tree(new GridPoint2(2, 2))));

        assertTrue(level.isFree(new GridPoint2(3, 3)));
        assertFalse(level.isFree(new GridPoint2(2, 2)));  // tree
        assertFalse(level.isFree(new GridPoint2(-1, 0))); // outside
        assertFalse(level.isFree(new GridPoint2(5, 0)));  // outside (width is 5)
    }

    @Test
    void exposedTreeListCannotBeModifiedFromOutside() {
        Level level = new Level(5, 5, tankAt(0, 0), Collections.singletonList(new Tree(new GridPoint2(2, 2))));
        List<Tree> trees = level.getTrees();
        assertThrows(UnsupportedOperationException.class, () -> trees.add(new Tree(new GridPoint2(3, 3))));
    }
}
