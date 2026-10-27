package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.List;
import java.util.Random;

/** Simplest AI: every frame orders a move in a random direction. */
public class RandomController implements ControlStrategy {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Movable movable;
    private final Random random;

    public RandomController(Movable movable, Random random) {
        this.movable = movable;
        this.random = random;
    }

    @Override
    public List<Command> nextCommands() {
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return List.of(new MoveCommand(movable, direction));
    }
}
