package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

import java.util.HashMap;
import java.util.Map;

/** Which texture draws which object class. A class without a texture is an error, not a default sprite. */
public class TextureBindings {

    private final Map<Class<? extends GameObject>, String> texturePaths = new HashMap<>();

    /** Returns {@code this} for chaining. */
    public TextureBindings bind(Class<? extends GameObject> type, String texturePath) {
        texturePaths.put(type, texturePath);
        return this;
    }

    public String texturePathFor(GameObject object) {
        String texturePath = texturePaths.get(object.getClass());
        if (texturePath == null) {
            throw new IllegalArgumentException("No texture bound for " + object.getClass().getName());
        }
        return texturePath;
    }
}
