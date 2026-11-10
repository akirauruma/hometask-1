package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelObserver;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

/**
 * Draws the map and every object of the level it observes. An object gets drawn only through
 * {@link #objectAdded} and stops being drawn when it leaves the level.
 */
public class LevelRenderer implements Disposable, LevelObserver {

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final TextureBindings textureBindings;

    private final Map<String, Texture> textures = new HashMap<>();
    /** In the order objects were added, which is also the drawing order. */
    private final Map<GameObject, Renderable> renderables = new LinkedHashMap<>();

    public LevelRenderer(String levelFile, TextureBindings textureBindings) {
        this.textureBindings = textureBindings;
        batch = new SpriteBatch();

        tiledMap = new TmxMapLoader().load(levelFile);
        tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        groundLayer = getSingleLayer(tiledMap);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public int getLevelWidth() {
        return groundLayer.getWidth();
    }

    public int getLevelHeight() {
        return groundLayer.getHeight();
    }

    @Override
    public void objectAdded(GameObject object) {
        // each texture file is loaded once and shared
        Texture texture = textures.computeIfAbsent(textureBindings.texturePathFor(object), Texture::new);
        renderables.put(object, new GameObjectGraphics(object, texture, tileMovement));
    }

    @Override
    public void objectRemoved(GameObject object) {
        renderables.remove(object);
    }

    public void render() {
        clearScreen();

        // render each tile of the level
        tiledMapRenderer.render();

        // start recording all drawing commands
        batch.begin();

        for (Renderable renderable : renderables.values()) {
            renderable.render(batch);
        }

        // submit all drawing requests
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        tiledMap.dispose();
        batch.dispose();
    }
}
