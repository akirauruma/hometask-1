package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToggleCommandTest {

    @Test
    void togglesItsReceiverOnEveryExecution() {
        List<String> log = new ArrayList<>();
        ToggleCommand command = new ToggleCommand(() -> log.add("toggled"));

        command.execute();
        command.execute();

        assertEquals(List.of("toggled", "toggled"), log);
    }
}
