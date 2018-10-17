Feature: Feature to test add coffee variety function of Rob's Coffee House

  Scenario: Happy path - Successfully Add a coffee variety
    When Post Request with below coffee variety values is requested to get "200" http status response
      | Name  | Description         | Available Quantity |
      | Mocha | No Milk Only Coffee | 65                 |

  Scenario: Pessimistic path - Request with invalid info gets a bad request as response
    When Post Request with below invalid variety name is requested to get "400" http status response
      | Name | Description         | Available Quantity |
      |      | No Milk Only Coffee | 65                 |
    And Post Request with below invalid available quantity value is requested to get "400" http status response
      | Name  | Description         | Available Quantity |
      | Mocha | No Milk Only Coffee | -89                |