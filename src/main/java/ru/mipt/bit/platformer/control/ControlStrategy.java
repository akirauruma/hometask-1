package ru.mipt.bit.platformer.control;

import java.util.List;

/** Strategy deciding which commands to run this frame. */
public interface ControlStrategy {

    List<Command> nextCommands();
}
