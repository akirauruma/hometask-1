package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovePlayerCommandTest {

    @Test
    void executeAsksTheLevelToMoveThePlayerTankInItsDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        Level level = new Level(5, 5, tank, Collections.emptyList());

        new MovePlayerCommand(level, Direction.UP).execute();

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getDestination());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }
}
