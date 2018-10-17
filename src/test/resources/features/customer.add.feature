Feature: Feature to test add customer function of Rob's Coffee House

  Scenario: Happy path - Successfully Add a customer
    When Post Request with below customer values is requested to get "200" http status response
      | Customer Name | Phone Number  |
      | Subramanian   | +449459453234 |

  Scenario: Pessimistic path - Request with invalid info gets a bad request as response
    When Post Request with below invalid phone number value is requested to get "400" http status response
      | Customer Name | Phone Number |
      | Subramanian   | +Subramanian |
    And Post Request with below invalid customer name value is requested to get "400" http status response
      | Customer Name | Phone Number  |
      | 449459453234  | +449459453234 |




