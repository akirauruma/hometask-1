package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Shooter;

public class ShootCommand implements Command {

    private final Shooter shooter;

    public ShootCommand(Shooter shooter) {
        this.shooter = shooter;
    }

    @Override
    public void execute() {
        shooter.shoot();
    }
}
