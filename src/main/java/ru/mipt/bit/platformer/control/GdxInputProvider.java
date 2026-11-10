package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

/** {@link InputProvider} backed by libGDX. */
public class GdxInputProvider implements InputProvider {

    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
