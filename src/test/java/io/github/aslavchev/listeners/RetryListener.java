package io.github.aslavchev.listeners;

import io.github.aslavchev.utils.RetryAnalyzer;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * TestNG listener that:
 * 1. Automatically applies RetryAnalyzer to ALL tests
 * 2. Tracks flakiness metrics (which tests retry, how often)
 * 3. Reports flaky tests at end of suite
 */
public class RetryListener implements IAnnotationTransformer, ISuiteListener {

    private static Map<String, Integer> flakyTests = new HashMap<>();

    /**
     * Automatically apply RetryAnalyzer to all test methods
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    /**
     * Track retried tests (called from RetryAnalyzer)
     * CRITICAL: This is called directly from RetryAnalyzer.retry() method
     */
    public static void trackFlakyTest(String testName) {
        flakyTests.put(testName, flakyTests.getOrDefault(testName, 0) + 1);
    }

    /**
     * Report flakiness summary at end of test suite
     */
    @Override
    public void onFinish(ISuite suite) {
        if (!flakyTests.isEmpty()) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("⚠️  FLAKINESS REPORT - Tests That Required Retries");
            System.out.println("=".repeat(60));

            flakyTests.forEach((testName, retryCount) -> {
                System.out.println(String.format("   🔴 %s (retried %d times)",
                        testName, retryCount));
            });

            System.out.println("=".repeat(60));
            System.out.println("⚠️  ACTION REQUIRED: Investigate root cause for flaky tests");
            System.out.println("=".repeat(60) + "\n");
        }
    }
}