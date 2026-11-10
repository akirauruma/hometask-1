package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Each frame first asks every strategy for its commands and only then runs them, so all
 * strategies decide on the same state of the world. Drops the strategy of an object that
 * left the level.
 */
public class CommandInvoker implements LevelObserver {

    private final List<Controlled> controlled = new ArrayList<>();

    /** Returns {@code this} for chaining. */
    public CommandInvoker add(GameObject object, ControlStrategy strategy) {
        controlled.add(new Controlled(object, strategy));
        return this;
    }

    public void invokeAll() {
        List<Command> commands = new ArrayList<>();
        for (Controlled entry : controlled) {
            commands.addAll(entry.strategy().nextCommands());
        }
        commands.forEach(Command::execute);
    }

    @Override
    public void objectAdded(GameObject object) {
        // a strategy is attached explicitly through add()
    }

    @Override
    public void objectRemoved(GameObject object) {
        controlled.removeIf(entry -> entry.object() == object);
    }

    private record Controlled(GameObject object, ControlStrategy strategy) {
    }
}
