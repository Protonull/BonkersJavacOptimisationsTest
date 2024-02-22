package uk.protonull.test.testing.test3;

import java.util.UUID;
import uk.protonull.test.softdepend.ActionUser;
import uk.protonull.test.testing.test1.impl.ExampleAction;

/**
 * This is the working-alternative to {@link FailingField} that instead constructs the anonymous class within a method
 * body.
 */
public final class WorkingField {
    private Object events = null;

    public void init() {
        this.events = new Object() {
            public void handleSomeEvent(
                final Object event
            ) {
                for (final ActionUser user : ActionUser.getUsers()) {
                    user.acceptAction(new ExampleAction(UUID.randomUUID().toString(), 0, 0, 0));
                }
            }
        };
        //Something.eventBus.register(this.events);
    }
}
