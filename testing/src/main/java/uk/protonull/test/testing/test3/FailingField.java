package uk.protonull.test.testing.test3;

import java.util.UUID;
import uk.protonull.test.softdepend.ActionUser;
import uk.protonull.test.testing.test1.impl.ExampleAction;

/**
 * This class will cause a {@link NoClassDefFoundError} despite the soft-dependency classes being buried within the
 * method body of an anonymous class, which should become a sibling class (within the same package).
 */
public final class FailingField {
    private final Object events = new Object() {
        public void handleSomeEvent(
            final Object event
        ) {
            for (final ActionUser user : ActionUser.getUsers()) {
                user.acceptAction(new ExampleAction(UUID.randomUUID().toString(), 0, 0, 0));
            }
        }
    };

    public void init() {
        //Something.eventBus.register(this.events);
    }
}
