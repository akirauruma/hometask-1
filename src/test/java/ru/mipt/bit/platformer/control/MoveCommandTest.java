package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCommandTest {

    @Test
    void executeMovesTheObjectInTheCommandDirection() {
        List<Direction> moves = new ArrayList<>();

        new MoveCommand(moves::add, Direction.UP).execute();

        assertEquals(List.of(Direction.UP), moves);
    }
}
