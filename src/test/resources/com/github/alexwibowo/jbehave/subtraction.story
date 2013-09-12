Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: simple addition of two numbers
Given I have number '5'
When I subtract '1' from it
Then I should get '4'

Scenario: simple addition of three numbers
Given I have number '5'
When I subtract '2' from it
And I subtract '1' from it
Then I should get '2'