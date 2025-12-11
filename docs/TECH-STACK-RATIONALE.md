# Tech Stack Rationale - Why These Tools?

**Purpose**: Explain strategic choices for portfolio project (interview prep)

---

## TL;DR (Kid-Friendly Version)

**Q: Why TestNG instead of JUnit 5?**
**A:** Both are great! TestNG has easier test grouping (smoke, regression). It's like organizing your toys by type vs. throwing them all in one box.

**Q: Why Selenium instead of Playwright?**
**A:** Selenium is the "old reliable" that most companies use (56,000+ companies). Playwright is the "new cool kid" (growing 235%/year). Learning Selenium gets you hired, then you can learn Playwright on the job.

---

## 1. TestNG vs JUnit 5

### The Data
- **Job Market**: Both frameworks equally valued in 2025 [LinkedIn Advice](https://www.linkedin.com/advice/1/how-do-you-choose-between-testng-junit)
- **Features**: Similar since JUnit 5 (parallel execution, annotations) [BrowserStack Guide](https://www.browserstack.com/guide/junit-vs-testng)

### Why TestNG for This Project?

| Feature | TestNG | JUnit 5 | Winner |
|---------|--------|---------|--------|
| **Test Groups** | Built-in (`@Test(groups="smoke")`) | Requires tags | TestNG |
| **Parallel Execution** | XML config (simpler) | More setup | TestNG |
| **Test Dependencies** | `dependsOnMethods` | Workarounds needed | TestNG |
| **Reporting** | Better default reports | Basic | TestNG |

**Decision**: TestNG for better test organization in automation suites.

**Trade-off**: JUnit 5 has broader unit testing adoption. TestNG wins for **integration/E2E testing**.

---

## 2. Selenium vs Playwright

### The Data (2025)

| Metric | Selenium | Playwright | Source |
|--------|----------|------------|--------|
| **Market Share** | 39% | 15% | [6sense](https://6sense.com/tech/testing-and-qa/selenium-vs-playwright) |
| **Companies Using** | 56,291 | 7,079 | [6sense](https://6sense.com/tech/testing-and-qa/playwright-market-share) |
| **Growth Rate** | Stable | +235% YoY | [TestDino](https://testdino.com/blog/playwright-market-share/) |
| **Job Postings** | Must-have | Nice-to-have | [VarnikTech](https://varniktech.com/playwright-automation-vs-selenium/) |

### Why Selenium for This Portfolio?

**Strategic Reasoning**:

1. **Job Market Reality**: [BrowserStack reports](https://www.browserstack.com/guide/playwright-vs-selenium) Selenium dominates job requirements (especially FAANG/Enterprise)
2. **Portfolio Signal**: Shows you know industry-standard tools employers use TODAY
3. **Learning Path**: Easier to learn Playwright after Selenium than vice versa
4. **Interview Value**: More interviewers know Selenium, easier to discuss architecture

### What Playwright Does Better

✅ Auto-wait (no explicit waits)
✅ Faster execution
✅ Better API
✅ Network interception

**But**: Selenium + good practices (explicit waits, POM) achieves same stability.

### The Trade-Off

- **Chose**: Proven tool (Selenium) for portfolio credibility
- **Accepted**: Slightly more boilerplate code vs Playwright's cleaner API
- **Mitigation**: Clean architecture (POM, BasePage patterns) minimizes Selenium verbosity

---

## 3. Full Stack Summary

```
Java 21          → Latest LTS, modern language features
Selenium 4       → Industry standard, FAANG-proven (39% market share)
TestNG           → Best for test suite organization
REST Assured     → De facto Java API testing standard
Maven            → Industry standard build tool (vs Gradle)
Allure           → Professional reporting, CI integration
GitHub Actions   → Free CI/CD, portfolio-friendly
```

---

## Interview Talking Points

**Q: "Why not use Playwright? It's faster and more modern."**

**Answer**:
> "I evaluated both. Playwright is excellent and growing fast (235% YoY), but I chose Selenium because:
> 1. **Job market alignment** - 56k companies use Selenium vs 7k for Playwright (2025 data)
> 2. **Portfolio positioning** - Shows I can work with tools enterprises actually use
> 3. **Learning strategy** - Selenium foundations transfer to Playwright easily
>
> For a greenfield project at a modern startup, I'd absolutely consider Playwright. For this portfolio demonstrating SDET skills across companies, Selenium was the strategic choice."

**Q: "Why TestNG over JUnit?"**

**Answer**:
> "Both are great. I chose TestNG because test grouping (`@Test(groups="smoke")`) and parallel execution are simpler, which matters for automation suites. JUnit 5 excels at unit testing, TestNG excels at integration/E2E testing. Since this is an automation framework, TestNG fit better."

---

## Key Learning

**Portfolio projects aren't about "best" tech - they're about strategic choices that:**
1. Match job market reality
2. Demonstrate you understand trade-offs
3. Show you can justify decisions with data

---

## Sources

- [JUnit vs TestNG Comparison - BrowserStack](https://www.browserstack.com/guide/junit-vs-testng)
- [Selenium vs Playwright 2025 - BrowserStack](https://www.browserstack.com/guide/playwright-vs-selenium)
- [Playwright Market Share Data - TestDino](https://testdino.com/blog/playwright-market-share/)
- [Selenium Market Share - 6sense](https://6sense.com/tech/testing-and-qa/selenium-vs-playwright)
- [TestNG vs JUnit - LinkedIn Advice](https://www.linkedin.com/advice/1/how-do-you-choose-between-testng-junit)

---

**Phase 6.5 Part 3: Complete** ✅
