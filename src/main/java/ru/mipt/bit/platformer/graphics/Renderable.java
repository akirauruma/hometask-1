package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

/** Anything that draws itself into the batch. */
public interface Renderable {

    void render(Batch batch);
}
