# ADR-001: Page Object Model Pattern

**Status**: Accepted
**Date**: 2025-11-10

---

## Context

As the Selenium test suite grows, maintaining UI automation tests becomes challenging:
- **Locator duplication** across multiple test files
- **Brittle tests** that break when UI changes
- **Scattered wait logic** creating synchronization issues
- **Poor readability** mixing test logic with WebDriver API calls

Without a design pattern, tests become difficult to maintain, hard to read, and prone to flakiness.

---

## Decision

**Implement Page Object Model (POM) pattern with BasePage abstraction.**

**Architecture:**
```
src/main/java/pages/
├── BasePage.java          # Common waits & interactions
├── LoginPage.java         # Login page object
└── [Other pages...]

src/test/java/
└── LoginTests.java        # Tests using page objects
```

---

## Rationale

### Why POM?

1. **Maintainability**: Locators defined once in page objects. UI changes only require updates to page object class.
2. **Readability**: Test code reads like business logic: `loginPage.login(email, password)`
3. **Reusability**: Page objects shared across multiple tests. Common interactions centralized.
4. **Consistency**: Standardized wait strategies prevent flaky tests.

### Why BasePage Abstraction?

Centralizes common functionality:
- Wait strategies (`waitForElementVisible`, `waitForElementClickable`)
- Common actions (`click`, `type`, `getText`)
- Consistent timeout configuration
- Protection against `NoSuchElementException`

---

## Alternatives Considered

**1. No Pattern (Raw Selenium)**
- Simple to start but creates massive code duplication
- Tests break easily, difficult to maintain
- Rejected: Creates technical debt immediately

**2. Page Factory (@FindBy)**
- Selenium's built-in pattern with annotations
- Lazy initialization causes unexpected behavior
- Rejected: POM without Page Factory is more flexible

**3. Screenplay Pattern**
- Advanced actor-based interactions (Serenity BDD)
- Excellent for BDD but significant learning curve
- Rejected: Over-engineering for project scope

---

## Consequences

### Positive
✅ Reduced code duplication (~70% less code)
✅ UI changes isolated to page objects
✅ Tests describe behavior, not mechanics
✅ Consistent waits eliminate flakiness
✅ Faster test development

### Negative
⚠️ Initial overhead (more files upfront)
⚠️ Team learning curve for POM pattern
⚠️ Additional abstraction layer

**Mitigation**: Well-documented examples, templates reduce boilerplate

---

## Implementation Example

### Before (No POM)
```java
// Difficult to read, duplicated, brittle
driver.findElement(By.id("email")).sendKeys("test@test.com");
driver.findElement(By.id("password")).sendKeys("password");
driver.findElement(By.id("login-btn")).click();
Thread.sleep(2000); // Bad practice
```

### After (With POM)
```java
// Readable, maintainable, reusable
loginPage.login("test@test.com", "password");
assertTrue(loginPage.isLoggedIn());
```

---

## Design Principles

- **Dependency Injection**: WebDriver passed via constructor
- **Encapsulation**: Locators private to page objects
- **DRY**: Wait logic in BasePage
- **Single Responsibility**: One page object per page/component
- **Fluent Interface**: Method chaining for readability

---

## References

- [Selenium POM Documentation](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)
- [Martin Fowler - Page Object](https://martinfowler.com/bliki/PageObject.html)

---

## 🧒 ELI5 (Explain Like I'm 5)

**The Problem**:
Tests had the same locators copied everywhere, broke easily when the UI changed, and mixed test logic with browser commands making them unreadable.

**The Options**:
1. No pattern (raw Selenium) - Simple to start but creates immediate technical debt through duplication
2. Page Factory (@FindBy) - Selenium's built-in pattern but lazy initialization causes unexpected behavior
3. Screenplay Pattern - Advanced actor-based approach but over-engineering for a 25-test portfolio
4. Page Object Model with BasePage - Industry standard with centralized wait logic

**The Choice**:
Page Object Model with BasePage abstraction to centralize locators and wait strategies.

**Why This Matters**:
When UI changes, update one page object file instead of hunting through dozens of test files. Team can read tests as business logic, not WebDriver commands.

**The Trade-off**:
More files upfront (extra abstraction layer) and team learning curve. We accepted this because 70% code reduction and flakiness elimination outweighed initial setup cost.

**Key Takeaway**:
"Chose Page Object Model over raw Selenium because centralizing locators and wait logic reduced code duplication by 70% and eliminated synchronization flakiness—worth the upfront abstraction overhead for long-term maintainability."
