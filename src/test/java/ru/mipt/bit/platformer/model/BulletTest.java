package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BulletTest {

    private static final int HEALTH = 100;
    /** Enough frames for a bullet to cross any test field. */
    private static final int FLIGHT_FRAMES = 10;

    /** Keeps track of the bullets currently on the level. */
    private static class BulletCounter implements LevelObserver {
        private final List<GameObject> bullets = new ArrayList<>();

        @Override
        public void objectAdded(GameObject object) {
            if (object instanceof Bullet) {
                bullets.add(object);
            }
        }

        @Override
        public void objectRemoved(GameObject object) {
            bullets.remove(object);
        }

        int count() {
            return bullets.size();
        }
    }

    private static Tank tankAt(Level level, int x, int y, Direction direction) {
        Tank tank = new Tank(new GridPoint2(x, y), direction, 0.4f, HEALTH, level, level);
        level.add(tank);
        return tank;
    }

    private static void fly(Level level) {
        fly(level, FLIGHT_FRAMES);
    }

    /** One frame takes a bullet exactly one tile further. */
    private static void fly(Level level, int frames) {
        for (int frame = 0; frame < frames; frame++) {
            level.update(Bullet.MOVEMENT_SPEED);
        }
    }

    @Test
    void damagesAnAdjacentTankAndDisappears() {
        Level level = new Level(5, 5);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank shooter = tankAt(level, 1, 1, Direction.RIGHT);
        Tank target = tankAt(level, 2, 1, Direction.LEFT);

        shooter.shoot();
        level.update(Bullet.MOVEMENT_SPEED);

        assertEquals(HEALTH - Tank.BULLET_DAMAGE, target.getHealth());
        assertEquals(HEALTH, shooter.getHealth());
        assertEquals(0, bullets.count());
    }

    @Test
    void hitsATankAfterFlyingAcrossTheField() {
        Level level = new Level(5, 5);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank shooter = tankAt(level, 0, 1, Direction.RIGHT);
        Tank target = tankAt(level, 4, 1, Direction.LEFT);

        shooter.shoot();
        level.update(Bullet.MOVEMENT_SPEED);
        assertEquals(1, bullets.count());
        fly(level);

        assertEquals(HEALTH - Tank.BULLET_DAMAGE, target.getHealth());
        assertEquals(HEALTH, shooter.getHealth());
        assertEquals(0, bullets.count());
    }

    @Test
    void twoBulletsThatMeetDestroyEachOther() {
        Level level = new Level(6, 3);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank left = tankAt(level, 0, 1, Direction.RIGHT);
        Tank right = tankAt(level, 5, 1, Direction.LEFT);

        left.shoot();
        right.shoot();
        // on (1,1) and (4,1), then (2,1) and (3,1), then the left one runs into the right one
        fly(level, 2);
        assertEquals(2, bullets.count());
        fly(level, 1);
        assertEquals(0, bullets.count());
        fly(level);

        assertEquals(0, bullets.count());
        assertEquals(HEALTH, left.getHealth());
        assertEquals(HEALTH, right.getHealth());
    }

    @Test
    void bulletsMeetingOnTheSameTileDestroyEachOther() {
        Level level = new Level(5, 3);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank left = tankAt(level, 0, 1, Direction.RIGHT);
        Tank right = tankAt(level, 4, 1, Direction.LEFT);

        left.shoot();
        right.shoot();
        // on (1,1) and (3,1), then the left one enters (2,1) and the right one runs into it
        fly(level, 1);
        assertEquals(2, bullets.count());
        fly(level, 1);
        assertEquals(0, bullets.count());
        fly(level);

        assertEquals(0, bullets.count());
        assertEquals(HEALTH, left.getHealth());
        assertEquals(HEALTH, right.getHealth());
    }

    @Test
    void disappearsAtTheBorder() {
        Level level = new Level(5, 5);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank shooter = tankAt(level, 3, 1, Direction.RIGHT);

        shooter.shoot();
        fly(level);

        assertEquals(0, bullets.count());
        assertEquals(HEALTH, shooter.getHealth());
    }

    @Test
    void stopsAtATreeAndLeavesItStanding() {
        Level level = new Level(5, 5);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank shooter = tankAt(level, 0, 1, Direction.RIGHT);
        Tree tree = new Tree(new GridPoint2(3, 1));
        level.add(tree);

        shooter.shoot();
        fly(level);

        assertEquals(0, bullets.count());
        assertEquals(Optional.of(tree), level.objectAt(new GridPoint2(3, 1)));
    }

    @Test
    void tankShootingWhileMovingIsNotHitByItsOwnBullet() {
        Level level = new Level(5, 5);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank shooter = tankAt(level, 1, 1, Direction.RIGHT);

        shooter.move(Direction.RIGHT);
        shooter.shoot();
        fly(level);

        assertEquals(HEALTH, shooter.getHealth());
        assertEquals(0, bullets.count());
    }

    @Test
    void destroyedTankLeavesTheLevelAndCannotShootAnymore() {
        Level level = new Level(5, 5);
        BulletCounter bullets = new BulletCounter();
        level.subscribe(bullets);
        Tank shooter = tankAt(level, 0, 1, Direction.RIGHT);
        Tank target = tankAt(level, 3, 1, Direction.LEFT);

        int hitsToDestroy = (HEALTH + Tank.BULLET_DAMAGE - 1) / Tank.BULLET_DAMAGE;
        for (int shot = 0; shot < hitsToDestroy; shot++) {
            shooter.shoot();
            fly(level);
        }
        target.shoot();
        fly(level);

        assertFalse(target.isAlive());
        assertEquals(Optional.empty(), level.objectAt(new GridPoint2(3, 1)));
        assertEquals(HEALTH, shooter.getHealth());
        assertEquals(0, bullets.count());
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
