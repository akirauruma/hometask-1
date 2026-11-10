package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

/** Anything that draws itself into the batch. */
public interface Renderable {

    void render(Batch batch);

    /** Where it was drawn last time, in pixels; decorators draw relative to it. */
    Rectangle getBounds();
}
