package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandInvokerTest {

    private static GameObject objectAt(int x, int y) {
        return new Tree(new GridPoint2(x, y));
    }

    @Test
    void runsCommandsOfEveryStrategyInOrder() {
        List<String> log = new ArrayList<>();
        Command a = () -> log.add("a");
        Command b = () -> log.add("b");
        Command c = () -> log.add("c");

        new CommandInvoker()
                .add(objectAt(0, 0), () -> List.of(a, b))
                .add(objectAt(1, 0), List::of)
                .add(objectAt(2, 0), () -> List.of(c))
                .invokeAll();

        assertEquals(List.of("a", "b", "c"), log);
    }

    @Test
    void asksStrategiesAgainOnEveryCall() {
        List<String> log = new ArrayList<>();
        CommandInvoker invoker = new CommandInvoker()
                .add(objectAt(0, 0), () -> List.of(() -> log.add("tick")));

        invoker.invokeAll();
        invoker.invokeAll();

        assertEquals(List.of("tick", "tick"), log);
    }

    @Test
    void everyStrategyChoosesBeforeAnyCommandRuns() {
        List<String> log = new ArrayList<>();
        ControlStrategy first = () -> {
            log.add("first chooses");
            return List.of(() -> log.add("first runs"));
        };
        ControlStrategy second = () -> {
            log.add("second chooses");
            return List.of(() -> log.add("second runs"));
        };

        new CommandInvoker()
                .add(objectAt(0, 0), first)
                .add(objectAt(1, 0), second)
                .invokeAll();

        assertEquals(List.of("first chooses", "second chooses", "first runs", "second runs"), log);
    }

    @Test
    void forgetsTheStrategyOfAnObjectThatLeftTheLevel() {
        List<String> log = new ArrayList<>();
        Level level = new Level(5, 5);
        GameObject gone = objectAt(0, 0);
        GameObject stays = objectAt(1, 0);
        level.add(gone);
        level.add(stays);
        CommandInvoker invoker = new CommandInvoker()
                .add(gone, () -> List.of(() -> log.add("gone")))
                .add(stays, () -> List.of(() -> log.add("stays")));
        level.subscribe(invoker);

        level.remove(gone);
        level.update(0f);
        invoker.invokeAll();

        assertEquals(List.of("stays"), log);
    }
}
