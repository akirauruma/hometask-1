package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextureBindingsTest {

    /** A new object type nobody has bound a texture to. */
    private static class Rock implements GameObject {
        @Override
        public GridPoint2 getCoordinates() {
            return new GridPoint2(0, 0);
        }
    }

    @Test
    void givesTheTextureBoundToTheObjectsClass() {
        TextureBindings bindings = new TextureBindings()
                .bind(Tree.class, "tree.png")
                .bind(Rock.class, "rock.png");

        assertEquals("tree.png", bindings.texturePathFor(new Tree(new GridPoint2(1, 1))));
        assertEquals("rock.png", bindings.texturePathFor(new Rock()));
    }

    @Test
    void unboundObjectTypeIsAnErrorInsteadOfATree() {
        TextureBindings bindings = new TextureBindings().bind(Tree.class, "tree.png");

        assertThrows(IllegalArgumentException.class, () -> bindings.texturePathFor(new Rock()));
    }
}
