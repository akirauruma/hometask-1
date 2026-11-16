# tank-software-design

## Run

- random level with 3 enemy tanks driving in random directions: `./gradlew run`
- level from a file: `./gradlew run --args=levels/level.txt`

Controls: arrows / WASD — move, SPACE — shoot (one shot per 0.5 s), L — show / hide health bars.

Every tank starts with random health from 80 to 100, a hit takes 30. Bullets that meet destroy each other.

Tank states by health: light (> 70%) — as undamaged, medium (15–70%) — twice slower,
heavy (< 15%) — three times slower and cannot shoot.

Layers, ports and adapters: [ARCHITECTURE.md](ARCHITECTURE.md).

Level file: `T` — tree, `X` — player start, `_` — empty tile. The size must match the map (10x8), the first line is the top row.
