package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Player control: commands bound to pressed keys, in binding order. */
public class KeyboardController implements ControlStrategy {

    private final InputProvider input;
    private final Map<Integer, Command> bindings = new LinkedHashMap<>();

    public KeyboardController(InputProvider input) {
        this.input = input;
    }

    /** Returns {@code this} for chaining. */
    public KeyboardController bind(int keyCode, Command command) {
        bindings.put(keyCode, command);
        return this;
    }

    @Override
    public List<Command> nextCommands() {
        List<Command> commands = new ArrayList<>();
        for (Map.Entry<Integer, Command> binding : bindings.entrySet()) {
            if (input.isPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        return commands;
    }
}
