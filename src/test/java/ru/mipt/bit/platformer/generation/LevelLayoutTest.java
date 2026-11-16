package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelLayoutTest {

    @Test
    void laterChangesOfTheGivenDataDoNotLeakIn() {
        GridPoint2 player = new GridPoint2(1, 1);
        List<GridPoint2> trees = new ArrayList<>(List.of(new GridPoint2(2, 2)));
        List<GridPoint2> enemies = new ArrayList<>(List.of(new GridPoint2(3, 3)));
        LevelLayout layout = new LevelLayout(player, trees, enemies);

        player.set(0, 0);
        trees.clear();
        enemies.clear();

        assertEquals(new GridPoint2(1, 1), layout.getPlayerStart());
        assertEquals(List.of(new GridPoint2(2, 2)), layout.getTrees());
        assertEquals(List.of(new GridPoint2(3, 3)), layout.getEnemyTanks());
    }

    @Test
    void listsCannotBeModified() {
        LevelLayout layout = new LevelLayout(new GridPoint2(), List.of(), List.of());
        assertThrows(UnsupportedOperationException.class, () -> layout.getEnemyTanks().add(new GridPoint2()));
    }
}
