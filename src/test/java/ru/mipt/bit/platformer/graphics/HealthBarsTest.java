package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** No GL here: the pixel texture is never touched while the bars are hidden. */
class HealthBarsTest {

    /** Records how often it was drawn. */
    private static class RecordingRenderable implements Renderable {
        private final List<Batch> renders = new ArrayList<>();
        private final Rectangle bounds = new Rectangle(10, 20, 92, 84);

        @Override
        public void render(Batch batch) {
            renders.add(batch);
        }

        @Override
        public Rectangle getBounds() {
            return bounds;
        }
    }

    private static Tank tank() {
        Level level = new Level(5, 5);
        return new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.4f, 100, level, level);
    }

    @Test
    void hiddenAtStartAndToggledEachTime() {
        HealthBars healthBars = new HealthBars(null);
        assertFalse(healthBars.isShown());

        healthBars.toggle();
        assertTrue(healthBars.isShown());

        healthBars.toggle();
        assertFalse(healthBars.isShown());
    }

    @Test
    void wrapsTheRenderableOfAnObjectWithHealth() {
        HealthBars healthBars = new HealthBars(null);
        RecordingRenderable graphics = new RecordingRenderable();

        Renderable decorated = healthBars.decorate(tank(), graphics);

        assertInstanceOf(HealthBarDecorator.class, decorated);
        assertSame(graphics.getBounds(), decorated.getBounds());
    }

    @Test
    void leavesObjectsWithoutHealthAsTheyAre() {
        HealthBars healthBars = new HealthBars(null);
        RecordingRenderable graphics = new RecordingRenderable();

        assertSame(graphics, healthBars.decorate(new Tree(new GridPoint2(1, 1)), graphics));
    }

    @Test
    void decoratorAlwaysDrawsTheWrappedRenderable() {
        HealthBars healthBars = new HealthBars(null);
        RecordingRenderable graphics = new RecordingRenderable();
        Renderable decorated = healthBars.decorate(tank(), graphics);

        decorated.render(null);
        decorated.render(null);

        assertEquals(2, graphics.renders.size());
    }
}
