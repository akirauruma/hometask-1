package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

/**
 * Everything that has to do with drawing: the tiled map, the sprite batch and the
 * textures of the objects. The model classes stay free of libGDX rendering code.
 */
public class LevelRenderer implements Disposable {

    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    private final GameObjectGraphics tankGraphics;
    private final GameObjectGraphics treeGraphics;

    public LevelRenderer(String levelFile) {
        batch = new SpriteBatch();

        tiledMap = new TmxMapLoader().load(levelFile);
        tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        groundLayer = getSingleLayer(tiledMap);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tankGraphics = new GameObjectGraphics(TANK_TEXTURE);
        treeGraphics = new GameObjectGraphics(TREE_TEXTURE);
    }

    /** Field size in tiles, taken from the map itself instead of being hard-coded. */
    public int getLevelWidth() {
        return groundLayer.getWidth();
    }

    public int getLevelHeight() {
        return groundLayer.getHeight();
    }

    public void render(Level level) {
        clearScreen();

        // render each tile of the level
        tiledMapRenderer.render();

        // start recording all drawing commands
        batch.begin();

        tankGraphics.draw(batch, tileMovement, level.getPlayerTank());
        for (Tree tree : level.getTrees()) {
            treeGraphics.draw(batch, tileMovement, tree);
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
        treeGraphics.dispose();
        tankGraphics.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
