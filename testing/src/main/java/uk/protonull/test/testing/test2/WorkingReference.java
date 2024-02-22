package uk.protonull.test.testing.test2;

import uk.protonull.test.softdepend.ActionRegistry;
import uk.protonull.test.testing.test2.impl.ExampleAction;

/**
 * This is the working-alternative to {@link FailingNew} that instead uses a method-reference with a conforming
 * signature.
 */
public final class WorkingReference {
    public void init() {
        ActionRegistry.registerActionProvider(
            ExampleAction.IDENTIFIER,
            ExampleAction::provider
        );
    }
}
