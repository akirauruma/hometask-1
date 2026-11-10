# tank-software-design

## Run

- random level with 3 enemy tanks driving in random directions: `./gradlew run`
- level from a file: `./gradlew run --args=levels/level.txt`

Controls: arrows / WASD — move, SPACE — shoot (one shot per 0.5 s). A tank is destroyed by 3 hits; bullets that meet destroy each other.

Level file: `T` — tree, `X` — player start, `_` — empty tile. The size must match the map (10x8), the first line is the top row.
