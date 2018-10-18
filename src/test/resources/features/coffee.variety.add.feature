Feature: Feature to test add coffee variety function of Rob's Coffee House

  Scenario: Happy path - Successfully Add a coffee variety
    When Post Request with below coffee variety values is requested to get "200" http status response
      | Name  | Description         | Available Quantity | Price |
      | Mocha | No Milk Only Coffee | 65                 | 5.50  |

  Scenario: Pessimistic path - Request with invalid info gets a bad request as response
    When Post Request with below invalid variety name is requested to get "400" http status response
      | Name | Description         | Available Quantity | Price |
      |      | No Milk Only Coffee | 65                 | 2.50  |
    And Post Request with below invalid available quantity value is requested to get "400" http status response
      | Name  | Description         | Available Quantity | Price |
      | Mocha | No Milk Only Coffee | -89                | 10    |
    And Post Request with below invalid price value is requested to get "400" http status response
      | Name         | Description               | Available Quantity | Price |
      | Double Mocha | No Milk Only Coffee TWICE | 65                 | 12.5N |