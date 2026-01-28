#!/bin/bash

echo "========================================"
echo "  INDODAX API AUTOMATION TEST - DEMO"
echo "========================================"
echo ""

echo "✅ Testing API Endpoints..."
echo ""

# Test 1: Server Time
echo "1️⃣ Testing GET /api/server_time"
RESPONSE=$(curl -s "https://indodax.com/api/server_time")
echo "Response: $RESPONSE"
if echo "$RESPONSE" | grep -q "server_time"; then
    echo "✅ PASS - Server time endpoint working"
else
    echo "❌ FAIL - Server time endpoint failed"
fi
echo ""

# Test 2: Ticker BTC/IDR
echo "2️⃣ Testing GET /api/ticker/btcidr"
RESPONSE=$(curl -s "https://indodax.com/api/ticker/btcidr")
echo "Response: ${RESPONSE:0:200}..."
if echo "$RESPONSE" | grep -q "ticker"; then
    echo "✅ PASS - Ticker BTC/IDR endpoint working"
else
    echo "❌ FAIL - Ticker endpoint failed"
fi
echo ""

# Test 3: Ticker ETH/IDR
echo "3️⃣ Testing GET /api/ticker/ethidr"
RESPONSE=$(curl -s "https://indodax.com/api/ticker/ethidr")
if echo "$RESPONSE" | grep -q "ticker"; then
    echo "✅ PASS - Ticker ETH/IDR endpoint working"
else
    echo "❌ FAIL - Ticker endpoint failed"
fi
echo ""

# Test 4: Pairs
echo "4️⃣ Testing GET /api/pairs"
RESPONSE=$(curl -s "https://indodax.com/api/pairs")
echo "Response: ${RESPONSE:0:150}..."
if echo "$RESPONSE" | grep -q "symbol"; then
    echo "✅ PASS - Pairs endpoint working"
else
    echo "❌ FAIL - Pairs endpoint failed"
fi
echo ""

# Test 5: Price Increments
echo "5️⃣ Testing GET /api/price_increments"
RESPONSE=$(curl -s "https://indodax.com/api/price_increments")
echo "Response: ${RESPONSE:0:150}..."
if echo "$RESPONSE" | grep -q "increments"; then
    echo "✅ PASS - Price increments endpoint working"
else
    echo "❌ FAIL - Price increments endpoint failed"
fi
echo ""

echo "========================================"
echo "  API TESTS COMPLETED!"
echo "========================================"
echo ""
echo "📊 Test Data is stored in:"
echo "   src/test/resources/testdata/indodax_api_testdata.xlsx"
echo ""
echo "📝 Note: Full Cucumber tests available but require"
echo "   Maven dependencies to be fully configured."
