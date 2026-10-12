package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.GdxInputProvider;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.generation.FileLevelGenerator;
import ru.mipt.bit.platformer.generation.LevelGenerator;
import ru.mipt.bit.platformer.generation.LevelLayout;
import ru.mipt.bit.platformer.generation.RandomLevelGenerator;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.nio.file.Paths;
import java.util.Random;

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

    private static final String MAP_FILE = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final int RANDOM_TREE_COUNT = 10;

    private final LevelGenerator levelGenerator;
    private LevelRenderer levelRenderer;
    private Level level;
    private KeyboardController controller;

    public GameDesktopLauncher(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    @Override
    public void create() {
        levelRenderer = new LevelRenderer(MAP_FILE);
        int width = levelRenderer.getLevelWidth();
        int height = levelRenderer.getLevelHeight();
        level = new Level(width, height);
        LevelLayout layout = levelGenerator.generate(width, height);

        Tank playerTank = new Tank(layout.getPlayerStart(), Direction.RIGHT, TANK_MOVEMENT_SPEED, level);
        place(playerTank, TANK_TEXTURE);
        for (GridPoint2 treeCoordinates : layout.getTrees()) {
            place(new Tree(treeCoordinates), TREE_TEXTURE);
        }

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

    /** Optional argument: path to a level file, without it the level is random. */
    public static void main(String[] args) {
        LevelGenerator levelGenerator = args.length > 0
                ? new FileLevelGenerator(Paths.get(args[0]))
                : new RandomLevelGenerator(RANDOM_TREE_COUNT, new Random());

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(levelGenerator), config);
    }
}
