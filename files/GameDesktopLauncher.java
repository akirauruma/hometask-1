package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;

/**
 * Only wires the pieces together and runs the game loop:
 * read input -> update the model -> draw the model.
 */
public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_FILE = "level.tmx";
    private static final GridPoint2 TANK_START = new GridPoint2(1, 1);
    private static final GridPoint2 TREE_POSITION = new GridPoint2(1, 3);

    private LevelRenderer levelRenderer;
    private Level level;
    private KeyboardController controller;

    @Override
    public void create() {
        levelRenderer = new LevelRenderer(LEVEL_FILE);
        level = new Level(
                levelRenderer.getLevelWidth(),
                levelRenderer.getLevelHeight(),
                new Tank(TANK_START, Direction.RIGHT),
                Collections.singletonList(new Tree(TREE_POSITION)));
        controller = new KeyboardController();
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        controller.getPressedDirection().ifPresent(level::movePlayerTank);
        level.update(deltaTime);
        levelRenderer.render(level);
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        levelRenderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
