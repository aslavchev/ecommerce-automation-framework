# Architecture Decision Records

This directory contains Architecture Decision Records (ADRs) documenting key technical decisions for the test automation framework. Each ADR includes a Summary section for clarity and accessibility.

## Index

### Core Framework Decisions

**[ADR-001: Page Object Model Pattern](ADR-001-page-object-model.md)** *(Accepted - 2025-11-10)*
- **Decision**: Use Page Object Model with BasePage abstraction to centralize locators and wait strategies
- **Key Trade-off**: More files upfront vs. 70% code reduction and eliminated flakiness

**[ADR-002: TestNG Groups for Flexible Test Execution](ADR-002-testng-groups.md)** *(Accepted - 2025-11-25)*
- **Decision**: Categorize tests by purpose (smoke, regression, critical) and speed using TestNG groups
- **Key Trade-off**: Tagging discipline required vs. 60-80% faster developer feedback (2 min vs 10 min)

**[ADR-003: CSV-Based Data-Driven Testing](ADR-003-data-driven-testing.md)** *(Accepted - 2025-11-28)*
- **Decision**: Centralized CSV architecture with shared product catalog and automatic data enrichment
- **Key Trade-off**: Runtime CSV errors vs. QA team autonomy and 200% scenario growth without code changes

### Process & Strategy Decisions

**[ADR-004: CI/CD-First Automation Strategy](ADR-004-cicd-strategy.md)** *(Accepted - 2025-11-10)*
- **Decision**: Implement GitHub Actions CI/CD in Week 3 before expanding test coverage
- **Key Trade-off**: 4-day infrastructure investment with only 2 tests vs. automatic validation for 8 weeks of future development

### Evaluated (Not Implemented)

**[ADR-012: Observability & Test Metrics Strategy](ADR-012-observability-metrics.md)** *(Evaluated - 2025-12-11)*
- **Decision**: Document SLF4J + Logback approach but don't implement - Allure sufficient at 23-test scale
- **Key Trade-off**: Avoided over-engineering while documenting scalability path for future growth

---

## Key Themes

- **Stability > Speed**: ADR-002 (Groups), ADR-004 (CI/CD First)
- **Simplicity > Enterprise Patterns**: ADR-003 (CSV over Database)
- **Long-term Maintainability**: ADR-001 (Page Object Model)
- **Pragmatic Engineering**: ADR-012 (Right-sizing for scale)

---

## What are ADRs?

Architecture Decision Records document significant architectural decisions, their context, alternatives considered, and consequences. They help:
- Explain "why" behind technical choices
- Onboard new team members
- Avoid revisiting settled decisions
- Learn from past decisions

## ADR Structure

Each ADR follows this format:
1. **Context**: Problem and constraints
2. **Decision**: What was chosen
3. **Rationale**: Why it was chosen
4. **Alternatives Considered**: What else was evaluated (with rejection reasons)
5. **Consequences**: Trade-offs and impacts
6. **Summary**: Plain-language explanation with problem, options, choice, impact, and trade-offs

## References

- [ADR GitHub Organization](https://adr.github.io/)
- [Documenting Architecture Decisions](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions)
