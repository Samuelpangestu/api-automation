@api @indodax @price-increments
Feature: Indodax Price Increments API
  As a API consumer
  I want to get price increments for all pairs
  So that I can validate price precision

  Background:
    Given I load test data from excel file "indodax_api_testdata.xlsx" sheet "PriceIncrements"

  @positive @smoke
  Scenario: API-PI-001 - Get Price Increments
    Given I get test data for test case "API-PI-001"
    When I send GET request to price increments endpoint
    Then the response status code should match expected from test data
    And the response body should contain field "increments"
    And the increments data should not be empty
    And the response time should be less than 3000 ms

  @positive
  Scenario: API-PI-002 - Increments Data Type
    Given I get test data for test case "API-PI-002"
    When I send GET request to price increments endpoint
    Then the response status code should match expected from test data
    And the response body should contain field "increments"
    And the increments data should not be empty
