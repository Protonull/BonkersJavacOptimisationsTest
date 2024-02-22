package uk.protonull.test.softdepend;

import java.util.List;
import java.util.logging.Logger;

public final class ActionUser {
    private static final Logger LOGGER = Logger.getLogger(ActionUser.class.getSimpleName());

    public void acceptAction(
        final Action action
    ) {
        LOGGER.info("Accepted action: " + action.name());
    }

    public static List<ActionUser> getUsers() {
        return List.of();
    }
}
