package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

/** Wraps the renderable of an object into a decorator, or returns it as it is. */
public interface Decoration {

    Renderable decorate(GameObject object, Renderable renderable);
}
