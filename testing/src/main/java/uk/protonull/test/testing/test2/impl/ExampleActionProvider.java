package uk.protonull.test.testing.test2.impl;

import java.util.UUID;
import uk.protonull.test.softdepend.Action;
import uk.protonull.test.softdepend.ActionProvider;

public class ExampleActionProvider implements ActionProvider {
    @Override
    public Action construct(
        UUID uuid
    ) {
        return new ExampleAction(uuid.toString(), 0, 0, 0);
    }
}
