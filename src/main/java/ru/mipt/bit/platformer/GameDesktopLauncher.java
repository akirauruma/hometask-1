package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.CommandInvoker;
import ru.mipt.bit.platformer.control.GdxInputProvider;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.ShootCommand;
import ru.mipt.bit.platformer.control.RandomController;
import ru.mipt.bit.platformer.generation.FileLevelGenerator;
import ru.mipt.bit.platformer.generation.LevelGenerator;
import ru.mipt.bit.platformer.generation.LevelLayout;
import ru.mipt.bit.platformer.generation.RandomLevelGenerator;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.TextureBindings;
import ru.mipt.bit.platformer.model.Bullet;
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
import static com.badlogic.gdx.Input.Keys.SPACE;

/** Wires everything together and runs the loop: input -> update -> render. */
public class GameDesktopLauncher implements ApplicationListener {

    private static final String MAP_FILE = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final String BULLET_TEXTURE = "images/brick.png";
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final int RANDOM_TREE_COUNT = 10;
    private static final int RANDOM_ENEMY_COUNT = 3;

    private final LevelGenerator levelGenerator;
    private final Random random = new Random();
    private LevelRenderer levelRenderer;
    private Level level;
    private CommandInvoker invoker;

    public GameDesktopLauncher(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    @Override
    public void create() {
        levelRenderer = new LevelRenderer(MAP_FILE, new TextureBindings()
                .bind(Tank.class, TANK_TEXTURE)
                .bind(Tree.class, TREE_TEXTURE)
                .bind(Bullet.class, BULLET_TEXTURE));
        int width = levelRenderer.getLevelWidth();
        int height = levelRenderer.getLevelHeight();
        level = new Level(width, height);
        invoker = new CommandInvoker();
        level.subscribe(levelRenderer);
        level.subscribe(invoker);
        LevelLayout layout = levelGenerator.generate(width, height);

        for (GridPoint2 treeCoordinates : layout.getTrees()) {
            place(new Tree(treeCoordinates));
        }
        Tank playerTank = placeTank(layout.getPlayerStart());
        invoker.add(playerTank, playerControls(playerTank));
        for (GridPoint2 enemyStart : layout.getEnemyTanks()) {
            Tank enemyTank = placeTank(enemyStart);
            invoker.add(enemyTank, new RandomController(enemyTank, random));
        }
    }

    private Tank placeTank(GridPoint2 start) {
        Tank tank = new Tank(start, Direction.RIGHT, TANK_MOVEMENT_SPEED, level, level);
        place(tank);
        return tank;
    }

    private static KeyboardController playerControls(Tank playerTank) {
        return new KeyboardController(new GdxInputProvider())
                .bind(UP, new MoveCommand(playerTank, Direction.UP))
                .bind(W, new MoveCommand(playerTank, Direction.UP))
                .bind(LEFT, new MoveCommand(playerTank, Direction.LEFT))
                .bind(A, new MoveCommand(playerTank, Direction.LEFT))
                .bind(DOWN, new MoveCommand(playerTank, Direction.DOWN))
                .bind(S, new MoveCommand(playerTank, Direction.DOWN))
                .bind(RIGHT, new MoveCommand(playerTank, Direction.RIGHT))
                .bind(D, new MoveCommand(playerTank, Direction.RIGHT))
                .bind(SPACE, new ShootCommand(playerTank));
    }

    private void place(GameObject object) {
        level.add(object);
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        invoker.invokeAll();
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
                : new RandomLevelGenerator(RANDOM_TREE_COUNT, RANDOM_ENEMY_COUNT, new Random());

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(levelGenerator), config);
    }
}
