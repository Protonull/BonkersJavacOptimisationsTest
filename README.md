# BonkersJavacOptimisationsTest

Came across these issues while working on a Minecraft plugin that softly depends on other plugins. It hadn't been an
issue since those plugins were always present, but as soon as one was missing, the plugin exploded with
`NoClassDefFoundError` exceptions.

The problem arises from the assumption that you can directly reference potentially-missing classes if the process of
execution never 'reaches' that code, eg:
```java
class ChestLockerListener implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onChestOpen(FictionalChestOpenEvent event) {
        if (Bukkit.getPluginManager().isPluginEnabled("SomePlugin")) {
            if (SomePlugin.isChestLocked(event.getChest())) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
}
```
And yes, this *does* work. However, there are cases where javac optimises your code ways that break this assumption.
This repo demonstrates three instances of this (see [SoftDependencyTests](testing/src/test/java/uk/protonull/tests/SoftDependencyTests.java)):

**KEEP IN MIND THAT THE `init` METHOD IN ALL THE TESTS IS NEVER INVOKED!**

## Tests

### Lambda Hoisting

If you've played around with any Java decompilers, you may have noticed that lambdas are often compiled as sibling
classes where, for example, lambdas in `ExampleClass` will produce `ExampleClass$1` as a standalone class within the
same package. This is not always true, something that `lambdaHoistTest()` demonstrates. Instead, the lambda gets hoisted
as a sibling *method*, eg:
```java
public final class Demonstration {
    public void init() {
        ActionRegistry.registerActionProvider(
            ExampleAction.IDENTIFIER,
            this::lambda$init$0
        );
    }

    public Action lambda$init$0(UUID uuid) {
        return new ExampleAction(uuid.toString(), 0, 0, 0);
    }
}
```
And because `Action` is a class from a missing soft-dependency, the `Demonstration` class fails upon initialisation.

### Const `new`

However, manually doing what you wish the compiler had done and creating a sibling class isn't helpful either.
```java
public final class Demonstration {
    public void init() { // Remember that this is never invoked
        ActionRegistry.registerActionProvider(
            ExampleAction.IDENTIFIER,
            new ExampleActionProvider() // Why does this cause a NoClassDefFoundError?! ಠ_ಠ
        );
    }
}
```
You'd think that, based on the `ChestLockerListener` example above, that this would be fine. But *something* is
happening here that's causing the `Demonstration` class to fail upon initialisation that I can't put my finger on... but
whatever it is, the issue is 'fixed' by replacing it with a static-method reference, even if that static method is on an
missing class.
```java
public final class Demonstration {
    public void init() { // Remember that this is never invoked
        ActionRegistry.registerActionProvider(
            ExampleAction.IDENTIFIER,
            ExampleAction::provider // This fixes it... for some reason
        );
    }
}
```

### Const fields

Now, let's say you have an event listener like so:
```java
public final class Demonstration {
    private final Object events = new Object() { // This is never registered nor is any of its methods invoked
        @Subscribe
        public void handleSomeEvent(
            final Object event
        ) {
            for (final ActionUser user : ActionUser.getUsers()) {
                user.acceptAction(new ExampleAction(UUID.randomUUID().toString(), 0, 0, 0));
            }
        }
    };

    public void init() { // Remember that this is never invoked
        Something.eventBus.register(this.events);
    }
}
```
That anonymous class is compiled as a sibling class, as expected. And yet, despite none of the missing classes being
'reached'. Fixing this means making the field no longer final (or effectively final) and re-assigning it within the
`init` method, like so:
```java
public final class Demonstration {
    private final Object events = null;

    public void init() { // Remember that this is never invoked
        Something.eventBus.register(this.events = new Object() {
            @Subscribe
            public void handleSomeEvent(
                final Object event
            ) {
                for (final ActionUser user : ActionUser.getUsers()) {
                    user.acceptAction(new ExampleAction(UUID.randomUUID().toString(), 0, 0, 0));
                }
            }
        });
    }
}
```
Which is bizarre because this changes seemingly nothing about how the anonymous class gets compiled as a sibling class.

## How to run

Clone this repo and execute `sh run.sh`

## Results

```
openjdk 17.0.9 2023-10-17 LTS
OpenJDK Runtime Environment Corretto-17.0.9.8.1 (build 17.0.9+8-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.9.8.1 (build 17.0.9+8-LTS, mixed mode, sharing)

> Task :testing:test

SoftDependencyTests > constParameterTest() STARTED

SoftDependencyTests > constParameterTest() PASSED

SoftDependencyTests > lambdaHoistTest() STARTED

SoftDependencyTests > lambdaHoistTest() PASSED

SoftDependencyTests > constFieldTest() STARTED

SoftDependencyTests > constFieldTest() PASSED

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.5/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 1s
7 actionable tasks: 7 executed
```
