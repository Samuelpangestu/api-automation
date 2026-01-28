@api @indodax @pairs
Feature: Indodax Trading Pairs API
  As a API consumer
  I want to get all available trading pairs
  So that I can display supported cryptocurrencies

  Background:
    Given I load test data from excel file "indodax_api_testdata.xlsx" sheet "TradingPairs"

  @positive @smoke
  Scenario: API-PR-001 - Get All Trading Pairs
    Given I get test data for test case "API-PR-001"
    When I send GET request to pairs endpoint
    Then the response status code should match expected from test data
    And the pairs list should not be empty
    And each pair should have required fields
    And the response time should be less than 3000 ms

  @positive
  Scenario: API-PR-002 - Pairs Data Structure
    Given I get test data for test case "API-PR-002"
    When I send GET request to pairs endpoint
    Then the response status code should match expected from test data
    And each pair should have required fields
