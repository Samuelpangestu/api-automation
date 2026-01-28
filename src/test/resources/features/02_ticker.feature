@api @indodax @ticker
Feature: Indodax Ticker API
  As a API consumer
  I want to get ticker information for trading pairs
  So that I can display current market prices

  Background:
    Given I load test data from excel file "indodax_api_testdata.xlsx" sheet "Ticker"

  @positive @smoke
  Scenario Outline: Get ticker for <pair> pair
    Given I get test data for test case "<testCase>"
    When I send GET request to ticker endpoint with pair from test data
    Then the response status code should match expected from test data
    And the response should match json schema "ticker_schema.json"
    And the ticker data should have valid structure
    And the high price should be greater than or equal to low price
    And the response time should be less than 3000 ms

    Examples:
      | testCase    | pair    |
      | API-TK-001  | btcidr  |
      | API-TK-002  | ethidr  |
      | API-TK-003  | usdtidr |
