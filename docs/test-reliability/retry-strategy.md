# Retry Strategy & Flaky Test Handling

## Overview

Retry mechanism automatically retries failed tests to handle environmental flakiness (network timeouts, timing issues). **Retry is a tactical solution, not a permanent fix.**

## Configuration

- **Max Retries**: 2 (3 total attempts)
- **Scope**: All tests (auto-applied via RetryListener)
- **Tracking**: Automatic flakiness report after each run

## When to Use Retry

**Valid Use Cases** ✅:
- Network timeouts (external API calls)
- Selenium StaleElementReferenceException (DOM timing)
- CI environment resource contention

**Invalid Use Cases** ❌:
- Covering up bad test design
- Ignoring race conditions in code
- Masking real application bugs

## Implementation

### Components

1. **RetryAnalyzer** (`utils/RetryAnalyzer.java`)
   - Implements IRetryAnalyzer
   - Retries up to 2 times
   - Calls trackFlakyTest() on each retry

2. **RetryListener** (`listeners/RetryListener.java`)
   - Auto-applies RetryAnalyzer to all tests
   - Tracks flakiness metrics
   - Prints flakiness report at end of suite

3. **Configuration**
   - testng.xml: Listener registered
   - pom.xml: Surefire plugin listener property

## Flakiness Investigation Process

When a test appears in the flakiness report:

1. **Reproduce**: Can you make it fail consistently?
2. **Check Logs**: What's the failure pattern?
3. **Root Cause**: Test issue or app issue?
4. **Fix**:
   - Test issue → Add explicit waits, improve selectors
   - App issue → File bug, disable test until fixed
   - Environment → Document, accept retry

## Interview Talking Point

> "I implemented retry as a tactical solution for environmental flakiness, but I track all retry events in a flakiness report. Any test that retries gets investigated for root cause. Retry is a band-aid, not a fix. The goal is zero flaky tests."

## Metrics to Track

- % of tests requiring retry
- Most frequently flaky tests
- Trend over time (improving or worsening)

**Goal**: Zero flaky tests in production suite
