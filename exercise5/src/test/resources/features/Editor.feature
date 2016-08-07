Feature: Editor

  Scenario: As a <User>, I want to <start the editor> in order to <use it>.
    Given I have just started the editor,
    Then the document name is "Unnamed",
    And the document is "",
    And the window title is "Editor: Unnamed".

  Scenario: As a <User>, I want to <enter text> in order to <create and edit content which is textual data>.
    Given I have just started the editor,
    When I enter the text "foo",
    Then the document is "foo".

  Scenario: As a <User>, I want to <create a new file> in order to <create new content from scratch>.
    Given I have just started the editor,
    And I enter the text "foo",
    When I action "new",
    Then the document is "".

  Scenario: As a <User>, I want to <save a file> in order to <persist my work>.
    Given I have just started the editor,
    And a file "foo.txt" does not exist,
    And I enter the text "This is some text.",
    When I action "save",
    Then I am asked for a filename.
    When I enter the filename "foo.txt",
    Then a file "foo.txt" exists,
    And the file "foo.txt" contains the text "This is some text.".
