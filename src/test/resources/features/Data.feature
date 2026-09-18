Feature: Dataset table view (UC_6)

  Scenario: Table successfully loads all bins (AT_13)
    Given the user is not viewing the dataset table view
    When they open the data page
    Then all bins are shown in a table