package ru.mipt.bit.platformer.control;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps keys to {@link Command}s and runs the ones whose key is pressed.
 * <p>
 * Adding a new handler (for example {@code SPACE -> fire}) is a single {@link #bind} call:
 * no new {@code if} branch, and neither this class nor the game loop has to change.
 * The order in which keys are bound is the priority when several are held at once.
 */
public class KeyboardController {

    private final InputProvider input;
    private final Map<Integer, Command> bindings = new LinkedHashMap<>();

    public KeyboardController(InputProvider input) {
        this.input = input;
    }

    /** Bind a key code to a command. Returns {@code this} so bindings can be chained. */
    public KeyboardController bind(int keyCode, Command command) {
        bindings.put(keyCode, command);
        return this;
    }

    /** Execute every bound command whose key is currently pressed, in binding order. */
    public void processInput() {
        for (Map.Entry<Integer, Command> binding : bindings.entrySet()) {
            if (input.isPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
