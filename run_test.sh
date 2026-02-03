#!/bin/bash

##############################################
# API Automation Test Runner
# Usage: ./run_test.sh [tag]
##############################################

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo ""
echo -e "${BLUE}=========================================="
echo "   API Automation Test Runner"
echo -e "==========================================${NC}"
echo ""

# Parse arguments
TAG=${1:-@smoke}

# Display configuration
echo -e "${GREEN}Configuration:${NC}"
echo "  Test Tag: $TAG"
echo ""

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Error: Maven not found${NC}"
    exit 1
fi

if ! command -v allure &> /dev/null; then
    echo -e "${YELLOW}Warning: Allure command line not found${NC}"
    echo "Install with: npm install -g allure-commandline"
    echo ""
fi

echo -e "${GREEN}✓ All prerequisites met${NC}"
echo ""

# Clean previous results
echo -e "${YELLOW}Cleaning previous results...${NC}"
rm -rf target/allure-results target/allure-report 2>/dev/null
echo -e "${GREEN}✓ Cleaned${NC}"
echo ""

# Run tests
echo -e "${BLUE}=========================================="
echo "   Running API Tests"
echo -e "==========================================${NC}"
echo ""

mvn clean test -Dcucumber.filter.tags="$TAG"
TEST_RESULT=$?

echo ""
echo -e "${BLUE}=========================================="
if [ $TEST_RESULT -eq 0 ]; then
    echo -e "   ${GREEN}Tests Completed Successfully${NC}"
else
    echo -e "   ${YELLOW}Tests Completed with Failures${NC}"
fi
echo -e "${BLUE}==========================================${NC}"
echo ""

# Generate and serve Allure report
if [ -d "target/allure-results" ] && [ "$(ls -A target/allure-results)" ]; then
    echo -e "${YELLOW}Opening Allure report...${NC}"
    echo ""

    # Use allure serve to automatically generate and open report
    allure serve target/allure-results

else
    echo -e "${YELLOW}No test results found in target/allure-results${NC}"
fi

echo ""
echo -e "${BLUE}=========================================="
echo "   Done"
echo -e "==========================================${NC}"
echo ""

# Show available tags if tests failed
if [ $TEST_RESULT -ne 0 ]; then
    echo "Available tags:"
    echo "  @smoke       - Smoke tests"
    echo "  @regression  - Regression tests"
    echo "  @sanity      - Sanity tests"
    echo "  @positive    - Positive scenarios"
    echo "  @negative    - Negative scenarios"
    echo ""
    echo "Example: ./run_test.sh @smoke"
    echo ""
fi

exit $TEST_RESULT
