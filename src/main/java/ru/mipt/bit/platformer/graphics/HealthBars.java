package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.control.Toggleable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.HasHealth;

/**
 * One switch for the health bars of every object with health. Wraps their renderables into
 * {@link HealthBarDecorator}s, which draw a bar only while the bars are shown. Hidden at start.
 */
public class HealthBars implements Decoration, Toggleable, Disposable {

    private final Texture pixel;
    private boolean shown;

    /** Takes ownership of {@code pixel}: a white texture the bars are painted with. */
    public HealthBars(Texture pixel) {
        this.pixel = pixel;
    }

    @Override
    public Renderable decorate(GameObject object, Renderable renderable) {
        if (object instanceof HasHealth health) {
            return new HealthBarDecorator(renderable, health, this);
        }
        return renderable;
    }

    @Override
    public void toggle() {
        shown = !shown;
    }

    public boolean isShown() {
        return shown;
    }

    Texture getPixel() {
        return pixel;
    }

    @Override
    public void dispose() {
        pixel.dispose();
    }
}
