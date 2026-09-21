package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

/**
 * Real input backed by libGDX. Not unit-tested: it only forwards to the engine.
 */
public class GdxInputProvider implements InputProvider {

    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
