package uk.protonull.test.testing.test1;

import uk.protonull.test.softdepend.ActionRegistry;
import uk.protonull.test.testing.test1.impl.ExampleAction;

/**
 * This class will cause a {@link NoClassDefFoundError} because the lambda within 'init' gets hoisted as a sibling
 * method.
 */
public final class LambdaHoist {
    public void init() {
        ActionRegistry.registerActionProvider(
            ExampleAction.IDENTIFIER,
            (uuid) -> new ExampleAction(uuid.toString(), 0, 0, 0)
        );
    }
}
