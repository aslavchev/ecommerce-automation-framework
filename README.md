# E-Commerce Test Automation Framework

[![Tests Status](https://github.com/aslavchev/ecommerce-automation-framework/actions/workflows/allure-report.yml/badge.svg)](https://github.com/aslavchev/ecommerce-automation-framework/actions)
[![Live Report](https://img.shields.io/badge/📊_Allure_Report-View_Live-blue?style=flat&logo=github)](https://aslavchev.github.io/ecommerce-automation-framework/)
![Tests](https://img.shields.io/badge/tests-23_passing-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.38.0-green)
![REST Assured](https://img.shields.io/badge/REST_Assured-5.5.6-blue)

**Strategic test automation framework for e-commerce testing demonstrating production-ready automation practices.**

📊 **[View Live Allure Report →](https://aslavchev.github.io/ecommerce-automation-framework/)**

---

## 🎯 Overview

This framework demonstrates comprehensive test automation for e-commerce applications using modern tools and best practices:

- **Test Strategy**: Risk-based test selection following Test Pyramid principles
- **Architecture**: Page Object Model for maintainability and scalability
- **Tech Stack**: Java 21, Selenium 4, TestNG, REST Assured
- **DevOps**: GitHub Actions CI/CD integration with automated test execution

Built by a QA professional with 18 years of testing expertise, this framework combines strategic test planning with modern automation implementation.

---

## 🚀 Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 21 |
| **Build Tool** | Maven | 3.9+ |
| **UI Automation** | Selenium WebDriver | 4.38.0 |
| **Test Framework** | TestNG | 7.11.0 |
| **API Testing** | REST Assured | 5.5.6 |
| **Reporting** | Allure Report | 2.29.0 |
| **Driver Management** | WebDriverManager | 6.3.2 |
| **CI/CD** | GitHub Actions | ✅ Active |
| **Containerization** | Docker | Planned |

---



## 📁 Project Structure
```
ecommerce-automation-framework/
  ├── src/
  │   ├── main/java/io/github/aslavchev/
  │   │   └── ui/
  │   │       └── pages/          # UI Page Objects
  │   │           ├── BasePage.java
  │   │           ├── LoginPage.java
  │   │           ├── ProductsPage.java, ProductDetailsPage.java
  │   │           ├── CartPage.java
  │   │           └── CheckoutPage.java, PaymentPage.java
  │   └── test/java/io/github/aslavchev/
  │       ├── ui/                 # UI tests (11 tests)
  │       │   ├── LoginTests.java
  │       │   ├── ProductTests.java
  │       │   ├── CartTests.java
  │       │   └── CheckoutTests.java
  │       ├── api/                # API tests (12 tests)
  │       │   ├── base/BaseAPITest.java
  │       │   ├── ProductAPITests.java
  │       │   ├── SearchAPITests.java
  │       │   ├── AuthAPITests.java
  │       │   └── APIHelper.java
  │       ├── base/BaseTest.java
  │       ├── utils/TestConfig.java
  │       └── dataproviders/ProductDataProvider.java
  ├── .github/workflows/          # CI/CD pipelines
  └── docs/                       # Documentation
      └── architecture/           # Architecture Decision Records
```

---

## 🎯 Test Strategy

**Approach**: Risk-based test automation following Test Pyramid principles

**Distribution**:
- **60% API Tests**: Fast, reliable validation of business logic
- **40% UI Tests**: Critical user-facing workflows

**Focus Areas**:
- User registration and authentication
- Product search and catalog browsing
- Shopping cart management
- Checkout process (end-to-end critical path)
- API endpoint validation

**Intentionally Manual** (documented in test strategy):
- Visual/UI design validation
- Exploratory testing
- Usability assessments

---

## ⚙️ Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.9 or higher
- Git

### Installation

```bash
# Clone the repository
git clone https://github.com/aslavchev/ecommerce-automation-framework.git
cd ecommerce-automation-framework

# Install dependencies
mvn clean install

# Run tests
mvn test
```

---

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run by TestNG group (recommended)
mvn test -Dgroups=smoke                 # Fast sanity checks (~2 min)
mvn test -Dgroups=regression            # Full test suite (~5-10 min)
mvn test -Dgroups=critical              # Critical business paths only
mvn test -Dgroups=e2e                   # End-to-end user journeys

# Run specific test class
mvn test -Dtest=LoginTests
mvn test -Dtest=e2e.CheckoutTests

# Generate Allure report
mvn clean test && mvn allure:serve
```

### Test Organization

Tests are organized using **TestNG groups** for flexible execution:

| Group | Purpose | Tests | Run Time |
|-------|---------|-------|----------|
| `smoke` | Fast sanity checks | Login, Product navigation | ~2 min |
| `regression` | Full test suite | All 23 tests | ~5-10 min |
| `critical` | Business-critical paths | Login (valid), E2E Checkout | ~3 min |
| `e2e` | Complete user journeys | Checkout flow | ~1 min |
| `ui` | All UI tests | All current tests | ~5-10 min |


### Data-Driven Testing
Tests use TestNG DataProvider with CSV files for easy test data management:

```bash
# CartTests runs 3 scenarios from ProductDataProvider
mvn test -Dtest=CartTests#addMultipleProductsToCartAndVerify

# CheckoutTests runs 2 scenarios from checkout-payment.csv
mvn test -Dtest=e2e.CheckoutTests#placeOrderWithLoginBeforeCheckout

# Add new test scenarios by editing CSV files (no code changes needed)
# src/test/resources/testdata/products.csv
# src/test/resources/testdata/checkout-payment.csv

---

## 📊 Test Reports

Test execution reports are generated in `target/surefire-reports/` after each test run.

Allure Reports (interactive HTML) can be generated and viewed with:
```bash
mvn allure:serve
```

Allure results are automatically generated at `target/allure-results/` after each test execution.

---

## 🏗️ Framework Design

### Page Object Model

All UI interactions are encapsulated in Page Object classes following the Single Responsibility Principle:

```java
package io.github.aslavchev.pages;

public class LoginPage {
    // Locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");

    // Actions
    public void loginAs(String username, String password) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        clickLoginButton();
    }
}
```

### API Testing

REST Assured is used for API validation with request/response models:

```java
package io.github.aslavchev.api;

@Test
public void testUserRegistration() {
    given()
        .contentType("application/json")
        .body(userRequest)
    .when()
        .post("/api/users")
    .then()
        .statusCode(201)
        .body("email", equalTo(userRequest.getEmail()));
}
```

---

## 📚 Documentation

- **[Architecture Decisions](docs/architecture/)**: ADRs documenting key technical choices
- **[Test Strategy](docs/test-strategy.md)**: Comprehensive test approach and rationale (planned)
- **[Setup Guide](docs/setup.md)**: Detailed environment setup (planned)

---

## 🎓 Learning & Development

This framework was built as part of a transition from manual QA (18 years) to test automation, demonstrating:

- Strategic test planning and risk-based prioritization
- Modern automation tools and best practices
- Production-ready framework architecture
- CI/CD integration and DevOps practices
- Comprehensive documentation and decision records

---

## 📈 Development Roadmap

### ✅ **Phase 1: Foundation (Weeks 1-2) - COMPLETE**
- [x] Maven project setup with modern tech stack
- [x] Page Object Model foundation (BaseTest, BasePage)
- [x] First page object implementation (LoginPage)
- [x] Login test suite (2 tests passing, 100% pass rate)
- [x] Allure reporting configured and generating
- [x] Architecture Decision Records (ADR-001, ADR-003)

### ✅ **Phase 2: CI/CD Infrastructure (Week 3) - COMPLETE**
- [x] GitHub Actions workflow for automated test execution
- [x] Headless browser configuration
- [x] Allure report publishing to GitHub Pages
- [x] Environment variable configuration for credentials
- [x] Build status badges

### ✅ **Phase 3: Test Coverage Expansion (Weeks 4-5) - COMPLETE**
- [x] ProductsPage, ProductDetailsPage objects (Journey 1: Product Discovery ✅)
- [x] 4 test scenarios covering product browsing (Test Cases 8, 9, 18, 21)
- [x] CartPage object and 3 cart management tests (Test Cases 12, 13, 17)
- [x] CheckoutPage, PaymentPage, OrderConfirmationPage objects
- [x] End-to-end checkout flow test (Test Case 16)
- [x] TestNG groups for flexible test execution (smoke, regression, critical, e2e)
- [x] TestConfig utility for secure credential management
- [x] **10 UI tests passing** (9 feature + 1 E2E journey)

### ✅ **Phase 4: Data-Driven Testing (Week 6) - COMPLETE**
- [x] TestNG DataProvider implementation for CartTests and CheckoutTests
- [x] CSV-based test data management with centralized ProductDataProvider
- [x] Parameterized test scenarios (+200% test coverage without code duplication)
- [x] TestDataReader utility with data enrichment pattern
- [x] Shared product catalog (products.csv) reusable across test classes
- [x] **11 UI tests passing** (5 data-driven scenarios)

### ✅ **Phase 5: API Testing Integration (Week 7) - COMPLETE**
- [x] REST Assured framework setup with BaseAPITest
- [x] APIHelper utility class for reusable API methods
- [x] 12 API tests (Products, Search, Authentication)
- [x] Data-driven API tests with CSV (5 scenarios)
- [x] Generic TestDataReader for simple CSV files
- [x] **12 API tests passing** (8 standard + 4 data-driven)

### ✅ **Phase 6: Test Reliability (Week 8) - COMPLETE**
**Focus**: Production-grade reliability patterns and cross-browser support

- [x] **Retry Mechanism**
  - TestNG IRetryAnalyzer implementation (max 2 retries)
  - RetryListener for automatic retry application and flakiness tracking
  - Retry strategy documentation (`docs/test-reliability/retry-strategy.md`)
- [x] **Cross-Browser Testing**
  - Firefox WebDriver support (alongside Chrome)
  - System property: `-Dbrowser=chrome/firefox`
  - CI matrix: parallel browser execution (fail-fast disabled)
- [ ] **Test Stability Documentation** *(Deferred to Phase 7)*
  - Test stability patterns guide
  - Flaky test debugging strategies

**Goal**: Demonstrate production reliability engineering for enterprise QA roles ✅

---

### **Phase 6.5: Infrastructure Modernization (Week 8.5) - PLANNED**
**Focus**: Containerization, observability, and strategic tech decisions *(FAANG infrastructure signal)*

- [ ] **Docker + Selenium Grid** ⭐ *Critical for infrastructure-as-code demonstration*
  - `docker-compose.yml`: Selenium Hub + Chrome/Firefox nodes
  - Updated `BaseTest.java`: `-Dexecution=local/grid` support
  - Containerized test execution in CI
  - `docs/DOCKER-SETUP.md`: Setup guide with architecture diagram

- [ ] **Observability & Metrics**
  - `TestMetrics.java`: JSON logging (test name, status, duration, browser, timestamp)
  - SLF4J + Logback structured logging configuration
  - `docs/OBSERVABILITY.md`: Metrics strategy and use cases
  - Production-grade logging for debugging and trend analysis

- [ ] **Tech Stack Rationale** ⭐ *Demonstrates data-driven decision making*
  - `docs/TECH-STACK-RATIONALE.md`:
    - Why TestNG over JUnit 5? (Job market: 5,700 Selenium+Java+TestNG roles)
    - Why Selenium over Playwright? (Market demand data: 42,000 vs 3,200 jobs)
    - Honest trade-off analysis and strategic thinking

**Goal**: Demonstrate infrastructure thinking and data-driven tech decisions for Senior/Staff SDET roles

---

### **Phase 7: Performance & Scale (Week 9) - PLANNED**
**Focus**: Performance engineering, scalability proof, and microservices awareness *(FAANG differentiator)*

- [ ] **Parallel Execution** ⭐ *Critical for Senior SDET roles*
  - TestNG `parallel="methods"` configuration
  - Thread-safe test design audit
  - Performance metrics: Sequential vs. Parallel execution time
  - Document proof: "23 tests: 8min → 3min with parallelization"

- [ ] **Contract Testing** ⭐ *New - Critical for microservices companies*
  - `ProductContractTest.java`: 2-3 Pact consumer contract examples
  - `docs/CONTRACT-TESTING.md`: Consumer-driven contracts explained
  - Demonstrates microservices testing awareness (Google/Amazon/Shopify requirement)

- [ ] **API Performance Testing**
  - `APIPerformanceTest.java`: Response time assertions (<500ms)
  - Basic load testing (5-10 concurrent requests)
  - Performance SLA validation

- [ ] **CI Optimization**
  - GitHub Actions matrix: Chrome & Firefox parallel jobs
  - Execution time reduction evidence in CI logs

- [ ] **Scalability Documentation**
  - `docs/PERFORMANCE-ENGINEERING.md`: Actual metrics with scalability projections
  - README sections: "Scaling Beyond 100 Tests", "Infrastructure Strategy"
  - Docker/Grid execution strategy for production scale

**Goal**: Concrete performance engineering proof + microservices awareness for Senior/Staff SDET roles

---

### **Phase 8: Portfolio Polish (Week 10) - PLANNED**
**Focus**: Interview-ready presentation and strategic positioning

- [ ] **README Enhancement**
  - Add "Infrastructure" section (Docker/Grid architecture with screenshots)
  - Add "Performance Engineering" section with concrete metrics
  - Add "Scalability Strategy" section with parallel execution proof
  - Add "Contract Testing" section (microservices awareness)
  - Add "Test Reliability" section with retry patterns

- [ ] **Strategic Documentation**
  - `docs/MODERN-PATTERNS.md`:
    - Contract testing ✅ (implemented)
    - Observability ✅ (implemented)
    - Visual regression awareness 📋 (future consideration)
    - Accessibility testing awareness 📋 (future consideration)
    - AI-assisted testing awareness 📋 (future consideration)
  - `docs/FUTURE-MODERNIZATION.md`:
    - Playwright evaluation (when to migrate)
    - JUnit 5 migration path
    - Kubernetes test execution strategy
    - Shows strategic thinking beyond current implementation

- [ ] **Code Quality Audit**
  - Remove dead/unused code
  - Ensure consistent naming conventions
  - JavaDoc for public methods
  - Final Allure report screenshot refresh
  - Performance baseline documentation

**Goal**: Portfolio optimized for Senior/Staff SDET interviews at FAANG companies

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**[Alex Slavchev]**
- GitHub: [@aslavchev](https://github.com/aslavchev)
- LinkedIn: [aslavchev](https://www.linkedin.com/in/aslavchev/)

---

## 🙏 Acknowledgments

Built using industry-standard tools and following best practices from the testing and automation community.

Special thanks to the open-source community for excellent tools like Selenium, TestNG, and REST Assured.

---

**Status**: ✅ Phase 5 Complete (Week 7) | 🚧 Phase 6: Test Reliability (Week 8)

**Last Updated**: December 4, 2025

**Current State**: 23 tests passing (11 UI + 12 API) | REST Assured framework | Data-driven testing
