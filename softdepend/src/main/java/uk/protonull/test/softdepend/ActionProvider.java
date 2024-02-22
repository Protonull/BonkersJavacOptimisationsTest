package uk.protonull.test.softdepend;

import java.util.UUID;

@FunctionalInterface
public interface ActionProvider {
    Action construct(
        UUID uuid
    );
}
