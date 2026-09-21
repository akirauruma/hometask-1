package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.GdxInputProvider;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.control.MovePlayerCommand;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

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

        // One binding per key. Adding a new handler later (e.g. SPACE -> fire) is a single
        // extra .bind(...) line here and a new Command class — nothing else changes.
        controller = new KeyboardController(new GdxInputProvider())
                .bind(UP, new MovePlayerCommand(level, Direction.UP))
                .bind(W, new MovePlayerCommand(level, Direction.UP))
                .bind(LEFT, new MovePlayerCommand(level, Direction.LEFT))
                .bind(A, new MovePlayerCommand(level, Direction.LEFT))
                .bind(DOWN, new MovePlayerCommand(level, Direction.DOWN))
                .bind(S, new MovePlayerCommand(level, Direction.DOWN))
                .bind(RIGHT, new MovePlayerCommand(level, Direction.RIGHT))
                .bind(D, new MovePlayerCommand(level, Direction.RIGHT));
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        controller.processInput();
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
