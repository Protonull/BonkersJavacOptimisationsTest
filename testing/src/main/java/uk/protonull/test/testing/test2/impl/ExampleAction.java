package uk.protonull.test.testing.test2.impl;

import java.util.UUID;
import uk.protonull.test.softdepend.Action;

public record ExampleAction(
    String name,
    int x,
    int y,
    int z
) implements Action {
    public static final String IDENTIFIER = "HELLO_WORLD";

    @Override
    public int getLocationX() {
        return x();
    }

    @Override
    public int getLocationY() {
        return y();
    }

    @Override
    public int getLocationZ() {
        return z();
    }

    public static Action provider(
        UUID uuid
    ) {
        return new ExampleAction(uuid.toString(), 0, 0, 0);
    }
}
