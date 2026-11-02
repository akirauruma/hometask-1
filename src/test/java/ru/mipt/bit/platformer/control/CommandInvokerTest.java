package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandInvokerTest {

    @Test
    void runsCommandsOfEveryStrategyInOrder() {
        List<String> log = new ArrayList<>();
        Command a = () -> log.add("a");
        Command b = () -> log.add("b");
        Command c = () -> log.add("c");

        new CommandInvoker()
                .add(() -> List.of(a, b))
                .add(List::of)
                .add(() -> List.of(c))
                .invokeAll();

        assertEquals(List.of("a", "b", "c"), log);
    }

    @Test
    void asksStrategiesAgainOnEveryCall() {
        List<String> log = new ArrayList<>();
        CommandInvoker invoker = new CommandInvoker().add(() -> List.of(() -> log.add("tick")));

        invoker.invokeAll();
        invoker.invokeAll();

        assertEquals(List.of("tick", "tick"), log);
    }
}
