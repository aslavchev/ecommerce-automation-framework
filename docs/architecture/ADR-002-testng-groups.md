# ADR-002: TestNG Groups for Flexible Test Execution

**Status**: Accepted
**Date**: 2025-11-25

---

## Context

With 11 UI tests across multiple suites (Login, Products, Cart, Checkout), we needed flexible test execution to:
- Run fast feedback loops during development (smoke tests ~2 min)
- Execute comprehensive validation before releases (regression ~5-10 min)
- Identify critical business paths (login, checkout)
- Separate long-running E2E tests from quick feature tests
- Optimize CI/CD pipeline execution time

**Pain points without groups:**
- Running all tests (5-10 min) too slow for quick feedback
- No way to run only critical paths before deployment
- E2E tests (60-90s) slow down development workflow

---

## Decision

**Implement TestNG groups to categorize tests by purpose and execution speed.**

**Groups defined:**
- `smoke` - Fast sanity checks (2-3 tests, ~2 min)
- `regression` - Full test suite (all tests, ~5-10 min)
- `critical` - Business-critical paths (login, checkout)
- `e2e` - End-to-end user journeys
- `ui` - All Selenium tests (vs future `api` group)
- `slow` - Tests >30 seconds (excludable)

**Execution:**
```bash
mvn test -Dgroups=smoke                 # Quick feedback
mvn test -Dgroups=regression            # Full validation
mvn test -Dgroups=critical              # Pre-deployment
mvn test -DexcludedGroups=slow          # Fast tests only
```

---

## Rationale

### Why TestNG Groups?

1. **Native Feature**: No additional dependencies, works out-of-box
2. **Flexible Multi-Group Assignment**: Tests belong to multiple groups simultaneously
3. **Industry Standard**: Widely used in professional test automation
4. **CI/CD Friendly**: Easy Maven/Jenkins/GitHub Actions integration
5. **Scalable**: Simple to add new groups (e.g., `api`, `integration`)

### Example
```java
@Test(groups = {"smoke", "regression", "critical", "ui"})
public void testValidLogin() { ... }
```
This login test runs in smoke, regression, critical, and ui contexts.

---

## Alternatives Considered

**1. TestNG Groups (Chosen)**
- Native TestNG, flexible, standard practice
- Zero overhead, tests can have multiple groups
- **Selected**: Best balance of flexibility and simplicity

**2. JUnit 5 Tags**
- Modern but requires full framework migration
- Rejected: Too disruptive, TestNG working well

**3. Separate Suite XMLs**
- Clear separation but inflexible
- Tests can't easily belong to multiple suites
- Rejected: Doesn't scale with growing test suite

**4. Maven Profiles**
- Still needs mechanism to select tests
- Rejected: Complements groups but doesn't replace them

---

## Consequences

### Positive
✅ 60-80% faster developer feedback (smoke vs full suite)
✅ Optimized CI/CD (PR runs smoke+critical, nightly runs regression)
✅ Selective execution (exclude slow E2E during dev)
✅ Clear test purpose documentation
✅ Scalable for API tests

### Negative
⚠️ Requires discipline to tag tests consistently
⚠️ Groups must be documented
⚠️ Maintenance as test characteristics change

**Mitigation**: Document groups in README, add to PR checklist, periodic audits

---

## Implementation

```java
// Fast sanity checks
@Test(groups = {"smoke", "regression", "critical", "ui"})
public void testValidLogin() { ... }

// Full regression
@Test(groups = {"regression", "ui"})
public void testInvalidLogin() { ... }

// E2E journeys
@Test(groups = {"e2e", "regression", "critical", "ui", "slow"})
public void testPlaceOrderLogInBeforeCheckout() { ... }
```

**Maven Configuration:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <groups>${groups}</groups>
        <excludedGroups>${excludedGroups}</excludedGroups>
    </configuration>
</plugin>
```

---

## Group Selection Criteria

- **`smoke`**: <2 min runtime, happy path only
- **`critical`**: Revenue or security paths
- **`slow`**: >30 seconds execution time
- **`regression`**: All tests (default group)
- **`ui` / `api`**: Test type categorization

---

## References

- [TestNG Groups Documentation](https://testng.org/doc/documentation-main.html#test-groups)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/examples/testng.html)
