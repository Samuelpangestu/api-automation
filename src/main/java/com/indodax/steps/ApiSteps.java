package com.indodax.steps;

import com.indodax.api.client.IndodaxApiClient;
import com.indodax.helper.ExcelReader;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ApiSteps {
    private static final Logger logger = LoggerFactory.getLogger(ApiSteps.class);

    private IndodaxApiClient apiClient;
    private Response response;
    private ExcelReader excelReader;
    private Map<String, String> testData;

    public ApiSteps() {
        this.apiClient = new IndodaxApiClient();
    }

    @Step("Load test data from Excel file: {fileName}, sheet: {sheetName}")
    @Given("I load test data from excel file {string} sheet {string}")
    public void loadTestDataFromExcel(String fileName, String sheetName) {
        String filePath = "src/test/resources/testdata/" + fileName;
        excelReader = new ExcelReader(filePath);
        excelReader.selectSheet(sheetName);
        Allure.addAttachment("Excel File", fileName);
        Allure.addAttachment("Sheet Name", sheetName);
        logger.info("Loaded test data from: {} - Sheet: {}", fileName, sheetName);
    }

    @Step("Get test data for test case: {testCaseName}")
    @Given("I get test data for test case {string}")
    public void getTestDataForTestCase(String testCaseName) {
        testData = excelReader.getTestDataByName(testCaseName);
        Allure.addAttachment("Test Case ID", testCaseName);
        Allure.addAttachment("Test Data", testData.toString());
        logger.info("Test data loaded for: {}", testCaseName);
        logger.info("Test data: {}", testData);
    }

    @Step("Send GET request to /api/server_time")
    @When("I send GET request to server time endpoint")
    public void sendGetServerTime() {
        response = apiClient.getServerTime();
        attachResponseToAllure();
    }

    @Step("Send GET request to /api/ticker/{pair}")
    @When("I send GET request to ticker endpoint with pair {string}")
    public void sendGetTicker(String pair) {
        response = apiClient.getTicker(pair);
        attachResponseToAllure();
    }

    @Step("Send GET request to ticker endpoint with pair from test data")
    @When("I send GET request to ticker endpoint with pair from test data")
    public void sendGetTickerFromTestData() {
        String pair = testData.get("Pair");
        Allure.parameter("Trading Pair", pair);
        response = apiClient.getTicker(pair);
        attachResponseToAllure();
    }

    @Step("Send GET request to /api/tickers")
    @When("I send GET request to all tickers endpoint")
    public void sendGetAllTickers() {
        response = apiClient.getAllTickers();
        attachResponseToAllure();
    }

    @Step("Send GET request to /api/pairs")
    @When("I send GET request to pairs endpoint")
    public void sendGetPairs() {
        response = apiClient.getPairs();
        attachResponseToAllure();
    }

    @Step("Send GET request to /api/price_increments")
    @When("I send GET request to price increments endpoint")
    public void sendGetPriceIncrements() {
        response = apiClient.getPriceIncrements();
        attachResponseToAllure();
    }

    @Step("Send GET request to /api/summaries")
    @When("I send GET request to summaries endpoint")
    public void sendGetSummaries() {
        response = apiClient.getSummaries();
        attachResponseToAllure();
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        assertEquals("Expected status code " + expectedStatusCode + " but got " + response.getStatusCode(),
                expectedStatusCode, response.getStatusCode());
        logger.info("Status code verified: {}", expectedStatusCode);
    }

    @Then("the response status code should match expected from test data")
    public void verifyStatusCodeFromTestData() {
        int expectedStatus = Integer.parseInt(testData.get("Expected Status"));
        assertEquals("Status code mismatch", expectedStatus, response.getStatusCode());
        logger.info("Status code verified from test data: {}", expectedStatus);
    }

    @Then("the response body should contain field {string}")
    public void verifyResponseContainsField(String fieldName) {
        assertNotNull("Response should contain field: " + fieldName,
                response.jsonPath().get(fieldName));
        logger.info("Response contains field: {}", fieldName);
    }

    @Then("the response body field {string} should not be empty")
    public void verifyFieldNotEmpty(String fieldName) {
        Object fieldValue = response.jsonPath().get(fieldName);
        assertNotNull("Field " + fieldName + " should not be null", fieldValue);
        assertThat(fieldValue.toString(), not(emptyString()));
        logger.info("Field {} is not empty: {}", fieldName, fieldValue);
    }

    @Then("the response should match json schema {string}")
    public void verifyJsonSchema(String schemaFileName) {
        File schemaFile = new File("src/test/resources/schemas/" + schemaFileName);
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
        logger.info("Response matches JSON schema: {}", schemaFileName);
    }

    @Then("the ticker data should have valid structure")
    public void verifyTickerStructure() {
        assertNotNull("Response should contain ticker", response.jsonPath().get("ticker"));
        assertNotNull("Ticker should have high price", response.jsonPath().get("ticker.high"));
        assertNotNull("Ticker should have low price", response.jsonPath().get("ticker.low"));
        assertNotNull("Ticker should have last price", response.jsonPath().get("ticker.last"));
        assertNotNull("Ticker should have buy price", response.jsonPath().get("ticker.buy"));
        assertNotNull("Ticker should have sell price", response.jsonPath().get("ticker.sell"));
        logger.info("Ticker structure validated successfully");
    }

    @Then("the high price should be greater than or equal to low price")
    public void verifyHighLowPrice() {
        String highStr = response.jsonPath().getString("ticker.high");
        String lowStr = response.jsonPath().getString("ticker.low");

        double high = Double.parseDouble(highStr);
        double low = Double.parseDouble(lowStr);

        assertTrue("High price should be >= low price", high >= low);
        logger.info("Price validation passed - High: {}, Low: {}", high, low);
    }

    @Then("the pairs list should not be empty")
    public void verifyPairsNotEmpty() {
        assertThat(response.jsonPath().getList("$"), not(empty()));
        int count = response.jsonPath().getList("$").size();
        logger.info("Pairs count: {}", count);
    }

    @Then("each pair should have required fields")
    public void verifyPairFields() {
        assertNotNull(response.jsonPath().get("[0].id"));
        assertNotNull(response.jsonPath().get("[0].symbol"));
        assertNotNull(response.jsonPath().get("[0].base_currency"));
        assertNotNull(response.jsonPath().get("[0].traded_currency"));
        logger.info("Pair fields validated");
    }

    @Then("the increments data should not be empty")
    public void verifyIncrementsNotEmpty() {
        Map<String, Object> increments = response.jsonPath().getMap("increments");
        assertFalse("Increments should not be empty", increments.isEmpty());
        logger.info("Increments count: {}", increments.size());
    }

    @Then("the response time should be less than {int} ms")
    public void verifyResponseTime(long maxTime) {
        long responseTime = response.getTime();
        assertTrue("Response time " + responseTime + "ms should be less than " + maxTime + "ms",
                responseTime < maxTime);
        logger.info("Response time: {}ms", responseTime);
    }

    @After
    public void cleanup() {
        if (excelReader != null) {
            excelReader.close();
        }
    }

    // ============================================
    // HTTP Methods Support
    // ============================================

    @Step("Send POST request to {endpoint} with body")
    @When("I send POST request to {string} with body:")
    public void sendPostRequest(String endpoint, String jsonBody) {
        Map<String, Object> body = parseJsonToMap(jsonBody);
        response = apiClient.sendPost(endpoint, body);
        attachResponseToAllure();
    }

    @Step("Send PUT request to {endpoint} with body")
    @When("I send PUT request to {string} with body:")
    public void sendPutRequest(String endpoint, String jsonBody) {
        Map<String, Object> body = parseJsonToMap(jsonBody);
        response = apiClient.sendPut(endpoint, body);
        attachResponseToAllure();
    }

    @Step("Send DELETE request to {endpoint}")
    @When("I send DELETE request to {string}")
    public void sendDeleteRequest(String endpoint) {
        response = apiClient.sendDelete(endpoint);
        attachResponseToAllure();
    }

    @Step("Send PATCH request to {endpoint} with body")
    @When("I send PATCH request to {string} with body:")
    public void sendPatchRequest(String endpoint, String jsonBody) {
        Map<String, Object> body = parseJsonToMap(jsonBody);
        response = apiClient.sendPatch(endpoint, body);
        attachResponseToAllure();
    }

    @Step("Send GET request to {endpoint}")
    @When("I send GET request to {string}")
    public void sendGetRequest(String endpoint) {
        response = apiClient.sendGet(endpoint);
        attachResponseToAllure();
    }

    // ============================================
    // API Test Chaining Support
    // ============================================

    @Step("Store response value from {jsonPath} as {variableName}")
    @When("I store response value from {string} as {string}")
    public void storeResponseValue(String jsonPath, String variableName) {
        apiClient.storeDataFromResponse(variableName, jsonPath);
        logger.info("Stored {} from {}", variableName, jsonPath);
    }

    @Step("Use stored variable {variableName} in request")
    @When("I use stored variable {string} in request body")
    public void useStoredVariable(String variableName) {
        Object value = apiClient.getChainedData(variableName);
        logger.info("Using stored variable: {} = {}", variableName, value);
    }

    @Step("Verify stored variable {variableName} exists")
    @Then("the stored variable {string} should exist")
    public void verifyStoredVariableExists(String variableName) {
        Object value = apiClient.getChainedData(variableName);
        assertNotNull("Stored variable " + variableName + " should exist", value);
        logger.info("Stored variable verified: {} = {}", variableName, value);
    }

    @Step("Compare stored variables: {var1} >= {var2}")
    @Then("the stored variable {string} should be greater than or equal to stored variable {string}")
    public void compareStoredVariablesGreaterOrEqual(String var1, String var2) {
        Object value1 = apiClient.getChainedData(var1);
        Object value2 = apiClient.getChainedData(var2);

        assertNotNull("Variable " + var1 + " should exist", value1);
        assertNotNull("Variable " + var2 + " should exist", value2);

        double num1 = Double.parseDouble(value1.toString());
        double num2 = Double.parseDouble(value2.toString());

        assertTrue(var1 + " (" + num1 + ") should be >= " + var2 + " (" + num2 + ")", num1 >= num2);
        logger.info("Comparison verified: {} ({}) >= {} ({})", var1, num1, var2, num2);
    }

    @Step("Compare stored variables: {var1} > {var2}")
    @Then("the stored variable {string} should be greater than stored variable {string}")
    public void compareStoredVariablesGreater(String var1, String var2) {
        Object value1 = apiClient.getChainedData(var1);
        Object value2 = apiClient.getChainedData(var2);

        assertNotNull("Variable " + var1 + " should exist", value1);
        assertNotNull("Variable " + var2 + " should exist", value2);

        double num1 = Double.parseDouble(value1.toString());
        double num2 = Double.parseDouble(value2.toString());

        assertTrue(var1 + " (" + num1 + ") should be > " + var2 + " (" + num2 + ")", num1 > num2);
        logger.info("Comparison verified: {} ({}) > {} ({})", var1, num1, var2, num2);
    }

    @Step("Compare stored variables: {var1} == {var2}")
    @Then("the stored variable {string} should equal stored variable {string}")
    public void compareStoredVariablesEqual(String var1, String var2) {
        Object value1 = apiClient.getChainedData(var1);
        Object value2 = apiClient.getChainedData(var2);

        assertNotNull("Variable " + var1 + " should exist", value1);
        assertNotNull("Variable " + var2 + " should exist", value2);

        assertEquals(var1 + " should equal " + var2, value1.toString(), value2.toString());
        logger.info("Comparison verified: {} == {}", var1, var2);
    }

    // ============================================
    // Data Calculation Validation
    // ============================================

    @Step("Verify calculated field {fieldName} equals {jsonPath1} + {jsonPath2}")
    @Then("the response field {string} should equal sum of {string} and {string}")
    public void verifyFieldEqualsSum(String fieldName, String jsonPath1, String jsonPath2) {
        double field = response.jsonPath().getDouble(fieldName);
        double value1 = response.jsonPath().getDouble(jsonPath1);
        double value2 = response.jsonPath().getDouble(jsonPath2);
        double expectedSum = value1 + value2;

        assertEquals("Field " + fieldName + " should equal sum of " + jsonPath1 + " + " + jsonPath2,
                expectedSum, field, 0.0001);
        logger.info("Calculation verified: {} = {} + {} = {}", fieldName, value1, value2, field);
    }

    @Step("Verify calculated field {fieldName} equals {jsonPath1} * {jsonPath2}")
    @Then("the response field {string} should equal product of {string} and {string}")
    public void verifyFieldEqualsProduct(String fieldName, String jsonPath1, String jsonPath2) {
        double field = response.jsonPath().getDouble(fieldName);
        double value1 = response.jsonPath().getDouble(jsonPath1);
        double value2 = response.jsonPath().getDouble(jsonPath2);
        double expectedProduct = value1 * value2;

        assertEquals("Field " + fieldName + " should equal product of " + jsonPath1 + " * " + jsonPath2,
                expectedProduct, field, 0.0001);
        logger.info("Calculation verified: {} = {} * {} = {}", fieldName, value1, value2, field);
    }

    @Step("Verify volume calculation: vol_idr = last * vol_crypto")
    @Then("the volume calculation should be correct")
    public void verifyVolumeCalculation() {
        String lastPrice = response.jsonPath().getString("ticker.last");
        String volIdr = response.jsonPath().getString("ticker.vol_idr");

        // Detect which volume field exists (vol_btc, vol_eth, vol_usdt, etc.)
        String volCrypto = response.jsonPath().getString("ticker.vol_btc");
        String cryptoName = "BTC";

        if (volCrypto == null) {
            volCrypto = response.jsonPath().getString("ticker.vol_eth");
            cryptoName = "ETH";
        }
        if (volCrypto == null) {
            volCrypto = response.jsonPath().getString("ticker.vol_usdt");
            cryptoName = "USDT";
        }

        assertNotNull("Volume crypto field should exist", volCrypto);

        double last = Double.parseDouble(lastPrice);
        double cryptoVol = Double.parseDouble(volCrypto);
        double idrVol = Double.parseDouble(volIdr);
        double expectedVolIdr = last * cryptoVol;

        // Allow 1% tolerance for calculation
        double tolerance = expectedVolIdr * 0.01;
        assertTrue("Volume calculation should be correct: vol_idr ~= last * vol_" + cryptoName.toLowerCase(),
                Math.abs(idrVol - expectedVolIdr) <= tolerance);
        logger.info("Volume calculation verified: {} ~= {} * {} ({})", idrVol, last, cryptoVol, cryptoName);
    }

    // ============================================
    // Environment Management
    // ============================================

    @Step("Verify current environment is {environment}")
    @Then("the current environment should be {string}")
    public void verifyEnvironment(String expectedEnv) {
        String currentEnv = IndodaxApiClient.getCurrentEnvironment();
        assertTrue("Environment should contain " + expectedEnv,
                currentEnv.contains(expectedEnv));
        logger.info("Environment verified: {}", currentEnv);
    }

    // ============================================
    // Helper Methods
    // ============================================

    /**
     * Helper method to attach API response details to Allure report
     */
    private void attachResponseToAllure() {
        if (response != null) {
            Allure.addAttachment("Status Code", String.valueOf(response.getStatusCode()));
            Allure.addAttachment("Response Time (ms)", String.valueOf(response.getTime()));
            Allure.addAttachment("Response Body", "application/json", response.getBody().asString());
            Allure.addAttachment("Response Headers", response.getHeaders().toString());
        }
    }

    /**
     * Parse JSON string to Map for request body
     */
    private Map<String, Object> parseJsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        // Simple JSON parsing (for production use proper JSON library)
        json = json.trim().replace("{", "").replace("}", "");
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                map.put(key, value);
            }
        }
        return map;
    }
}
