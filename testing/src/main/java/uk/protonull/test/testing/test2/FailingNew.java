package uk.protonull.test.testing.test2;

import uk.protonull.test.softdepend.ActionRegistry;
import uk.protonull.test.testing.test2.impl.ExampleAction;
import uk.protonull.test.testing.test2.impl.ExampleActionProvider;

/**
 * This class will cause a {@link NoClassDefFoundError} because of the `new ExampleActionProvider()`, unsure why.
 */
public final class FailingNew {
    public void init() {
        ActionRegistry.registerActionProvider(
            ExampleAction.IDENTIFIER,
            new ExampleActionProvider()
        );
    }
}
