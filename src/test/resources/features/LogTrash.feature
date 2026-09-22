Feature: Log usage of bin (UC_3)

  Scenario: Successfully logging usage of bin (AT_7)
    Given the user is viewing a bin's details
    When they correctly log their usage
    Then the logging is saved in the user's timeline

  Scenario: Attempting to log usage of bin without completing proper fields (AT_8)
    Given the user logs their usage without filling out necessary information
    When they click submit
    Then the log is rejected and a message is shown to the user