# Ports & adapters

Every class is marked in its javadoc: layer, and port / adapter where it is one.

## Layers

| Layer | Classes |
|---|---|
| Domain (entities) | `Tank`, `Bullet`, `Tree`, `Level`, `Direction`, `LevelLayout`, `TankState` + `HealthState`, `LightState`, `MediumState`, `HeavyState`; interfaces `GameObject`, `Movable`, `Shooter`, `Damageable`, `HasHealth`, `CollisionChecker`, `Battlefield`; port `LevelObserver` |
| Application (use cases) | `MoveCommand`, `ShootCommand`, `ToggleCommand`, `CommandInvoker`, `RandomController`, `RandomLevelGenerator`; ports `Command`, `ControlStrategy`, `InputProvider`, `LevelGenerator`, `Toggleable` |
| Infrastructure | `GameDesktopLauncher`, `GdxInputProvider`, `KeyboardController`, `FileLevelGenerator`, `graphics/*`, `util/*` |

## Ports and adapters

| Port | Kind | Implemented by |
|---|---|---|
| `Command` | input | use cases `MoveCommand`, `ShootCommand`, `ToggleCommand` |
| `ControlStrategy` | input | `KeyboardController` (adapter of the keyboard), `RandomController` (AI) |
| `InputProvider` | output | `GdxInputProvider` |
| `LevelGenerator` | output | `FileLevelGenerator` (adapter of the file system), `RandomLevelGenerator` (no I/O) |
| `LevelObserver` | output | `LevelRenderer`, `CommandInvoker` |
| `Toggleable` | output | `HealthBars` |

`CollisionChecker` and `Battlefield` are domain interfaces, not ports: `Level` implements them inside the domain.
`Renderable` and `Decoration` are interfaces inside infrastructure.

## Missing ports and adapters

- The domain depends on libGDX: `GridPoint2` everywhere, `MathUtils` and `GdxGameUtils.continueProgress` in `Tank` and `Bullet`.
  Needs an own coordinate type in the domain and a conversion in the graphics adapter.
- No use case for one frame: `GameDesktopLauncher.render()` itself runs commands, updates the level and draws.
  Needs an input port like `Game.tick(deltaTime)`, the launcher would only call it.
- The level size comes from the TMX map through `LevelRenderer`, so the domain level is sized by graphics.
  Needs a port for the map size.
- `java.util.Random` is used directly in `RandomController`, `RandomLevelGenerator` and the launcher (tank health).
  A random source port would make them testable without fixed seeds.
- Game settings (speeds, health range, number of trees and enemies) are constants in the launcher, no configuration port.
