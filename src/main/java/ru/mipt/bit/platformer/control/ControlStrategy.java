package ru.mipt.bit.platformer.control;

import java.util.List;

/** Application input port, Strategy: decides which commands to run this frame. */
public interface ControlStrategy {

    List<Command> nextCommands();
}
