# Resource Schedule Exercise
###### Thinking Walk-through

Review exercise's document and:

*   get a first idea of what the problem is
*   retrieve the key requirements
*	get the nouns (classes) and the verbs (methods/actions)

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
