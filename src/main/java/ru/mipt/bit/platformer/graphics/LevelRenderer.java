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
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

/** Draws the map and the added {@link Renderable}s. */
public class LevelRenderer implements Disposable {

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    private final Map<String, Texture> textures = new HashMap<>();
    private final List<Renderable> renderables = new ArrayList<>();

    public LevelRenderer(String levelFile) {
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

    public void add(GameObject object, String texturePath) {
        // each texture file is loaded once and shared
        Texture texture = textures.computeIfAbsent(texturePath, Texture::new);
        add(new GameObjectGraphics(object, texture, tileMovement));
    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
    }

    public void render() {
        clearScreen();

        // render each tile of the level
        tiledMapRenderer.render();

        // start recording all drawing commands
        batch.begin();

        for (Renderable renderable : renderables) {
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
