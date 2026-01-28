# API Automation - Indodax

API testing framework using Rest Assured, Cucumber, and data-driven testing with Excel.

---

## 🚀 Quick Start

```bash
# Run all tests
mvn clean test

# Run smoke tests only
mvn clean test -Dcucumber.filter.tags="@smoke"

# Run with script
./run_test.sh smoke
```

---

## 📋 Tech Stack

- **Framework**: Rest Assured + Cucumber
- **Language**: Java 17
- **Build Tool**: Maven
- **Data-Driven**: Apache POI (Excel)
- **Reporting**: Allure

---

## 🎯 Features

✅ BDD with Cucumber (Gherkin scenarios)
✅ Data-driven testing with Excel
✅ JSON schema validation
✅ Allure reporting with graphs
✅ Tag-based test execution
✅ CI/CD ready

---

## 📂 Project Structure

```
api-automation/
├── src/
│   ├── main/java/com/indodax/
│   │   ├── client/        # API client layer
│   │   └── helper/        # Excel reader, utilities
│   └── test/
│       ├── java/
│       │   ├── runner/    # Cucumber test runner
│       │   └── steps/     # Step definitions
│       └── resources/
│           ├── features/  # Gherkin scenarios
│           ├── schemas/   # JSON schemas
│           └── testdata/  # Excel test data
├── pom.xml
└── run_test.sh
```

---

## ▶️ Running Tests

### By Tags
```bash
# Smoke tests
mvn test -Dcucumber.filter.tags="@smoke"

# Positive tests
mvn test -Dcucumber.filter.tags="@positive"

# Negative tests
mvn test -Dcucumber.filter.tags="@negative"
```

### By Feature
```bash
mvn test -Dcucumber.features="src/test/resources/features/01_server_time.feature"
```

### Using Shell Script
```bash
# Run with tag
./run_test.sh smoke
./run_test.sh positive
./run_test.sh negative
```

---

## 📊 Test Coverage

| Endpoint | Scenarios | Status |
|----------|-----------|--------|
| `/api/server_time` | 2 | ✅ |
| `/api/ticker` | 3 | ✅ |
| `/api/pairs` | 2 | ✅ |
| `/api/price_increments` | 2 | ✅ |
| Negative cases | 3 | ✅ |

---

## 📈 Reports

### Allure Report
```bash
# Generate and open Allure report
allure serve target/allure-results
```

**Report includes**:
- Test execution summary
- Pass/fail statistics
- Response time metrics
- Request/response details
- Timeline visualization

---

## 📝 Data-Driven Testing

**Excel file**: `src/test/resources/testdata/indodax_api_testdata.xlsx`

**Sheets**:
- `ServerTime` - Test data for /api/server_time
- `Ticker` - Test data for /api/ticker
- `Pairs` - Test data for /api/pairs
- `PriceIncrements` - Test data for /api/price_increments
- `NegativeTests` - Invalid scenarios
- `TestSummary` - Overview dashboard

**Benefits**:
- Easy to maintain test data
- No code changes for new test cases
- Readable by non-technical QA team
- Version controlled

---

## 🔧 Prerequisites

- Java 17+
- Maven 3.8+

---

## 📦 Installation

```bash
# Install dependencies
mvn clean install

# Verify setup
mvn test -Dcucumber.filter.tags="@smoke"
```

---

## 🎯 CI/CD Integration

This repo is integrated with CI/CD pipeline at:
**https://github.com/Samuelpangestu/dummy-app-repo**

**Automated triggers**:
- Push to main/develop
- Scheduled (4x daily)
- Manual runs

**Output**: Single-file HTML Allure report

---

## 📖 Documentation

For complete architecture and design decisions, see root documentation folder.

---

**Ready to test APIs! 🚀**
