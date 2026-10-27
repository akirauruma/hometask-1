package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.List;

/** Runs the commands of every strategy, in the order strategies were added. */
public class CommandInvoker {

    private final List<ControlStrategy> strategies = new ArrayList<>();

    /** Returns {@code this} for chaining. */
    public CommandInvoker add(ControlStrategy strategy) {
        strategies.add(strategy);
        return this;
    }

    public void invokeAll() {
        for (ControlStrategy strategy : strategies) {
            for (Command command : strategy.nextCommands()) {
                command.execute();
            }
        }
    }
}
