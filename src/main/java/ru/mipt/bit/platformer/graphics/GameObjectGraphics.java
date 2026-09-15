package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

/**
 * The picture of one kind of object: texture, its region and the rectangle it is drawn in.
 * <p>
 * Loading a texture and drawing it used to be written out separately for the tank and for
 * the tree; here it is written once and reused by every object that shares the texture.
 */
public class GameObjectGraphics implements Disposable {

    private final Texture texture;
    private final TextureRegion region;
    private final Rectangle rectangle;

    public GameObjectGraphics(String texturePath) {
        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        this.texture = new Texture(texturePath);
        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
        this.region = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(region);
    }

    /**
     * Place the rectangle between the object's current tile and its destination and draw it.
     * A static object has both tiles equal and progress 1, so it simply lands on its tile.
     */
    public void draw(Batch batch, TileMovement tileMovement, GameObject object) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle, object.getCoordinates(), object.getDestination(), object.getMovementProgress());
        drawTextureRegionUnscaled(batch, region, rectangle, object.getRotation());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
