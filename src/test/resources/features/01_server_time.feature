@api @indodax @server-time
Feature: Indodax Server Time API
  As a API consumer
  I want to get server time from Indodax API
  So that I can sync my application time with server

  Background:
    Given I load test data from excel file "indodax_api_testdata.xlsx" sheet "ServerTime"

  @positive @smoke
  Scenario: API-ST-001 - Get Server Time - Valid Request
    Given I get test data for test case "API-ST-001"
    When I send GET request to server time endpoint
    Then the response status code should match expected from test data
    And the response should match json schema "server_time_schema.json"
    And the response body should contain field "timezone"
    And the response body should contain field "server_time"
    And the response body field "server_time" should not be empty
    And the response time should be less than 3000 ms

  @positive
  Scenario: API-ST-002 - Server Time - Response Format
    Given I get test data for test case "API-ST-002"
    When I send GET request to server time endpoint
    Then the response status code should match expected from test data
    And the response body should contain field "timezone"
    And the response body field "timezone" should not be empty

  @positive
  Scenario: API-ST-003 - Server Time - Response Time
    Given I get test data for test case "API-ST-003"
    When I send GET request to server time endpoint
    Then the response status code should match expected from test data
    And the response time should be less than 3000 ms
