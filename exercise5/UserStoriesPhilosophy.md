# User Story aren't always User Stories

User Stories are often written in the form of a sentence filling a template like the following:
| As a <Role>, I want to <action> in order to <purpose>.
I think that this is a good template for User Stories.
But it doesn't mean that just by pressing your requirement into that template you automatically have a User Story.

Your requirement could be something else.
Not every requirement which sounds like a User Story is a User Story.

I categorize requirements which sound like User Stories into four groups:
* Epics
* (Proper) User Stories
* Acceptance Criteria (of other User Stories)
* Definition of Done

Please note that the examples given here are just rough guidelines.
In an ideal world, a User Story would always reflect work at a granularity that delivers true business value.
True business value is value in the eyes of customers and users.

Experience, environment, tools, libraries and existing code influence are just some of the many factors which influence the speed at a team adds value.
Each team faces their own situation influenced by these factors.

Therefore, the examples given below should be seen as guidelines, not as hard rules.
The scale applies, but it might have to shift to the bigger or smaller depending on the speed of the team.

At the same time I would invite teams that move so slow that they cannot apply the scale described in the examples to think:
There are impediments which slow you down, like viscosity.


## Epics
| As a <User>, I want to <edit Markdown files> in order to <work with a convenient format capturing 80% of day-to-day editing requirements>.
This sounds like a User Story, but it's actually very big and can be broken down into various user stories

## (Proper) User Stories
| As a <User>, I want to <save a file> in order to <persist my work>.
This is a clear user story.
It is end-to-end, and it can be easily implemented.
Of course there are a few details to clarify to get the story ready and done, like:
* file format, file encoding
* menu position, mnemonic, icon and text; toolbar position and icon; tooltip text; keystroke

## Acceptance Criteria (of other User Stories)
| As a <User>, I want to <trigger save with Ctrl+S> in order to <work more efficiently>.

This is usually too small for a user story.
It should rather be an acceptance criteria of the bigger User Story which talks about saving a file:

| Given I have a document with file name,
| When I press "ctrl pressed S",
| Then I trigger the "save" action.

| Given I have a document without file name,
| When I press "ctrl pressed S",
| Then I trigger the "saveAs" action.

## Definition of Done
| As a <User>, I want to <trigger actions via keystrokes> in order to <work more efficiently>.

While it sounds like a User Story, it actually isn't.
Obviously it is orthogonal to stories, and it's part of the Acceptance.
It isn't applicable to a single User Story or a very particular small subset of User Stories.
It is applicable to an entire category of User Stories.
In a GUI application, it is probably even the majority of User Stories.
Therefore, this requirement is best captured as a Definition of Done.

Also, this element of the Definition of Done feeds back to the Definition of Ready:
"The User Story specifies the keystrokes for its actions."
