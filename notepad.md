# Resource Schedule Exercise
### Thinking Walk-through

Review exercise's document and:

* get a first idea of what the problem is
* retrieve the key requirements
* get the nouns (classes) and the verbs (methods/actions)

Make an initial draft design of the possible classes
So far got the following possible classes to be constructed:

* Message interface.
* Gateway interface.
* Resource - which should get the messages to be processed - just to simulate message processing.
* ConcreteMessage - which implements the Message interface.
* ResourceScheduler - which should be a good starting point for containing the resources list and messages queue.
* A good Gateway interface implementation candidate could probably be the ResourceScheduler class.

------------------------------------------------------------
Create a first test and test suite.

Probably should be enough to simulate processing inside the send message, instead of having a Resource class - not sure dough.

(May be a good idea to postpone implementation of Resource class to the future,
for now let's keep it simple with having counters).

Should be enough for now to control resources with two counters:

* assigned resources
* working resources

Create a test class for the ResourceScheduler, then:

* test the creation of the ResourceScheduler instance with the given number of resources to be assigned.
* test the messages being set or scheduled with the ResourceScheduler (made the skeleton of the testMessagesScheduling method but in fact wanted to finish up setting up the resources control counters - will commit later and after committing the resources stuff).

_Some of the methods have no javadoc associated and this is intentional, as I find that the method is quite simple and self-explanatory_
