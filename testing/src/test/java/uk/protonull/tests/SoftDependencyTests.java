package uk.protonull.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.protonull.test.testing.test1.LambdaHoist;
import uk.protonull.test.testing.test2.FailingNew;
import uk.protonull.test.testing.test2.WorkingReference;
import uk.protonull.test.testing.test3.FailingField;
import uk.protonull.test.testing.test3.WorkingField;

public class SoftDependencyTests {
    @Test
    public void lambdaHoistTest() {
        Assertions.assertThrowsExactly(
            NoClassDefFoundError.class,
            // Ignore the highlight, or you'll literally re-create the issue this test is demonstrating.
            () -> new LambdaHoist()
        );
    }

    @Test
    public void constParameterTest() {
        Assertions.assertThrowsExactly(
            NoClassDefFoundError.class,
            // Ignore the highlight, or you'll literally re-create the issue demonstrated in lambdaHoistTest()
            () -> new FailingNew()
        );
        Assertions.assertDoesNotThrow(
            // Ignore the highlight, or you'll literally re-create the issue demonstrated in lambdaHoistTest()
            () -> new WorkingReference()
        );
    }

    @Test
    public void constFieldTest() {
        Assertions.assertThrowsExactly(
            NoClassDefFoundError.class,
            // Ignore the highlight, or you'll literally re-create the issue demonstrated in lambdaHoistTest()
            () -> new FailingField()
        );
        Assertions.assertDoesNotThrow(
            // Ignore the highlight, or you'll literally re-create the issue demonstrated in lambdaHoistTest()
            () -> new WorkingField()
        );
    }
}
