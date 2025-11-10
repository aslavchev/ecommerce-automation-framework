# E-Commerce Test Automation Framework

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.15.0-green)
![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red)

**Strategic test automation framework for e-commerce testing demonstrating production-ready automation practices.**

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
| **CI/CD** | GitHub Actions | In Progress |
| **Containerization** | Docker | Planned |

---

## 📁 Project Structure

```
ecommerce-automation-framework/
├── src/
│   ├── main/java/io/github/aslavchev/
│   │   ├── pages/              # Page Object Model classes
│   │   ├── api/                # API testing models
│   │   └── utils/              # Shared utilities
│   └── test/
│       ├── java/io/github/aslavchev/
│       │   ├── base/           # Base test classes
│       │   └── tests/
│       │       ├── ui/         # UI test cases
│       │       └── api/        # API test cases
│       └── resources/
│           ├── config/         # Configuration files
│           └── testdata/       # Test data files
├── pom.xml                     # Maven configuration
├── .github/workflows/          # CI/CD pipelines
├── docker/                     # Docker configuration
└── docs/                       # Documentation
    ├── architecture/           # Architecture Decision Records
    └── test-strategy.md        # Test strategy document
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

# Run specific test suite
mvn test -Dtest=LoginTests

# Run with specific browser
mvn test -Dbrowser=chrome

# Generate test reports
mvn surefire-report:report
```

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

### 🚧 **Phase 2: CI/CD Infrastructure (Week 3) - IN PROGRESS**
- [ ] GitHub Actions workflow for automated test execution
- [ ] Headless browser configuration
- [ ] Allure report publishing to GitHub Pages
- [ ] Environment variable configuration for credentials
- [ ] Build status badges

### **Phase 3: Test Coverage Expansion (Week 4)**
- [ ] ProductPage, CartPage, CheckoutPage objects
- [ ] 6-8 new test scenarios across user journeys
- [ ] End-to-end critical path coverage

### **Phase 4: Data-Driven Testing (Week 5)**
- [ ] TestNG DataProvider implementation
- [ ] CSV/JSON test data management
- [ ] Parameterized test scenarios

### **Phase 5: API Testing Integration (Week 6)**
- [ ] REST Assured test suite
- [ ] API + UI combined test scenarios
- [ ] Full-stack QA demonstration

### **Phase 6: Advanced CI/CD (Weeks 7-8)**
- [ ] Parallel test execution configuration
- [ ] Cross-browser testing (Chrome, Firefox)
- [ ] Test retry mechanisms for flaky tests

### **Phase 7: Containerization (Weeks 9-10)**
- [ ] Docker test execution environment
- [ ] Selenium Grid setup
- [ ] Scalable infrastructure

### **Phase 8: Polish & Completion (Weeks 11-12)**
- [ ] Performance optimization
- [ ] Comprehensive documentation updates
- [ ] Final portfolio review

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

**Status**: ✅ Week 2 Complete | 🚧 Week 3 In Progress (CI/CD Integration)

**Last Updated**: November 10, 2025
