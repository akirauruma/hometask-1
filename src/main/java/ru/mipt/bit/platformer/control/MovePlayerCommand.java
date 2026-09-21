package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;

/**
 * Moves the player tank one tile in a fixed direction.
 * The level decides whether the move actually happens or the tank only turns.
 */
public class MovePlayerCommand implements Command {

    private final Level level;
    private final Direction direction;

    public MovePlayerCommand(Level level, Direction direction) {
        this.level = level;
        this.direction = direction;
    }

    @Override
    public void execute() {
        level.movePlayerTank(direction);
    }
}
