@api @indodax @advanced
Feature: Advanced API Testing Features
  As a QA Engineer
  I want to demonstrate advanced API testing capabilities

  @positive @chaining
  Scenario: API Test Chaining - Store and reuse data from response
    Given I send GET request to "/ticker/btcidr"
    Then the response status code should be 200
    When I store response value from "ticker.last" as "lastPrice"
    And I store response value from "ticker.high" as "highPrice"
    Then the response body should contain field "ticker"

  @positive @chaining @calculation
  Scenario: Advanced - API Chaining with Calculation Validation
    Given I send GET request to "/ticker/btcidr"
    Then the response status code should be 200
    When I store response value from "ticker.last" as "btc_last"
    And I store response value from "ticker.high" as "btc_high"
    And I store response value from "ticker.low" as "btc_low"
    Then the volume calculation should be correct
    And the stored variable "btc_high" should be greater than or equal to stored variable "btc_low"

    When I send GET request to "/ticker/ethidr"
    Then the response status code should be 200
    When I store response value from "ticker.last" as "eth_last"
    And I store response value from "ticker.high" as "eth_high"
    And I store response value from "ticker.low" as "eth_low"
    Then the volume calculation should be correct
    And the stored variable "eth_high" should be greater than or equal to stored variable "eth_low"
    And the stored variable "btc_last" should be greater than stored variable "eth_last"

  @positive @calculation
  Scenario: Data Calculation Validation - Volume calculation
    Given I send GET request to "/ticker/btcidr"
    Then the response status code should be 200
    And the volume calculation should be correct

  @positive @environment
  Scenario: Environment Management - Verify production environment
    Given I send GET request to "/server_time"
    Then the response status code should be 200
    And the current environment should be "indodax.com"

  @positive @http-methods
  Scenario: HTTP Methods Support - GET request
    When I send GET request to "/server_time"
    Then the response status code should be 200
    And the response body should contain field "timezone"
    And the response body should contain field "server_time"

  @positive @jsonpath
  Scenario: Response Validation by JSON Path - Multiple field checks
    When I send GET request to "/ticker/ethidr"
    Then the response status code should be 200
    And the response body should contain field "ticker.high"
    And the response body should contain field "ticker.low"
    And the response body should contain field "ticker.last"
    And the response body should contain field "ticker.buy"
    And the response body should contain field "ticker.sell"

  @positive @schema
  Scenario: Response Schema Validation - Ticker endpoint
    When I send GET request to "/ticker/usdtidr"
    Then the response status code should be 200
    And the response should match json schema "ticker_schema.json"
    And the ticker data should have valid structure

  @positive @performance
  Scenario: Performance Testing - Response time validation
    When I send GET request to "/server_time"
    Then the response status code should be 200
    And the response time should be less than 3000 ms

  @positive @data-validation
  Scenario: Data Validation - Price comparison
    When I send GET request to "/ticker/btcidr"
    Then the response status code should be 200
    And the high price should be greater than or equal to low price

  @positive @chaining @calculation @advanced
  Scenario: Complex - Multi-Pair Price Analysis with Chaining
    Given I send GET request to "/ticker/btcidr"
    Then the response status code should be 200
    When I store response value from "ticker.last" as "btc_price"
    And I store response value from "ticker.high" as "btc_high"
    And I store response value from "ticker.low" as "btc_low"
    Then the volume calculation should be correct
    And the stored variable "btc_high" should be greater than or equal to stored variable "btc_low"

    When I send GET request to "/ticker/ethidr"
    Then the response status code should be 200
    When I store response value from "ticker.last" as "eth_price"
    And I store response value from "ticker.high" as "eth_high"
    And I store response value from "ticker.low" as "eth_low"
    Then the volume calculation should be correct
    And the stored variable "eth_high" should be greater than or equal to stored variable "eth_low"

    When I send GET request to "/ticker/usdtidr"
    Then the response status code should be 200
    And the response should match json schema "ticker_schema.json"
    When I store response value from "ticker.last" as "usdt_price"
    And I store response value from "ticker.high" as "usdt_high"
    And I store response value from "ticker.low" as "usdt_low"
    Then the volume calculation should be correct
    And the stored variable "usdt_high" should be greater than or equal to stored variable "usdt_low"
    And the ticker data should have valid structure
    And the stored variable "btc_price" should be greater than stored variable "eth_price"
    And the stored variable "eth_price" should be greater than stored variable "usdt_price"
