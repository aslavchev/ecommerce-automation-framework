# E-Commerce Test Automation Framework

A test automation framework built on architectural discipline: stability over speed, judgment over coverage theater, documented decisions over tribal knowledge.

**Built for reliability**: Eliminated 35% test flakiness through sequential execution strategy. 100% pass rate across 25 tests, 100+ consecutive green builds in CI.

[![Tests](https://github.com/aslavchev/ecommerce-automation-framework/actions/workflows/allure-report.yml/badge.svg)](https://github.com/aslavchev/ecommerce-automation-framework/actions)
[![Live Report](https://img.shields.io/badge/📊_Allure_Report-Live-blue)](https://aslavchev.github.io/ecommerce-automation-framework/)
![Tests](https://img.shields.io/badge/tests-25_passing-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.38-green)

---

## Why This Framework Exists

Most test automation frameworks optimize for coverage and speed, then struggle with reliability. This framework inverts that priority: **stability and trust come first**.

The challenge: test flakiness erodes CI/CD confidence. Teams waste hours investigating false failures, or worse, ignore legitimate failures because "tests are always flaky."

This framework solves that through architectural discipline—documented decisions with clear trade-offs, not best practices cargo-culted from tutorials.

---

## Engineering Philosophy

**Core Principles** ([9 Architecture Decision Records](docs/architecture/))

1. **Stability > Speed**
   *Trade-off*: Sequential execution adds 2 minutes to builds, eliminates flakiness entirely
   *Result*: 35% flake rate → 0% (100+ consecutive green builds)

2. **Documented Decisions > Tribal Knowledge**
   *Practice*: Every architectural choice has an [ADR](docs/architecture/) with plain-language summary and trade-off analysis

3. **Simplicity > Enterprise Patterns**
   *Example*: CSV data-driven testing over database ([ADR-003](docs/architecture/ADR-003-data-driven-testing.md)) - QA team can edit scenarios in Excel

4. **Judgment > Automation Theater**
   *Strategy*: 25 high-value tests covering critical user journeys, not 200 tests for a coverage metric

---

## Key Technical Decisions

### Decision 1: Sequential Execution for Stability
**Problem**: 35% test flake rate eroded CI/CD trust
**Choice**: Single-threaded execution over parallel speed
**Trade-off**: +2 min build time for 100% reliability
**Impact**: Zero false alarms, team trusts test results
📖 [ADR-004: CI/CD-First Strategy](docs/architecture/ADR-004-cicd-strategy.md)

### Decision 2: Page Object Model with BasePage
**Problem**: Locator duplication, brittle tests breaking on UI changes
**Choice**: Page Object Model with centralized wait strategies
**Trade-off**: More files upfront for 70% code reduction long-term
**Impact**: UI changes update one file, not dozens of tests
📖 [ADR-001: Page Object Model](docs/architecture/ADR-001-page-object-model.md)

### Decision 3: CSV Data-Driven Testing
**Problem**: Hard-coded test data required developer changes for new scenarios
**Choice**: CSV files with centralized product catalog
**Trade-off**: Runtime validation vs. QA team autonomy
**Impact**: 200% scenario growth through Excel edits, zero code changes
📖 [ADR-003: CSV-Based Data-Driven Testing](docs/architecture/ADR-003-data-driven-testing.md)

### Decision 4: TestNG Groups for Flexible Execution
**Problem**: Running all tests took 10 minutes—too slow for quick feedback
**Choice**: Categorize tests by purpose (smoke, regression, critical) and speed
**Trade-off**: Tagging discipline required
**Impact**: Developer feedback reduced from 10 minutes to 2 minutes
📖 [ADR-002: TestNG Groups](docs/architecture/ADR-002-testng-groups.md)

**See All Decisions**: [ADR Index](docs/architecture/)

---

## Architecture

### System Design
```
┌─────────────────────────────────────────┐
│ GitHub Actions CI/CD                    │
│  ├─ Sequential Execution (stability)    │
│  ├─ Cross-browser (Chrome/Firefox)      │
│  └─ Retry mechanism + flakiness tracker │
└─────────────────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│ Docker + Selenium Grid                  │
│  ├─ Isolated test environment           │
│  └─ Reproducible local/CI parity        │
└─────────────────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│ Test Suite (25 tests)                   │
│  ├─ 11 UI (Page Object Model)           │
│  ├─ 12 API (REST Assured)               │
│  └─ 2 Data-Driven (CSV parameterized)   │
└─────────────────────────────────────────┘
           ↓
┌─────────────────────────────────────────┐
│ Reporting & Observability               │
│  ├─ Allure (interactive HTML reports)   │
│  └─ Retry tracking (production pattern) │
└─────────────────────────────────────────┘
```

### Technologies Used
**Test Frameworks**: Selenium WebDriver 4.38, TestNG 7.11, REST Assured 5.5.6
**Language**: Java 21
**Build**: Maven
**CI/CD**: GitHub Actions
**Infrastructure**: Docker, Selenium Grid
**Reporting**: Allure 2.29

**Target Application**: [AutomationExercise.com](https://automationexercise.com)

---

## Metrics That Matter

| Metric | Current | Context |
|--------|---------|---------|
| Test Pass Rate | 100% | (stable across 100+ consecutive builds) |
| Flakiness Rate | 0% | (was 35% before sequential execution) |
| Build Time | 6 min | (trade-off: +2 min for stability) |
| Test Coverage | 25 tests | (11 UI, 12 API, 2 data-driven) |
| Feedback Loop | 2 min | (smoke tests) vs. 10 min (full regression) |
| Browser Coverage | 2 browsers | (Chrome, Firefox validated in CI) |

**Philosophy**: We measure stability and trust, not just coverage percentages.

**Live Proof**: [View Allure Report →](https://aslavchev.github.io/ecommerce-automation-framework/)

---

## Quick Start

### Prerequisites
- Java 21+
- Docker
- Maven 3.8+

### Run Tests Locally
```bash
# Clone repo
git clone https://github.com/aslavchev/ecommerce-automation-framework.git
cd ecommerce-automation-framework

# Start Selenium Grid
docker-compose up -d

# Run all tests
mvn clean test

# Run specific groups
mvn test -Dgroups=smoke       # 2min - fast sanity
mvn test -Dgroups=regression  # 6min - full suite
mvn test -Dgroups=api         # API tests only

# Run with Docker Grid
mvn test -Dexecution=grid

# View Allure report
mvn allure:serve
```

### Run in CI
Tests run automatically on every push/PR via GitHub Actions.
See: `.github/workflows/allure-report.yml`

---

## Test Organization

```bash
# By Speed
mvn test -Dgroups=smoke       # Fast sanity checks (2 min)
mvn test -Dgroups=regression  # Full suite (6 min)
mvn test -Dgroups=critical    # Business-critical paths only

# By Type
mvn test -Dgroups=ui          # UI tests only
mvn test -Dgroups=api         # API tests only
mvn test -Dgroups=e2e         # End-to-end user journeys

# By Browser
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox

# Docker Grid with VNC debugging
docker-compose up -d
# VNC: localhost:5900 (Chrome), localhost:5901 (Firefox), password: secret
mvn test -Dexecution=grid -Dgrid.url=http://localhost:4444
```

---

## Project Structure

```
src/
├── main/java/io/github/aslavchev/
│   ├── ui/pages/              # 8 Page Objects (Login, Products, Cart, Checkout)
│   └── api/                   # APIHelper + REST Assured models
└── test/java/io/github/aslavchev/
    ├── ui/                    # UI tests + BaseTest
    ├── api/                   # API tests + BaseAPITest
    ├── data/                  # DataProviders + CSV files
    ├── listeners/             # RetryListener (flakiness tracking)
    └── utils/                 # TestConfig, RetryAnalyzer

docker-compose.yml             # Selenium Hub + Chrome/Firefox nodes
docs/
├── architecture/              # 9 ADRs with Summary sections
└── test-reliability/          # Retry strategy, flakiness handling
```

---

## Documentation

### For Technical Reviewers
- **[ADR Index](docs/architecture/)** - 9 architectural decisions with trade-off analysis
- **[Test Reliability Strategy](docs/test-reliability/retry-strategy.md)** - Retry mechanism and flakiness tracking

### For Developers
- **[Docker Setup Guide](docs/DOCKER-SETUP.md)** - Grid setup, VNC debugging, troubleshooting
- **[Live Allure Report](https://aslavchev.github.io/ecommerce-automation-framework/)** - Test execution results

---

## Design Patterns

### Data-Driven Testing
Add test scenarios without code changes:

```csv
# src/test/resources/testdata/products.csv
productName,productPrice
Blue Top,Rs. 500
Sleeveless Dress,Rs. 1000
```

```java
@Test(dataProvider = "productData", dataProviderClass = ProductDataProvider.class)
public void testAddToCart(String productName, String productPrice) {
    // Test automatically runs for each CSV row
}
```

### Retry Mechanism
Automatically retries failed tests (max 2 retries) for environmental flakiness. All retry events tracked in flakiness report for root cause analysis.

See: [Retry Strategy](docs/test-reliability/retry-strategy.md)

---

## Project Status

**Current Phase**: Production-Ready ✅
**Maintenance**: Active
**Last Major Update**: December 2025

---

## Author

**Alex Slavchev**
- GitHub: [@aslavchev](https://github.com/aslavchev)
- LinkedIn: [aslavchev](https://www.linkedin.com/in/aslavchev/)

*18 years testing experience → Test automation engineer*

---

## License

MIT License - see [LICENSE](LICENSE)

---

**Built with:** Java 21 • Selenium 4 • TestNG • REST Assured • Docker • GitHub Actions
