package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomControllerTest {

    @Test
    void givesOneMoveCommandForItsTank() {
        List<Direction> moves = new ArrayList<>();
        RandomController controller = new RandomController(moves::add, new Random(42));

        List<Command> commands = controller.nextCommands();
        commands.forEach(Command::execute);

        assertEquals(1, commands.size());
        assertEquals(1, moves.size());
    }

    @Test
    void soonerOrLaterTriesEveryDirection() {
        Set<Direction> seen = EnumSet.noneOf(Direction.class);
        RandomController controller = new RandomController(seen::add, new Random(42));

        for (int i = 0; i < 100; i++) {
            controller.nextCommands().forEach(Command::execute);
        }

        assertEquals(EnumSet.allOf(Direction.class), seen);
    }

    @Test
    void sameSeedGivesSameMoves() {
        List<Direction> first = new ArrayList<>();
        List<Direction> second = new ArrayList<>();
        RandomController a = new RandomController(first::add, new Random(7));
        RandomController b = new RandomController(second::add, new Random(7));

        for (int i = 0; i < 10; i++) {
            a.nextCommands().forEach(Command::execute);
            b.nextCommands().forEach(Command::execute);
        }

        assertEquals(first, second);
    }
}
