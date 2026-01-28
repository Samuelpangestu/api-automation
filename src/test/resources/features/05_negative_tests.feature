@api @indodax @negative
Feature: Negative Test Cases
  As a QA Engineer
  I want to test API edge cases
  So that I can verify API behavior with unusual inputs

  @negative @ticker
  Scenario: Invalid trading pair - Returns error
    When I send GET request to ticker endpoint with pair "invalidpair"
    Then the response status code should be 200
    And the response body should contain field "error"

  @negative @ticker
  Scenario: Non-existent pair - Returns error
    When I send GET request to ticker endpoint with pair "xyz123"
    Then the response status code should be 200
    And the response body should contain field "error"

  @negative @ticker
  Scenario: Case sensitivity test - lowercase vs uppercase
    When I send GET request to ticker endpoint with pair "BTCIDR"
    Then the response status code should be 200
    And the response body should contain field "ticker"
