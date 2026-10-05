package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.GdxInputProvider;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

/** Wires everything together and runs the loop: input -> update -> render. */
public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_FILE = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final GridPoint2 TANK_START = new GridPoint2(1, 1);
    private static final GridPoint2 TREE_POSITION = new GridPoint2(1, 3);
    private static final float TANK_MOVEMENT_SPEED = 0.4f;

    private LevelRenderer levelRenderer;
    private Level level;
    private KeyboardController controller;

    @Override
    public void create() {
        levelRenderer = new LevelRenderer(LEVEL_FILE);
        level = new Level(levelRenderer.getLevelWidth(), levelRenderer.getLevelHeight());

        Tank playerTank = new Tank(TANK_START, Direction.RIGHT, TANK_MOVEMENT_SPEED, level);
        place(playerTank, TANK_TEXTURE);
        place(new Tree(TREE_POSITION), TREE_TEXTURE);

        // new action = new Command + one more bind()
        controller = new KeyboardController(new GdxInputProvider())
                .bind(UP, new MoveCommand(playerTank, Direction.UP))
                .bind(W, new MoveCommand(playerTank, Direction.UP))
                .bind(LEFT, new MoveCommand(playerTank, Direction.LEFT))
                .bind(A, new MoveCommand(playerTank, Direction.LEFT))
                .bind(DOWN, new MoveCommand(playerTank, Direction.DOWN))
                .bind(S, new MoveCommand(playerTank, Direction.DOWN))
                .bind(RIGHT, new MoveCommand(playerTank, Direction.RIGHT))
                .bind(D, new MoveCommand(playerTank, Direction.RIGHT));
    }

    private void place(GameObject object, String texturePath) {
        level.add(object);
        levelRenderer.add(object, texturePath);
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        controller.processInput();
        level.update(deltaTime);
        levelRenderer.render();
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
