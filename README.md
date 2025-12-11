# E-Commerce Test Automation Framework

**Production-ready test automation with Docker + Selenium Grid, cross-browser execution, and strategic tech decisions.**

[![Tests](https://github.com/aslavchev/ecommerce-automation-framework/actions/workflows/allure-report.yml/badge.svg)](https://github.com/aslavchev/ecommerce-automation-framework/actions)
[![Live Report](https://img.shields.io/badge/📊_Allure_Report-Live-blue)](https://aslavchev.github.io/ecommerce-automation-framework/)
![Tests](https://img.shields.io/badge/tests-23_passing-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.38-green)

---

## 📊 What It Does

- **23 automated tests** (11 UI + 12 API) validating e-commerce flows
- **100% pass rate** across Chrome & Firefox in parallel CI execution
- **Docker + Selenium Grid** for reproducible, scalable test infrastructure
- **Retry mechanism** with flakiness tracking (production reliability patterns)
- **Data-driven testing** via CSV (zero code changes to add scenarios)

**Live proof:** [View Allure Report →](https://aslavchev.github.io/ecommerce-automation-framework/)

---

## ⚡ Quick Start

```bash
# Clone & run
git clone https://github.com/aslavchev/ecommerce-automation-framework.git
cd ecommerce-automation-framework
mvn test

# Run specific groups
mvn test -Dgroups=smoke        # 2min - fast sanity
mvn test -Dgroups=regression   # 5min - full suite

# Docker Grid execution
docker-compose up -d
mvn test -Dexecution=grid

# View report
mvn allure:serve
```

---

## 🎯 Why This Matters

| Problem | Solution |
|---------|----------|
| **Flaky tests block CI** | Retry mechanism (max 2 retries) + flakiness tracking |
| **"Works on my machine"** | Docker + Grid = reproducible anywhere |
| **Browser compatibility** | Chrome + Firefox parallel execution in CI |
| **Slow test data updates** | CSV-driven tests (no code changes) |
| **Tech stack choices unclear** | [Documented rationale](docs/TECH-STACK-RATIONALE.md) with job market data |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────┐
│              GitHub Actions CI/CD                    │
│  ┌──────────────┐          ┌──────────────┐        │
│  │ Chrome Tests │          │Firefox Tests │        │
│  │  (parallel)  │          │  (parallel)  │        │
│  └──────┬───────┘          └──────┬───────┘        │
│         │                         │                 │
│         └─────────┬───────────────┘                 │
│                   ▼                                  │
│         ┌─────────────────────┐                     │
│         │   Allure Reports    │                     │
│         │  (GitHub Pages)     │                     │
│         └─────────────────────┘                     │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│           Local/Docker Grid Execution                │
│                                                      │
│  ┌──────────────┐                                   │
│  │  Test Suite  │                                   │
│  │ (23 tests)   │                                   │
│  └──────┬───────┘                                   │
│         │                                            │
│    ┌────┴─────┐                                     │
│    ▼          ▼                                      │
│  ┌────┐    ┌─────┐                                  │
│  │ UI │    │ API │                                  │
│  │Tests    │Tests│                                  │
│  └─┬──┘    └─────┘                                  │
│    │                                                 │
│    ├─► Page Object Model (8 page objects)           │
│    ├─► REST Assured (APIHelper + models)            │
│    └─► Data Providers (CSV-driven)                  │
│                                                      │
│  ┌──────────────────────────────┐                   │
│  │   Selenium Grid (Docker)     │                   │
│  │  ┌──────┐  ┌──────┐         │                   │
│  │  │Chrome│  │Firefox         │                   │
│  │  │ Node │  │ Node │         │                   │
│  │  └──────┘  └──────┘         │                   │
│  │         Hub:4444             │                   │
│  └──────────────────────────────┘                   │
└─────────────────────────────────────────────────────┘
```

**Design Principles:**
- **Page Object Model** - UI interactions encapsulated, zero duplication
- **Test Pyramid** - 60% API (fast) + 40% UI (critical paths)
- **Retry Pattern** - IRetryAnalyzer + RetryListener for production reliability
- **Docker-first** - Reproducible infrastructure via `docker-compose.yml`

---

## 🚀 Tech Stack

| Layer | Technology | Why? |
|-------|-----------|------|
| **Language** | Java 21 | Modern LTS, industry standard |
| **UI Testing** | Selenium 4.38 | [56k companies vs 7k for Playwright](docs/TECH-STACK-RATIONALE.md) |
| **Test Framework** | TestNG 7.11 | [Better test organization than JUnit](docs/TECH-STACK-RATIONALE.md) |
| **API Testing** | REST Assured 5.5.6 | De facto Java API standard |
| **Infrastructure** | Docker + Selenium Grid | Reproducible, scalable execution |
| **CI/CD** | GitHub Actions | Parallel browser execution |
| **Reporting** | Allure 2.29 | Interactive HTML reports |

**Strategic Decisions:** [Full tech stack rationale →](docs/TECH-STACK-RATIONALE.md)

---

## 🧪 Test Organization

```bash
# By Speed
mvn test -Dgroups=smoke       # 2min  - Login, critical paths
mvn test -Dgroups=regression  # 5min  - Full suite

# By Type
mvn test -Dgroups=ui          # UI tests only
mvn test -Dgroups=api         # API tests only
mvn test -Dgroups=e2e         # End-to-end journeys

# By Browser
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox

# Docker Grid
docker-compose up -d
mvn test -Dexecution=grid -Dgrid.url=http://localhost:4444
```

---

## 📁 Project Structure

```
src/
├── main/java/io/github/aslavchev/
│   ├── ui/pages/              # 8 Page Objects (Login, Products, Cart, Checkout)
│   └── api/                   # APIHelper + REST Assured models
└── test/java/io/github/aslavchev/
    ├── ui/                    # UI tests + BaseTest
    ├── api/                   # API tests + BaseAPITest
    ├── data/                  # DataProviders + CSV
    ├── listeners/             # RetryListener
    └── utils/                 # TestConfig, RetryAnalyzer

docker-compose.yml             # Selenium Hub + Chrome/Firefox nodes
docs/
├── architecture/              # ADRs (Architecture Decision Records)
├── DOCKER-SETUP.md           # Grid setup + troubleshooting
└── TECH-STACK-RATIONALE.md   # Why Selenium? Why TestNG?
```

---

## 📚 Documentation

| Doc | Purpose |
|-----|---------|
| [DOCKER-SETUP.md](docs/DOCKER-SETUP.md) | Grid setup, VNC debugging, troubleshooting |
| [TECH-STACK-RATIONALE.md](docs/TECH-STACK-RATIONALE.md) | Why Selenium? Why TestNG? (Job market data) |
| [ADRs](docs/architecture/) | Architecture decisions (ADR-001 to ADR-012) |
| [Allure Report](https://aslavchev.github.io/ecommerce-automation-framework/) | Live test execution results |

---

## 🔧 Advanced Usage

### Data-Driven Testing

Add test scenarios without code changes:

```csv
# src/test/resources/testdata/products.csv
productName,category,expectedBehavior
"Blue Top","Women > Tops","Add to cart successfully"
```

```java
@Test(dataProvider = "productData")
public void testAddToCart(String name, String category, String expected) {
    // Test automatically runs for each CSV row
}
```

### Docker Grid with VNC Debugging

```bash
docker-compose up -d
# VNC into Chrome: localhost:5900 (password: secret)
# VNC into Firefox: localhost:5901 (password: secret)
mvn test -Dexecution=grid
```

---

## 🏆 Metrics

- **23 tests** (11 UI + 12 API)
- **100% pass rate** in CI (13/13 recent runs)
- **2-3 min** execution time (parallel)
- **0 flaky tests** (retry mechanism validates stability)
- **2 browsers** (Chrome, Firefox) validated in every CI run

---

## 👤 Author

**Alex Slavchev**
- GitHub: [@aslavchev](https://github.com/aslavchev)
- LinkedIn: [aslavchev](https://www.linkedin.com/in/aslavchev/)

*18 years testing experience → Test automation engineer*

---

## 📝 License

MIT License - see [LICENSE](LICENSE)

---

**Built with:** Java 21 • Selenium 4 • TestNG • REST Assured • Docker • GitHub Actions
