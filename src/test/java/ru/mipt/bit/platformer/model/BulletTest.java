package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BulletTest {

    @Test
    void damagesTankAndDisappearsOnImpact() {
        Level level = new Level(5, 5);
        Tank shooter = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.4f, level);
        Tank target = new Tank(new GridPoint2(2, 1), Direction.LEFT, 0.4f, level);
        level.add(shooter);
        level.add(target);

        shooter.shoot();
        level.update(0.1f);

        assertEquals(2, target.getHealth());
    }

    @Test
    void notifiesObserversAboutTheInitialAndDynamicObjects() {
        Level level = new Level(5, 5);
        List<GameObject> added = new ArrayList<>();
        List<GameObject> removed = new ArrayList<>();
        LevelObserver observer = new LevelObserver() {
            @Override
            public void objectAdded(GameObject object) {
                added.add(object);
            }

            @Override
            public void objectRemoved(GameObject object) {
                removed.add(object);
            }
        };
        Tree tree = new Tree(new GridPoint2(1, 1));
        level.add(tree);
        level.subscribe(observer);
        level.remove(tree);
        level.update(0f);

        assertEquals(List.of(tree), added);
        assertEquals(List.of(tree), removed);
    }
}
