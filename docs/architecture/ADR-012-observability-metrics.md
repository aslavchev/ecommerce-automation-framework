# ADR-012: Observability & Test Metrics Strategy

**Status**: Evaluated - Not Implemented
**Date**: 2025-12-11
**Decision Maker**: Alex Slavchev
**Context**: Phase 6.5 - Infrastructure Modernization, Part 2/3
**Outcome**: Evaluated structured metrics logging, decided not to implement (Allure sufficient at current scale)

---

## Context

With Docker + Selenium Grid infrastructure complete (Phase 6.5 Part 1) and 23 tests running across multiple browsers, the framework needs **production-grade observability** to support:

**Current Gaps:**
- No structured logging of test execution metrics
- Limited visibility into test duration, failure patterns, browser-specific issues
- Manual analysis required to extract insights from Allure reports
- No machine-readable metrics for trend analysis or CI dashboard integration
- Difficult to diagnose issues in distributed Grid environment

**Requirements:**
- Track test execution metrics: duration, status, browser, timestamp
- Enable trend analysis and performance regression detection
- Support distributed tracing across Grid nodes
- Machine-readable JSON format for tooling integration
- Minimal performance overhead

---

## Decision

**Implement structured logging with SLF4J + Logback for test metrics:**

1. **TestMetrics.java** - Utility class for structured metric logging
2. **JSON logging format** - Machine-readable output via Logback JSON encoder
3. **Automatic integration** - Hooks into BaseTest lifecycle (setup/teardown)
4. **Comprehensive metrics** - Test name, status, duration, browser, execution mode (local/grid), timestamp, retry count

**Technology Stack:**
- **SLF4J 2.0+** - Logging facade (abstraction layer)
- **Logback 1.5+** - Logging implementation with native JSON support
- **Jackson** - JSON serialization (already in project)

---

## Alternatives Considered

### Option 1: Continue with Log4j2 Only
- **Description**: Use existing Log4j2 with custom JSON layout
- **Pros**:
  - No new dependencies
  - Already configured
  - Supports JSON via JsonLayout
- **Cons**:
  - Log4j2 has security history concerns (Log4Shell)
  - Less flexible JSON configuration than Logback
  - Mixed logging approaches (Log4j2 for metrics, SLF4J already used by some deps)
- **Why rejected**: SLF4J facade provides better abstraction, Logback has cleaner JSON support

### Option 2: Custom Metrics Framework (TestNG ITestListener)
- **Description**: Build custom metrics collector using TestNG listeners
- **Pros**:
  - Full control over metric format
  - Direct integration with TestNG lifecycle
  - Could write to database or custom endpoint
- **Cons**:
  - Reinventing the wheel
  - More maintenance burden
  - Doesn't solve general logging needs
  - No standard tooling integration
- **Why rejected**: Over-engineering for current needs, standard logging more maintainable

### Option 3: OpenTelemetry Integration
- **Description**: Full observability stack with traces, metrics, logs (OTLP)
- **Pros**:
  - Industry-standard distributed tracing
  - Rich ecosystem (Jaeger, Prometheus, Grafana)
  - Excellent for microservices observability
- **Cons**:
  - Significant complexity for test framework scale
  - Requires backend infrastructure (collector, storage, visualization)
  - Steep learning curve
  - Overkill for 23-test framework
- **Why rejected**: Future consideration for large-scale implementations, too heavy for current scope

---

## Rationale

### Primary Reasons

1. **SLF4J Facade Pattern**: Industry best practice for logging abstraction
   - Decouples application code from logging implementation
   - Easy to swap implementations (Logback → Log4j2 → java.util.logging)
   - Most Java libraries use SLF4J (Selenium, REST Assured, TestNG)

2. **Logback Native JSON**: Best-in-class structured logging for Java
   - Built-in JSON encoding (logstash-logback-encoder)
   - Flexible configuration (XML/Groovy)
   - Excellent performance with async appenders
   - Strong community support

3. **Machine-Readable Metrics**: Enable automation and tooling
   - CI dashboards can parse JSON logs
   - Trend analysis scripts
   - Performance regression detection
   - Integration with monitoring systems (ELK, Splunk, CloudWatch)

### Supporting Factors

- **Industry Standard**: SLF4J + Logback widely adopted in enterprise Java environments
- **Practical Feasibility**: 2-3 hours implementation, minimal complexity

### Observability Principles

| Principle | Implementation |
|-----------|----------------|
| **Structured Logging** | JSON format with consistent schema |
| **Contextual Data** | Test name, browser, execution mode, retry count |
| **Machine-Readable** | Parseable by standard tooling (jq, Logstash, etc.) |
| **Low Overhead** | Async appenders, minimal performance impact |
| **Correlation IDs** | Timestamp + test name for trace correlation |
| **Failure Analysis** | Status + error messages captured |

---

## Consequences

### Positive Consequences

- **Visibility**: Clear metrics for every test execution
- **Debugging**: Quickly identify slow tests, flaky patterns, browser-specific issues
- **Trend Analysis**: Historical data for performance regression detection
- **CI Integration**: JSON logs consumable by GitHub Actions, Jenkins, etc.
- **Professionalism**: Demonstrates production-ready engineering practices
- **Scalability**: Foundation for future monitoring/alerting (Prometheus, Grafana)

### Negative Consequences (Trade-offs)

- **Mixed Logging**: Now have both Log4j2 (existing) and Logback (new)
  - **Mitigation**: Use SLF4J facade consistently, route Log4j2 through SLF4J bridge in future cleanup
- **Dependency Growth**: +3 new dependencies (slf4j-api, logback-classic, logstash-logback-encoder)
  - **Mitigation**: All dependencies are lightweight, industry-standard, well-maintained
- **Learning Curve**: Team needs to understand SLF4J/Logback configuration
  - **Mitigation**: Comprehensive documentation in OBSERVABILITY.md

### Risks and Mitigation

| Risk | Impact | Probability | Mitigation Strategy |
|------|--------|-------------|---------------------|
| Performance overhead from JSON serialization | Low | Low | Use async appenders, buffer writes, test execution time stays <5min |
| Log file growth | Medium | Medium | Logback rolling policies (size/time-based), retention 30 days |
| Dependency conflicts (SLF4J versions) | Low | Low | Maven enforcer plugin, consistent version management |

---

## Implementation Notes

### Technical Implementation

**1. Dependencies (pom.xml)**
```xml
<!-- SLF4J API (Facade) -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.16</version>
</dependency>

<!-- Logback Classic (Implementation) -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.16</version>
</dependency>

<!-- Logback JSON Encoder -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>8.0</version>
</dependency>
```

**2. TestMetrics.java Structure**
```java
package io.github.aslavchev.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class TestMetrics {
    private static final Logger metricsLogger = LoggerFactory.getLogger("TestMetrics");

    public static void logTestStart(String testName, String browser, String executionMode) {
        MDC.put("test_name", testName);
        MDC.put("browser", browser);
        MDC.put("execution_mode", executionMode);
        MDC.put("status", "STARTED");
        MDC.put("timestamp", Instant.now().toString());

        metricsLogger.info("Test started: {}", testName);
    }

    public static void logTestEnd(String testName, String status, long duration, int retryCount) {
        MDC.put("status", status);
        MDC.put("duration_ms", String.valueOf(duration));
        MDC.put("retry_count", String.valueOf(retryCount));

        metricsLogger.info("Test completed: {} - {} ({}ms)", testName, status, duration);
        MDC.clear();
    }
}
```

**3. Logback Configuration (src/test/resources/logback.xml)**
```xml
<configuration>
    <!-- Console: Human-readable for developers -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- File: JSON metrics for tooling -->
    <appender name="METRICS_JSON" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>target/test-metrics.json</file>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeMdcKeyName>test_name</includeMdcKeyName>
            <includeMdcKeyName>browser</includeMdcKeyName>
            <includeMdcKeyName>execution_mode</includeMdcKeyName>
            <includeMdcKeyName>status</includeMdcKeyName>
            <includeMdcKeyName>duration_ms</includeMdcKeyName>
            <includeMdcKeyName>retry_count</includeMdcKeyName>
            <includeMdcKeyName>timestamp</includeMdcKeyName>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>target/test-metrics-%d{yyyy-MM-dd}.%i.json</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <!-- TestMetrics logger: JSON only -->
    <logger name="TestMetrics" level="INFO" additivity="false">
        <appender-ref ref="METRICS_JSON" />
    </logger>

    <!-- Root logger: Console -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
</configuration>
```

**4. BaseTest Integration**
```java
@BeforeMethod
public void setUp(Method method, ITestContext context) {
    String testName = method.getName();
    String browser = System.getProperty("browser", "chrome");
    String executionMode = System.getProperty("execution", "local");

    testStartTime = System.currentTimeMillis();
    TestMetrics.logTestStart(testName, browser, executionMode);

    // ... existing WebDriver setup
}

@AfterMethod
public void tearDown(ITestResult result) {
    long duration = System.currentTimeMillis() - testStartTime;
    String status = result.getStatus() == ITestResult.SUCCESS ? "PASS" :
                    result.getStatus() == ITestResult.FAILURE ? "FAIL" : "SKIP";
    int retryCount = result.getAttribute("retryCount") != null ?
                     (int) result.getAttribute("retryCount") : 0;

    TestMetrics.logTestEnd(result.getName(), status, duration, retryCount);

    // ... existing teardown
}
```

### Sample JSON Output
```json
{
  "@timestamp": "2025-12-11T14:32:15.123Z",
  "level": "INFO",
  "logger_name": "TestMetrics",
  "message": "Test completed: testLoginWithValidCredentials - PASS (2340ms)",
  "test_name": "testLoginWithValidCredentials",
  "browser": "chrome",
  "execution_mode": "grid",
  "status": "PASS",
  "duration_ms": "2340",
  "retry_count": "0",
  "timestamp": "2025-12-11T14:32:15.123Z"
}
```

### Timeline
- **Decision Date**: 2025-12-11 (Phase 6.5 Part 2)
- **Implementation**: 2-3 hours
- **Documentation**: OBSERVABILITY.md guide
- **Validation**: CI run with metrics logging

### Related Decisions
- **ADR-011**: API Testing Strategy - REST Assured already uses SLF4J
- **Phase 6.5 Part 1**: Docker + Grid infrastructure benefits from distributed metrics
- **Future**: Potential integration with Prometheus/Grafana (Phase 7+)

---

## References

- [SLF4J Manual](https://www.slf4j.org/manual.html)
- [Logback Documentation](https://logback.qos.ch/documentation.html)
- [Logstash Logback Encoder](https://github.com/logfellow/logstash-logback-encoder)
- [Google SRE Workbook - Monitoring](https://sre.google/workbook/monitoring/)
- [MDC (Mapped Diagnostic Context)](https://www.slf4j.org/api/org/slf4j/MDC.html)

---

## Review and Updates

**Change Log**:

| Date | Change | Reason |
|------|--------|--------|
| 2025-12-11 | Initial decision | Phase 6.5 Part 2 - Observability & Metrics |

---

## Summary

**The Problem**:
With 23 tests running across Docker + Selenium Grid, no structured way to track test performance, identify slow tests, or analyze failure patterns. Manual analysis of Allure reports required. No machine-readable metrics for CI dashboards or trend analysis.

**The Options**:
1. Continue with Log4j2 only - No new dependencies but security history concerns and less flexible JSON support
2. Custom metrics framework - Full control over format but reinventing the wheel, more maintenance
3. OpenTelemetry - Industry-standard distributed tracing but massive complexity for 23-test framework
4. SLF4J + Logback - Industry-standard logging facade with native JSON support, right-sized for current scale

**The Choice**:
**Status: Evaluated - Not Implemented.** Decided Allure reports sufficient at current scale (23 tests). SLF4J + Logback structured logging documented for future implementation when scale demands it.

**Why This Matters**:
If implemented, JSON-formatted logs enable CI dashboards to parse metrics automatically, trend analysis scripts to detect performance regressions, and quick diagnosis of browser-specific issues in Grid environment.

**The Trade-off**:
Would introduce mixed logging (Log4j2 + Logback) and 3 new dependencies. For 23-test scale, Allure provides sufficient observability. Documented approach ready when project scales to warrant structured metrics infrastructure.

**Key Takeaway**:
"Evaluated structured logging with SLF4J + Logback for production-grade observability but chose not to implement at current 23-test scale—Allure reports sufficient, avoiding over-engineering while documenting scalability path for future growth."
