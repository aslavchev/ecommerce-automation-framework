# ADR-003: CSV-Based Data-Driven Testing

**Status**: Accepted
**Date**: 2025-11-28

---

## Context

After implementing 10 UI tests with hardcoded data, expanding coverage required duplicate test methods for different data combinations (e.g., checkout with different products, cart operations with variations). This violated DRY principles and made maintenance difficult.

**Constraints:**
- QA team should add test scenarios without code changes
- Product data (names, prices) shared across multiple test classes
- Some data is test-specific (e.g., payment details)
- Maintain existing coverage while adding scenarios

---

## Decision

**Implement centralized CSV-based architecture with data enrichment pattern.**

**Components:**
1. **Shared product catalog** (`products.csv`) via `ProductDataProvider`
2. **Test-specific CSV files** (e.g., `checkout-payment.csv`)
3. **Data enrichment** where `TestDataReader` combines shared and specific data

**Architecture:**
```
src/test/resources/testdata/
├── products.csv                    # Shared product catalog
└── checkout-payment.csv            # Test-specific data

src/test/java/.../dataproviders/
└── ProductDataProvider.java        # Centralized catalog

src/main/java/.../utils/
└── TestDataReader.java             # CSV parsing + enrichment
```

---

## Rationale

### Why CSV?
- **Accessible**: QA team edits without IDE (Excel-compatible)
- **Version control friendly**: Git diffs work well
- **Industry standard**: Lightweight, no heavy libraries
- **Maintainable**: Non-developers can add test scenarios

### Why Centralized ProductDataProvider?
- **Single Source of Truth**: Product defined once, used everywhere
- **DRY Principle**: Price change in one place updates all tests
- **Type Safety**: Throws exception if product not found
- **Performance**: Lazy loading, cached

### Why Data Enrichment?
- **Concise CSVs**: `checkout-payment.csv` references product names, not full details
- **Automatic injection**: Product price looked up from catalog
- **Data consistency**: Prevents price mismatches

---

## Alternatives Considered

**1. Separate CSV Per Test Class**
- Simple but duplicates product data across files
- Rejected: Violates DRY, maintenance burden

**2. Database-Driven Test Data**
- Complex queries, relationships, scalable
- Rejected: Over-engineering for current scale

**3. JSON Test Data**
- Nested structures, type-safe
- Rejected: Less accessible to QA team than CSV

**4. Inline Arrays (Current)**
- Fast but requires recompilation for changes
- Rejected: Not maintainable by QA team

---

## Consequences

### Positive
✅ QA team adds scenarios via CSV edits
✅ +200% scenarios (1 → 3 for Cart, 1 → 2 for Checkout)
✅ Product catalog reused across tests
✅ Zero code duplication
✅ Easy to scale to 50+ scenarios

### Negative
⚠️ More files to understand (+4 files)
⚠️ CSV errors discovered at runtime
⚠️ Learning curve for enrichment pattern

**Mitigation**: Clear documentation, CSV validation utilities

---

## Implementation

### products.csv
```csv
productName,productPrice
Blue Top,Rs. 500
Sleeveless Dress,Rs. 1000
Men Tshirt,Rs. 400
```

### checkout-payment.csv
```csv
testName,productName,cardName,cardNumber,cvc,expiryMonth,expiryYear
Blue Top Checkout,Blue Top,John Doe,4532015112830366,123,12,2030
```

### Data Enrichment Flow
```java
// 1. Read CSV (7 columns)
String productName = csvRow[1];  // "Blue Top"

// 2. Lookup price from catalog
String price = ProductDataProvider.getProductPrice(productName);  // "Rs. 500"

// 3. Return enriched data (8 columns)
return {testName, productName, price, cardName, cardNumber, cvc, month, year};
```

### Test Usage
```java
@Test(dataProvider = "checkoutData", dataProviderClass = CheckoutDataProvider.class)
public void testCheckout(String testName, String productName, String price, ...) {
    // Test implementation
}
```

---

## Results

**Test Coverage:**
- CartTests: 3 scenarios from `ProductDataProvider`
- CheckoutTests: 2 scenarios from enriched CSV
- Total: 11 tests passing, 5 data-driven scenarios

**Maintainability:**
- Adding new product: 1 line in `products.csv`
- Adding checkout scenario: 1 line in `checkout-payment.csv`
- No code changes required

---

## References

- [TestNG DataProvider Documentation](https://testng.org/doc/documentation-main.html#parameters-dataproviders)
- Production code: [GitHub commit 5b89853](https://github.com/aslavchev/ecommerce-automation-framework/commit/5b89853)

---

## 🧒 ELI5 (Explain Like I'm 5)

**The Problem**:
Hardcoded test data meant creating duplicate test methods for each scenario (different products, payment details). QA team couldn't add test scenarios without touching code. Product data scattered across files.

**The Options**:
1. Separate CSV per test class - Simple but duplicates product catalog across every file
2. Database-driven testing - Industry standard but requires DB setup/maintenance for 25 tests
3. JSON files - Flexible but less accessible to non-developers than spreadsheets
4. Centralized CSV with data enrichment - Shared product catalog, test-specific CSVs combined automatically

**The Choice**:
CSV-based architecture with centralized ProductDataProvider and automatic data enrichment.

**Why This Matters**:
QA team edits CSV files in Excel to add scenarios without code changes. Product price updates in one place automatically flow to all tests. Grew from 1 scenario to 3 for cart tests through simple CSV edits.

**The Trade-off**:
Added 4 new files (CSV + data provider classes) and CSV errors discovered at runtime instead of compile time. We accepted this because QA team autonomy and 200% scenario growth outweighed validation complexity.

**Key Takeaway**:
"Chose CSV-based data-driven testing with centralized product catalog over database-driven because simplicity and team accessibility mattered more than enterprise patterns—grew test scenarios 200% through QA team CSV edits without code changes."
