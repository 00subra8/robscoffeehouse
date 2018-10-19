Feature: Feature to test order function of Rob's Coffee House

  Scenario: Happy path - Successfully Place an order
    When Post Request with below order values is requested to get "200" http status response
      | Customer Phone Number | Item List1 | Item List2 |
      | 1234567888            | Mocha,2    | Chaii,2    |


  Scenario: Pessimistic path - Place an order
    When Post Request with invalid customer phone number is requested to get "400" http status response
      | Customer Phone Number | Item List1 | Item List2 |
      | 123456S7888           | Mocha,2    | Chaii,2    |
    When Post Request with invalid quantity is requested to get "400" http status response
      | Customer Phone Number | Item List1 | Item List2 |
      | 1234567888            | Mocha,-2   | Chaii,2    |
    When Post Request with invalid customer variety is requested to get "400" http status response
      | Customer Phone Number | Item List1 | Item List2 |
      | 1234567888            | Beer,2     | Chaii,2    |

