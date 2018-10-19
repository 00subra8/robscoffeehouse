Feature: Feature to test order function of Rob's Coffee House

  Scenario: Happy path - Successfully get report with correct endpoint
    When GET Request is sent for report url "/report" then "200" http status response is received

  Scenario: Pessimistic path - Fail to get report with incorrect endpoint
    When GET Request is sent for report url "/reportMe" then "400" http status response is received

