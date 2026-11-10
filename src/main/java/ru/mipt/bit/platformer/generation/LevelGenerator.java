package ru.mipt.bit.platformer.generation;

/** Strategy of filling a level of the given size. */
public interface LevelGenerator {

    LevelLayout generate(int width, int height);
}
