package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

/** Draws one object. The texture may be shared, so it is not disposed here. */
public class GameObjectGraphics implements Renderable {

    private final GameObject object;
    private final TextureRegion region;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;

    public GameObjectGraphics(GameObject object, Texture texture, TileMovement tileMovement) {
        this.object = object;
        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
        this.region = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(region);
        this.tileMovement = tileMovement;
    }

    @Override
    public void render(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle, object.getCoordinates(), object.getDestination(), object.getMovementProgress());
        drawTextureRegionUnscaled(batch, region, rectangle, object.getRotation());
    }
}
