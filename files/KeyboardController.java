package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.model.Direction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

/**
 * Turns pressed keys into a direction.
 * <p>
 * Adding a key binding is now one line in the table instead of another copy-pasted
 * {@code if (Gdx.input.isKeyPressed(...))} block. The order of the entries is the priority
 * when several keys are held at once.
 */
public class KeyboardController {

    private final Map<Integer, Direction> keyBindings = new LinkedHashMap<>();

    public KeyboardController() {
        keyBindings.put(UP, Direction.UP);
        keyBindings.put(W, Direction.UP);
        keyBindings.put(LEFT, Direction.LEFT);
        keyBindings.put(A, Direction.LEFT);
        keyBindings.put(DOWN, Direction.DOWN);
        keyBindings.put(S, Direction.DOWN);
        keyBindings.put(RIGHT, Direction.RIGHT);
        keyBindings.put(D, Direction.RIGHT);
    }

    /** The direction of the first pressed key, or empty if nothing is pressed. */
    public Optional<Direction> getPressedDirection() {
        for (Map.Entry<Integer, Direction> binding : keyBindings.entrySet()) {
            if (Gdx.input.isKeyPressed(binding.getKey())) {
                return Optional.of(binding.getValue());
            }
        }
        return Optional.empty();
    }
}
