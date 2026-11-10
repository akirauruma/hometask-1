package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.HasHealth;

/** Decorator: draws the wrapped renderable and, while health bars are shown, a health bar above it. */
public class HealthBarDecorator implements Renderable {

    private static final float BAR_HEIGHT = 8f;
    /** Between the top of the sprite and the bar. */
    private static final float GAP = 6f;

    private final Renderable wrapped;
    private final HasHealth health;
    private final HealthBars healthBars;

    public HealthBarDecorator(Renderable wrapped, HasHealth health, HealthBars healthBars) {
        this.wrapped = wrapped;
        this.health = health;
        this.healthBars = healthBars;
    }

    @Override
    public void render(Batch batch) {
        wrapped.render(batch);
        if (healthBars.isShown()) {
            drawBar(batch, wrapped.getBounds());
        }
    }

    private void drawBar(Batch batch, Rectangle bounds) {
        float x = bounds.x;
        float y = bounds.y + bounds.height + GAP;
        float healthWidth = bounds.width * health.getHealth() / health.getMaxHealth();
        Color previousColor = batch.getColor().cpy();

        batch.setColor(Color.RED);
        batch.draw(healthBars.getPixel(), x, y, bounds.width, BAR_HEIGHT);
        batch.setColor(Color.GREEN);
        batch.draw(healthBars.getPixel(), x, y, healthWidth, BAR_HEIGHT);

        batch.setColor(previousColor);
    }

    @Override
    public Rectangle getBounds() {
        return wrapped.getBounds();
    }
}
