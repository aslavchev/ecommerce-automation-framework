package io.github.aslavchev.utils;

import io.github.aslavchev.listeners.RetryListener;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG Retry Analyzer for handling flaky test scenarios.
 *
 * IMPORTANT: Retry is a tactical solution for environmental flakiness,
 * not a substitute for fixing unstable tests. All retried tests should
 * be investigated and fixed at the root cause.
 *
 * Usage: @Test(retryAnalyzer = RetryAnalyzer.class)
 * Or automatic via RetryListener
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            String testName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();

            // CRITICAL: Notify listener to track flakiness
            RetryListener.trackFlakyTest(testName);

            // Log retry attempt
            System.out.println("⚠️ RETRY ATTEMPT " + retryCount + "/" + MAX_RETRY_COUNT +
                    " for test: " + testName);
            if (result.getThrowable() != null) {
                System.out.println("   Failure reason: " + result.getThrowable().getMessage());
            }

            return true; // Retry the test
        }
        return false; // Max retries reached, fail the test
    }
}
