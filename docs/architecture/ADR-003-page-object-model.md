# ADR-003: Page Object Model Pattern with BasePage Abstraction

**Status**: Accepted
**Date**: November 10, 2025
**Decision Maker**: Alex Slavchev
**Context**: Test code architecture decision for maintaining UI automation tests

---

## Context

The project requires a maintainable approach for organizing Selenium test code. As the test suite grows, key challenges emerge:

- **Locator Duplication**: Multiple tests interacting with same UI elements leads to duplicated selectors
- **Brittle Tests**: UI changes force updates across multiple test files
- **Synchronization Issues**: Explicit waits scattered throughout test code creates maintenance burden
- **Code Readability**: Test logic obscured by WebDriver API calls
- **Scaling Problems**: Adding new tests becomes exponentially harder without structure

Without a design pattern, tests become:
- Difficult to maintain (locator changes ripple across many files)
- Hard to read (mixing test logic with WebDriver mechanics)
- Prone to flakiness (inconsistent wait strategies)
- Slow to extend (code duplication slows development)

**Constraints**:
- Must work with Selenium WebDriver 4
- Must support test independence (tests run in any order)
- Must minimize code duplication (DRY principle)
- Must be simple enough for team onboarding

---

## Decision

**Implement Page Object Model (POM) pattern with a BasePage abstraction layer.**

**Key Components**:
1. **BasePage**: Abstract class containing common wait strategies and UI interactions
2. **Page Objects**: Classes representing each page/component (e.g., LoginPage extends BasePage)
3. **Test Classes**: Focus on test logic only, delegate UI interactions to Page Objects

---

## Alternatives Considered

### Option 1: No Pattern (Raw Selenium in Tests)
- **Description**: Write WebDriver code directly in test methods
- **Pros**:
  - Simple to start
  - No additional abstractions to learn
  - Everything in one place
- **Cons**:
  - Massive code duplication
  - Tests break easily with UI changes
  - Difficult to read and maintain
  - No separation of concerns
  - Scales poorly beyond 10-15 tests
- **Why rejected**: Creates technical debt immediately. Tests become unmaintainable as suite grows.

### Option 2: Page Factory (Selenium's @FindBy)
- **Description**: Selenium's built-in Page Factory pattern using annotations
- **Pros**:
  - Official Selenium pattern
  - Declarative locator definitions
  - Automatic element initialization
- **Cons**:
  - Lazy initialization can cause unexpected behavior
  - Limited flexibility (hard to handle dynamic elements)
  - Deprecated in some Selenium documentation
  - More complex debugging
  - Slower element lookup in practice
- **Why rejected**: POM without Page Factory is more flexible and actively recommended by Selenium maintainers.

### Option 3: Screenplay Pattern (Serenity BDD)
- **Description**: Advanced pattern focusing on actor-based interactions
- **Pros**:
  - Excellent for BDD scenarios
  - Very readable test code
  - Strong separation of concerns
- **Cons**:
  - Significant learning curve
  - Requires additional framework (Serenity BDD)
  - Overkill for simple UI test automation
  - More boilerplate code
  - Harder to debug
- **Why rejected**: Over-engineering for project scope. POM provides sufficient maintainability without complexity.

---

## Rationale

### Primary Reasons

1. **Maintainability**:
   - Locators defined once in page objects
   - UI changes only require updates to page object class
   - Tests remain stable when UI changes
   - Easier to refactor and optimize

2. **Readability**:
   - Test code reads like business logic: `loginPage.login(email, password)`
   - WebDriver mechanics hidden in page objects
   - New team members understand tests faster
   - Clearer test intent

3. **Reusability**:
   - Page objects used across multiple tests
   - Common interactions centralized in BasePage
   - Wait strategies shared across all page objects
   - Reduced code duplication (~70% less code)

4. **Test Independence**:
   - Each test instantiates own page objects
   - No shared state between tests
   - Tests can run in parallel safely
   - Easier debugging (isolated failures)

### BasePage Abstraction Benefits

**Centralizes**:
- Wait strategies (`waitForElementVisible`, `waitForElementClickable`)
- Common interactions (`click`, `type`, `getText`)
- Navigation helpers (`navigateTo`, `getCurrentUrl`)
- Error handling (graceful degradation)

**Provides**:
- Consistent timeout configuration (single source of truth)
- Protection against `NoSuchElementException`
- Protection against `ElementClickInterceptedException`
- Reusable methods for all page objects

---

## Consequences

### Positive Consequences

- ✅ **Reduced Duplication**: Locators and actions defined once
- ✅ **Easier Maintenance**: UI changes isolated to page objects
- ✅ **Better Readability**: Tests describe behavior, not mechanics
- ✅ **Consistent Waits**: No Thread.sleep, predictable synchronization
- ✅ **Faster Development**: New tests leverage existing page objects
- ✅ **Simpler Debugging**: Failures isolated to specific page objects

### Negative Consequences

- ⚠️ **Initial Overhead**: More files to create upfront
  - **Mitigation**: Templates and BasePage reduce boilerplate
- ⚠️ **Learning Curve**: Team must understand POM pattern
  - **Mitigation**: Well-documented code with clear examples
- ⚠️ **Abstraction Cost**: Additional layer between tests and WebDriver
  - **Mitigation**: Abstraction is minimal and provides clear value

### Risks and Mitigation

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| Over-abstraction | Medium | Low | Keep page objects focused on UI interactions only |
| Inconsistent usage | Medium | Medium | Code review standards, clear examples |
| Page objects too complex | High | Low | Single Responsibility Principle - one page object per page/component |

---

## Implementation

### Structure

```
src/
├── main/java/io/github/aslavchev/pages/
│   ├── BasePage.java          # Common wait strategies and interactions
│   ├── LoginPage.java          # Login page object
│   └── [Future page objects]
├── test/java/io/github/aslavchev/
│   ├── base/
│   │   └── BaseTest.java       # WebDriver lifecycle
│   └── LoginTests.java         # Test cases using page objects
```

### BasePage Responsibilities

**Wait Strategies**:
- `waitForElementVisible(By locator)` - Waits until element is visible
- `waitForElementClickable(By locator)` - Waits until element is clickable

**Common Actions** (with built-in waits):
- `click(By locator)` - Click element with wait
- `type(By locator, String text)` - Type text with wait
- `getText(By locator)` - Get element text with wait

**Safe Checks**:
- `isElementDisplayed(By locator)` - Returns false if not found (no exception)

**Navigation Helpers**:
- `navigateTo(String url)`
- `getCurrentUrl()`
- `getPageTitle()`

### Page Object Structure

Page objects (e.g., LoginPage) extend BasePage and provide:
- **Private locators**: Encapsulated, hidden from tests
- **Public methods**: Test-facing API (login, isLoggedIn, getErrorMessage)
- **Wait helpers**: Synchronization methods (waitForLoginSuccess)

### Test Code Pattern

**Before (No POM)**:
```java
// Difficult to read, duplicated code, brittle
driver.findElement(By.id("email")).sendKeys("test@test.com");
driver.findElement(By.id("password")).sendKeys("password");
driver.findElement(By.id("login-btn")).click();
Thread.sleep(2000); // Bad practice
```

**After (With POM)**:
```java
// Readable, maintainable, reusable
loginPage.login("test@test.com", "password");
loginPage.waitForLoginSuccess();
assertTrue(loginPage.isLoggedIn());
```

### Design Principles Applied

1. **Dependency Injection**: WebDriver passed to page objects via constructor
2. **Encapsulation**: Locators private to page objects
3. **DRY**: Wait logic in BasePage, not duplicated in tests
4. **Single Responsibility**: Each page object handles one page/component
5. **Method Naming**: Verbs for actions (login, click), nouns for getters (getErrorMessage)

See repository code (src/main/java/pages/ and src/test/java/) for full implementation.

---

## Lessons Learned

**What Worked Well**:
- BasePage abstraction eliminated 90% of wait-related code duplication
- Locator encapsulation made tests resilient to UI changes
- Test code is significantly more readable
- Adding new page objects is fast (extend BasePage, define locators)

**Challenges Encountered**:
- Initial setup took longer than raw Selenium approach
- Team needed brief explanation of POM pattern
- Deciding granularity of page objects (one per page vs. per component)

**Improvements for Future**:
- Consider Page Components pattern for reusable UI elements (nav bar, footer)
- Evaluate method chaining for fluent interface (trade-off: complexity vs readability)
- Add utility methods to BasePage as patterns emerge

---

## Related Decisions

- **ADR-001**: Selenium Framework Selection → Established need for maintainable test code
- **ADR-002**: TestNG vs. JUnit → POM works with both frameworks
- **ADR-008**: Removed Allure severity annotations → Simplified page object annotations

---

## References

- [Selenium POM Documentation](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)
- [Martin Fowler - Page Object](https://martinfowler.com/bliki/PageObject.html)
- [Selenium WebDriver Best Practices](https://www.selenium.dev/documentation/test_practices/)

---

## Change Log

| Date | Change | Reason |
|------|--------|--------|
| November 7, 2025 | Initial decision | Test architecture pattern selection |
| November 10, 2025 | Implementation complete | BasePage and LoginPage operational |
| November 10, 2025 | Documented lessons learned | Captured real implementation experience |
