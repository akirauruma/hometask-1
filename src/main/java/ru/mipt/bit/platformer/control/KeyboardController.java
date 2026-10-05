package ru.mipt.bit.platformer.control;

import java.util.LinkedHashMap;
import java.util.Map;

/** Runs commands bound to pressed keys, in binding order. */
public class KeyboardController {

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

    public void processInput() {
        for (Map.Entry<Integer, Command> binding : bindings.entrySet()) {
            if (input.isPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
