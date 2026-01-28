#!/bin/bash

# ===========================
# API Automation Test Runner
# ===========================
# Run Cucumber tests with Maven

set -e

# Get test tag (default: @smoke)
TAG="${1:-@smoke}"

echo "🔧 Running API Automation Tests"
echo "================================"
echo "Test Tag: $TAG"
echo ""

# Run Maven test with Cucumber tag
mvn clean test -Dcucumber.filter.tags="$TAG"

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Tests completed successfully!"
    echo "📊 Allure results: target/allure-results/"
else
    echo ""
    echo "❌ Tests failed!"
    exit 1
fi
