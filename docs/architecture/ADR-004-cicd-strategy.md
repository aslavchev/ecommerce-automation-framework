# ADR-004: CI/CD-First Automation Strategy

**Status**: Accepted
**Date**: 2025-11-10

---

## Context

After establishing the test framework foundation (BasePage, LoginPage, 2 passing tests), multiple paths were available:
1. Expand test coverage (add more page objects and tests)
2. Implement data-driven testing (TestNG DataProvider)
3. Implement CI/CD automation (GitHub Actions)
4. Add API testing (REST Assured)

**Key consideration**: All future tests would benefit from automated validation infrastructure established early.

---

## Decision

**Implement CI/CD integration early (Week 3) before expanding test coverage.**

Establish GitHub Actions pipeline with:
- Automated test execution on every push
- Headless browser configuration
- Allure report generation
- Credential management via environment variables

---

## Rationale

### Why CI/CD First?

**1. Automation-First Mindset**
- Production teams implement CI/CD early, don't wait for large test suites
- Demonstrates modern QA engineering practices
- Shift-left testing from the start

**2. Efficiency Multiplier**
- One-time setup (Week 3)
- Every future test automatically validated
- Faster development, earlier feedback
- Avoid retrofitting complexity later

**3. Strategic Timeline**
```
Week 3: CI/CD foundation (2 tests automated)
Week 4: Add 8 tests (all automated immediately)
Week 5: Data-driven testing (validated automatically)
Week 6: API testing (integrated into pipeline)
```

**vs Traditional Approach:**
```
Week 3: 10 tests (manual)
Week 4: 18 tests (manual)
Week 5: CI/CD retrofit (complexity, debugging)
Week 6: Still integrating API tests
```

**4. Production Practices**
- Clean environment validation (not just local)
- Headless browser forced early
- Environment variable management
- Test reliability validation

---

## Alternatives Considered

**1. Expand Coverage First**
- Add 8-10 tests before automation
- Pros: More visible progress, comfortable path
- Cons: Manual execution continues, harder to retrofit CI/CD
- Rejected: Doesn't demonstrate automation-first thinking

**2. Data-Driven Testing First**
- Implement TestNG DataProvider immediately
- Pros: Advanced TestNG skills, scalable data management
- Cons: Limited benefit with only 2 tests, still manual execution
- Rejected: More valuable after test suite grows

**3. API Testing First**
- Add REST Assured alongside UI tests
- Pros: Full-stack QA capability, test pyramid
- Cons: Compounds manual execution problem
- Rejected: More powerful when added to existing CI/CD

---

## Consequences

### Positive
✅ Automation infrastructure enables future velocity
✅ Every test automatically validated in clean environment
✅ Modern QA practices demonstrated
✅ Foundation for advanced features (parallel execution, cross-browser)
✅ Confidence in changes via automated validation
✅ Professional presentation (build badges, automated reports)

### Negative
⚠️ Initial time investment (4-5 days for setup)
⚠️ Learning curve (GitHub Actions, headless config, secrets)
⚠️ Small test count initially (only 2 tests in pipeline)

**Mitigation:**
- Time investment pays back immediately
- Skills learned are valuable for production work
- Small test count demonstrates iterative approach (strength, not weakness)

---

## Implementation

### GitHub Actions Workflow
```yaml
name: Test Automation
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
      - run: mvn clean test
      - uses: allure-framework/action@v2
        with:
          results: target/allure-results
```

### Headless Browser Configuration
```java
ChromeOptions options = new ChromeOptions();
if (System.getenv("CI") != null) {
    options.addArguments("--headless=new");
}
```

### Credentials Management
```properties
# .env (local, gitignored)
TEST_USER_EMAIL=test@example.com
TEST_USER_PASSWORD=password123

# GitHub Secrets (CI/CD)
TEST_USER_EMAIL
TEST_USER_PASSWORD
```

---

## Results

**Week 3 Deliverables:**
- ✅ GitHub Actions workflow executing
- ✅ 2 tests running on every push
- ✅ Allure reports generated
- ✅ Build status badges
- ✅ Headless browser working
- ✅ Environment-based credential management

**Future Scaling (Weeks 4-8):**
- Week 4: 10 tests (all automated)
- Week 5: Data-driven tests (validated automatically)
- Week 6: API tests (in same pipeline)
- Weeks 7-8: Parallel execution, cross-browser testing

---

## Design Principles

1. **Start Small, Validate, Scale**: Begin with 2 tests, ensure pipeline works, then expand
2. **Continuous Integration**: Frequent small changes, not batch releases
3. **Clean Environment**: Tests run in isolated CI environment
4. **Shift-Left**: Catch issues early via automated validation
5. **Infrastructure as Code**: Pipeline configuration version controlled

---

## References

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Selenium Headless Chrome](https://www.selenium.dev/blog/2023/headless-is-going-away/)
- [Test Pyramid - Martin Fowler](https://martinfowler.com/articles/practical-test-pyramid.html)
- [Shift-Left Testing Guide](https://www.browserstack.com/guide/what-is-shift-left-testing)
