package ru.mipt.bit.platformer.generation;

/** Application output port, Strategy: fills a level of the given size. */
public interface LevelGenerator {

    LevelLayout generate(int width, int height);
}
