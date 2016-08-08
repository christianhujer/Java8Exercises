Feature: Editor

  Scenario: As a <User>, I want to <start the editor> in order to <use it>.
    Given I have just started the editor,
    Then the document name must be "<Unnamed>",
    And the document must have the following content:
      """
      """
    And the window title must be "Editor: <Unnamed>".

  Scenario: As a <User>, I want to <enter text> in order to <create and edit content which is textual data>.
    Given I have just started the editor,
    When I enter the text "foo",
    Then the document must have the following content:
      """
      foo
      """

  Scenario: As a <User>, I want to <create a new file> in order to <create new content from scratch>.
    Given I have just started the editor,
    And I enter the text "foo",
    When I wait for action "new",
    Then the document must have the following content:
      """
      """
    And the document name must be "<Unnamed>",
    And the window title must be "Editor: <Unnamed>".

  Scenario: As a <User>, I want to <save a file> in order to <persist my work>.
    Given I have just started the editor,
    And the file "foo.txt" does not exist,
    And I enter the text "This is some text.",
    When I action "save",
    Then I must be asked for a filename.
    When I enter the filename "foo.txt",
    And I wait for I/O to be completed,
    Then the file "foo.txt" must have the following content:
      """
      This is some text.
      """
    And the document name must be "foo.txt",
    When I enter the text " This is some more text.",
    When I action "save",
    Then I must not be asked for a filename,
    When I wait for I/O to be completed,
    Then the file "foo.txt" must have the following content:
      """
      This is some text. This is some more text.
      """

  Scenario: As a <User>, I want to <load a file> in order to <continue my work>.
    Given I have just started the editor,
    And the file "foo.txt" has the following content:
      """
      Hello, World!
      """
    When I action "open",
    Then I must be asked for a filename,
    When I enter the filename "foo.txt",
    And I wait for I/O to be completed,
    Then the document must have the following content:
      """
      Hello, World!
      """
    And the document name must be "foo.txt".

  Scenario: As a <User>, I want to <save a file under a new name> in order to <create derived work>.
    Given I have just started the editor,
    And the file "bar.txt" does not exist,
    And the file "foo.txt" has the following content:
      """
      Hello, World!
      """
    When I action "open",
    Then I must be asked for a filename,
    When I enter the filename "foo.txt",
    And I wait for I/O to be completed,
    And I action "saveAs",
    Then I must be asked for a filename,
    When I enter the filename "bar.txt",
    And I wait for I/O to be completed,
    Then the file "bar.txt" must have the following content:
      """
      Hello, World!
      """
