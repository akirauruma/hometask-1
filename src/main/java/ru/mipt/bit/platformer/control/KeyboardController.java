package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Player control. A command bound with {@link #bind} runs every frame while its key is held,
 * one bound with {@link #bindOnPress} runs once per key press.
 */
public class KeyboardController implements ControlStrategy {

    private final InputProvider input;
    private final Map<Integer, Command> whileHeld = new LinkedHashMap<>();
    private final Map<Integer, Command> onPress = new LinkedHashMap<>();

    public KeyboardController(InputProvider input) {
        this.input = input;
    }

    /** Returns {@code this} for chaining. */
    public KeyboardController bind(int keyCode, Command command) {
        whileHeld.put(keyCode, command);
        return this;
    }

    /** Returns {@code this} for chaining. */
    public KeyboardController bindOnPress(int keyCode, Command command) {
        onPress.put(keyCode, command);
        return this;
    }

    @Override
    public List<Command> nextCommands() {
        List<Command> commands = new ArrayList<>();
        for (Map.Entry<Integer, Command> binding : whileHeld.entrySet()) {
            if (input.isPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        for (Map.Entry<Integer, Command> binding : onPress.entrySet()) {
            if (input.isJustPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        return commands;
    }
}
