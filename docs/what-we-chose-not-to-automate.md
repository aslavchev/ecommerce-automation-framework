# What We Chose NOT to Automate

Strategic testing decisions include not only what to build, but what to deliberately exclude. These decisions demonstrate risk-based thinking over "coverage theater."

**Last Updated**: December 2025

---

## Decision Framework

Before choosing NOT to automate, we evaluated:
1. **Technical Feasibility**: Can this be automated with current stack?
2. **ROI Analysis**: Cost (dev hours) vs. Benefit (bugs caught per year)
3. **Risk Level**: What's the business impact if this breaks?
4. **Alternative Strategy**: How do we mitigate risk without automation?

---

## Decision 1: Visual Regression Testing

### Why We Could Automate This
**Technical Feasibility**: ✅ High
- Tools available: Percy, Applitools, BackstopJS
- Selenium screenshots already captured
- Integration effort: ~30 hours setup + 5 hours/month maintenance

### Why We Chose NOT To
**ROI Analysis**: ❌ Negative ROI
- **Cost**: 30h initial setup + 5h/month managing false positives from dynamic content
- **Benefit**: Target site (automationexercise.com) has static design, estimated ~2 visual bugs/year
- **Calculation**: 80+ hours investment annually to catch 2 cosmetic bugs = 40h per bug

**Risk Analysis**:
- Visual bugs on this site are low-severity (CSS rendering issues, not payment failures)
- Not revenue-blocking (e-commerce checkout functionally tested)
- **Critical context**: Already addressing 35% test flakiness in existing tests—adding notoriously flaky visual regression tests would compound instability

**The Trade-off**:
Stability in existing 25 tests > adding 80+ hours of flaky visual tests

### Alternative Strategy
**Manual smoke test**: 5-minute visual check of 5 key pages before production deploy
- Login page
- Product listing
- Cart page
- Checkout flow
- Order confirmation

**Cost**: 5 min × 4 deploys/month = 20 min/month vs. 80h+ automation cost

---

## Decision 2: Accessibility Testing (WCAG 2.1 AA)

### Why We Could Automate This
**Technical Feasibility**: ✅ Moderate
- Tools available: axe-core, pa11y, WAVE
- Selenium integration straightforward
- Integration effort: ~4-6 hours for basic coverage

### Why We Chose NOT To
**ROI Analysis**: ⚠️ Low priority for demo site
- **Cost**: 6h setup + 3-5 additional tests + ongoing maintenance
- **Context**: Demo site not serving real users with accessibility needs
- **Benefit**: Would find accessibility violations, but no business impact for demonstration project

**Risk Analysis**:
- No legal compliance requirement (not production application)
- No user base requiring accessible experience
- Better investment: focus on stability and core functionality first

**The Trade-off**:
Prove stability and architectural discipline first, defer accessibility until targeting accessibility-focused employers

### Alternative Strategy
**Available for future extension**: Can be implemented if needed
- Can be added when applying to companies with accessibility focus
- Foundation exists (Selenium + TestNG) for quick integration
- Estimated effort: 4-6 hours to add when needed (axe-core, pa11y)

---

## Decision 3: Performance & Load Testing

### Why We Could Automate This
**Technical Feasibility**: ✅ High
- Tools available: JMeter, Gatling, k6
- API tests already exist as foundation
- Integration effort: ~6-8 hours for response time gates

### Why We Chose NOT To
**ROI Analysis**: ❌ Not applicable to demo site
- **Context**: Demo site (automationexercise.com) not owned by us, no production traffic
- **Benefit**: Performance testing valuable for production apps, not demonstration projects
- **Risk**: Demo site performance is external dependency, outside our control

**Priority Decision**:
Prove architectural discipline (flakiness elimination, stability) before expanding to performance validation

**The Trade-off**:
Focus automation investment on demonstrating reliability patterns (retry mechanism, sequential execution) over performance metrics we can't control

### Alternative Strategy
**Documented for production readiness**: Available for future implementation
- API response time monitoring via Allure reports (basic observability exists)
- Can add JMeter/Gatling when working with real production systems
- Current focus: stability and trust metrics (100% pass rate, 0% flakiness)

---

## Decision 4: 90%+ Code Coverage Goal

### Why We Could Automate This
**Technical Feasibility**: ✅ High
- Could write 100+ additional tests to hit 90% coverage metric
- Tools exist: JaCoCo, SonarQube
- Standard industry practice

### Why We Chose NOT To
**ROI Analysis**: ❌ Diminishing returns
- **Current**: 25 tests covering critical user journeys (login, checkout, cart, registration)
- **To hit 90%**: Would need ~100 additional tests covering admin features, edge cases, error pages
- **Maintenance cost**: 100 tests × 5 min/test/year = 500 min/year ongoing upkeep
- **Flakiness risk**: More tests = higher probability of introducing instability

**Risk Analysis**:
- Last 30% of coverage targets low-traffic areas (admin dashboards, rarely-used filters)
- Business-critical paths already at 100% coverage (checkout, payment flow, registration)
- **Core principle**: 25 stable, maintainable tests > 125 tests with flakiness creep

**The Trade-off**:
Strategic test selection (risk-based) vs. coverage metrics theater

### Alternative Strategy
**Risk-based prioritization**: Focus on revenue-impacting paths
- ✅ 100% coverage of checkout flow (revenue-critical)
- ✅ 100% coverage of user registration (access-blocking)
- ✅ 80% coverage of product browsing (primary user activity)
- ❌ 0% coverage of admin dashboards (manual testing acceptable)

**See**: [ADR-002: TestNG Groups](architecture/ADR-002-testng-groups.md) for test categorization strategy

---

## Summary: Automation Philosophy

### What We Automate
✅ **High-frequency, high-risk user journeys**
- Login/Registration (blocks all other features)
- Checkout flow (revenue-critical)
- Product browsing (primary user activity)
- API contracts (integration stability)

✅ **Regression-prone functionality**
- Cross-browser compatibility (Chrome, Firefox in CI)
- Data-driven scenarios (CSV-based test expansion)

### What We Don't Automate
❌ **Low-ROI targets**
- Visual regression on static designs (80h to catch 2 bugs/year)
- Edge cases in low-traffic admin areas (diminishing returns)
- Performance testing on demo sites (external dependency)

❌ **Premature optimization**
- Accessibility testing before proving core stability
- 90%+ coverage goals over strategic test selection

### The Core Principle

**"Can we afford NOT to automate this?"** > **"Can we automate this?"**

Decision framework:
- **No, this breaks revenue** → Automate
- **Yes, manual testing is acceptable** → Don't automate
- **Maybe, but stability comes first** → Defer until foundation solid

---

## Related Documentation
- [ADR-002: TestNG Groups for Test Selection](architecture/ADR-002-testng-groups.md)
- [ADR-004: CI/CD-First Strategy](architecture/ADR-004-cicd-strategy.md)
- [ADR-012: Observability & Metrics](architecture/ADR-012-observability-metrics.md) - Right-sizing for scale

---

**Philosophy**: Strategic risk mitigation over automation for automation's sake. These decisions reflect architectural discipline: knowing when NOT to build is as important as knowing what to build.
