@vehicleCheck
Feature: Compare Car details

  Scenario Outline: compare car details with valuation site
    Given fetch vehicle reg numbers from "<input>" file
    When  fetch vehicle details from vehicle valuation site
    Then  Compare the vehicle details with "<output>" file
    Examples:
      | input    | output    |
      | car_input - V5.txt | car_output - V5.txt |
