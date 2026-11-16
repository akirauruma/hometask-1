package ru.mipt.bit.platformer.control;

/** Switches a {@link Toggleable} on or off, e.g. the health bars. */
public class ToggleCommand implements Command {

    private final Toggleable toggleable;

    public ToggleCommand(Toggleable toggleable) {
        this.toggleable = toggleable;
    }

    @Override
    public void execute() {
        toggleable.toggle();
    }
}
