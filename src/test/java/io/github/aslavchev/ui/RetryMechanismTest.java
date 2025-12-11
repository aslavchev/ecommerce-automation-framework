package io.github.aslavchev.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validation tests for retry mechanism.
 * These tests intentionally fail to verify retry behavior.
 */
public class RetryMechanismTest {

    private static int attemptCount = 0;

    @Test(groups = "reliability")
    public void testRetrySucceedsOnSecondAttempt() {
        attemptCount++;
        System.out.println("Attempt #" + attemptCount);

        // Fail on first attempt, pass on retry
        if (attemptCount < 2) {
            Assert.fail("Intentional failure to test retry");
        }

        Assert.assertTrue(true, "Test passed on retry");
    }

    @Test(groups = "reliability")
    public void testRetryEventuallyFails() {
        // This should fail even after retries (max 2 retries = 3 total attempts)
        Assert.fail("This test fails to verify retry limit works");
    }
}