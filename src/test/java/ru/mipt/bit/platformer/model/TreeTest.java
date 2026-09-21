package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void keepsItsCoordinates() {
        Tree tree = new Tree(new GridPoint2(2, 7));
        assertEquals(new GridPoint2(2, 7), tree.getCoordinates());
    }

    @Test
    void copiesTheGivenPointSoLaterMutationsDoNotLeakIn() {
        GridPoint2 start = new GridPoint2(2, 7);
        Tree tree = new Tree(start);
        start.set(0, 0);
        assertEquals(new GridPoint2(2, 7), tree.getCoordinates());
    }

    @Test
    void isAStaticObjectAtRestFacingUpright() {
        // A tree relies on the GameObject defaults: it never moves and is never rotated.
        Tree tree = new Tree(new GridPoint2(2, 7));
        assertEquals(tree.getCoordinates(), tree.getDestination());
        assertEquals(1f, tree.getMovementProgress());
        assertEquals(0f, tree.getRotation());
    }
}
