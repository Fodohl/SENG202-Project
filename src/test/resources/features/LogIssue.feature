Feature: Report issue with bin condition (UC_4)

  Scenario: Submitting a valid issue report (AT_9)
    Given the user enters a valid issue report
    When they select submit report
    Then the report is added to the user's timeline and appears on the bin's details

  Scenario: Submitting an invalid issue report (AT_10)
    Given the user enters an invalid issue report
    When they select submit report
    Then the report is rejected and an error message is shown to the user