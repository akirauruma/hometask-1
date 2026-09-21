package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyboardControllerTest {

    /** Fake input: only the keys we say are "pressed" report as pressed. */
    private static class FakeInput implements InputProvider {
        private final Set<Integer> pressed = new HashSet<>();

        FakeInput press(int... keys) {
            for (int key : keys) {
                pressed.add(key);
            }
            return this;
        }

        @Override
        public boolean isPressed(int keyCode) {
            return pressed.contains(keyCode);
        }
    }

    /** Records that it ran, in call order. */
    private static class RecordingCommand implements Command {
        private final String name;
        private final List<String> log;

        RecordingCommand(String name, List<String> log) {
            this.name = name;
            this.log = log;
        }

        @Override
        public void execute() {
            log.add(name);
        }
    }

    @Test
    void runsOnlyTheCommandsWhoseKeyIsPressed() {
        List<String> log = new ArrayList<>();
        KeyboardController controller = new KeyboardController(new FakeInput().press(2))
                .bind(1, new RecordingCommand("a", log))
                .bind(2, new RecordingCommand("b", log))
                .bind(3, new RecordingCommand("c", log));

        controller.processInput();

        assertEquals(List.of("b"), log);
    }

    @Test
    void nothingRunsWhenNoBoundKeyIsPressed() {
        List<String> log = new ArrayList<>();
        KeyboardController controller = new KeyboardController(new FakeInput())
                .bind(1, new RecordingCommand("a", log));

        controller.processInput();

        assertEquals(List.of(), log);
    }

    @Test
    void severalPressedCommandsRunInBindingOrder() {
        List<String> log = new ArrayList<>();
        KeyboardController controller = new KeyboardController(new FakeInput().press(1, 2))
                .bind(1, new RecordingCommand("first", log))
                .bind(2, new RecordingCommand("second", log));

        controller.processInput();

        assertEquals(List.of("first", "second"), log);
    }
}
