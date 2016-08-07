Feature: Editor
  Scenario: Starting the Editor
    Given I have just started the editor,
    Then the document name is "Unnamed",
    Then the document is empty.