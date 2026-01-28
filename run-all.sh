#!/bin/bash

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo ""
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║                                                              ║"
echo "║         INDODAX API AUTOMATION - ALL IN ONE                  ║"
echo "║                                                              ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo ""

# Step 1: Run Tests
echo -e "${BLUE}[1/2] Running API Tests...${NC}"
echo ""
mvn clean test

# Check if tests passed
if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Tests completed successfully${NC}"
else
    echo ""
    echo -e "${YELLOW}⚠️  Tests completed with some issues${NC}"
fi

# Step 2: Generate Allure Report
echo ""
echo -e "${BLUE}[2/2] Generating Allure Report...${NC}"
echo ""

if [ -d "target/allure-results" ]; then
    echo -e "${GREEN}✅ Allure results found${NC}"
    echo ""
    echo -e "${BLUE}Opening Allure Report in browser...${NC}"
    echo ""

    # Generate and serve Allure report
    mvn allure:serve

else
    echo -e "${RED}❌ No allure-results found${NC}"
    echo "Please check if tests ran successfully"
fi

echo ""
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║                                                              ║"
echo "║                    EXECUTION COMPLETE                        ║"
echo "║                                                              ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo ""
