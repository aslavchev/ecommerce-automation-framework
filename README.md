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
- **DevOps**: Docker containerization and GitHub Actions CI/CD (coming in Weeks 5-6)

Built by a QA professional with 18 years of testing expertise, this framework combines strategic test planning with modern automation implementation.

---

## 🚀 Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 21 |
| **Build Tool** | Maven | 3.9+ |
| **UI Automation** | Selenium WebDriver | 4.15.0 |
| **Test Framework** | TestNG | 7.8.0 |
| **API Testing** | REST Assured | 5.4.0 |
| **Reporting** | Allure Report | 2.25.0 |
| **Driver Management** | WebDriverManager | 5.6.2 |
| **Containerization** | Docker | (Week 5) |
| **CI/CD** | GitHub Actions | (Week 6) |

---

## 📁 Project Structure

```
ecommerce-automation-framework/
├── src/
│   ├── main/java/io/github/[username]/
│   │   ├── pages/              # Page Object Model classes
│   │   ├── api/                # API testing models
│   │   └── utils/              # Shared utilities
│   └── test/
│       ├── java/io/github/[username]/
│       │   ├── base/           # Base test classes
│       │   └── tests/
│       │       ├── ui/         # UI test cases
│       │       └── api/        # API test cases
│       └── resources/
│           ├── config/         # Configuration files
│           └── testdata/       # Test data files
├── pom.xml                     # Maven configuration
├── docker/                     # Docker configuration (Week 5)
├── .github/workflows/          # CI/CD pipelines (Week 6)
└── docs/                       # Documentation
    ├── test-strategy.md        # Test strategy document
    └── decisions/              # Architecture Decision Records
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

Reports will be available at `target/allure-results/` (Week 4+).

---

## 🏗️ Framework Design

### Page Object Model

All UI interactions are encapsulated in Page Object classes following the Single Responsibility Principle:

```java
package io.github.[username].pages;

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
package io.github.[username].api;

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

- **[Test Strategy](docs/test-strategy.md)**: Comprehensive test approach and rationale
- **[Architecture Decisions](docs/decisions/)**: ADRs documenting key technical choices
- **[Setup Guide](docs/setup.md)**: Detailed environment setup (Week 2+)

---

## 🎓 Learning & Development

This framework was built as part of a transition from manual QA (18 years) to test automation, demonstrating:

- Strategic test planning and risk-based prioritization
- Modern automation tools and best practices
- Production-ready framework architecture
- CI/CD integration and DevOps practices
- Comprehensive documentation and decision records

---

## 📈 Roadmap

**Week 2-4**: Core framework implementation
- [x] Maven project setup
- [ ] Page Object Model implementation
- [ ] UI test suite (Selenium)
- [ ] API test suite (REST Assured)
- [ ] Test reporting (Extent Reports)

**Week 5-6**: DevOps integration
- [ ] Docker containerization
- [ ] GitHub Actions CI/CD pipeline
- [ ] Automated test execution on commit

**Week 7**: Documentation & polish
- [ ] Complete test strategy document
- [ ] Architecture Decision Records
- [ ] Framework design documentation

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

**Status**: 🚧 Work in Progress (Week 1 - Project Setup)

**Last Updated**: November 2025
