package com.psat.exercise;

public class ConcreteMessage implements Message {

	/**
	 * Reference to a {@link ResourceScheduler} so that later on can inform that
	 * message has been processed
	 */
	private ResourceScheduler	scheduler;

	private String				groupID;
	private String				content;

	/**
	 * Making the default constructor private - Do not want to create
	 * {@link ConcreteMessage} objects
	 * without association with a {@link ResourceScheduler}
	 */
	@SuppressWarnings("unused")
	private ConcreteMessage() {
		throw new AssertionError();
	}

	/**
	 * Creates a {@link ConcreteMessage} object associated with a
	 * {@link ResourceScheduler}
	 *
	 * @param scheduler
	 */
	public ConcreteMessage(ResourceScheduler scheduler, final String groupID, final String content)
			throws NullPointerException {
		// make sure that we are not getting a null object
		if (scheduler == null || groupID == null || content == null) {
			throw new NullPointerException("Provided ResourceScheduler object is null");
		}

		this.scheduler = scheduler;
		this.groupID = groupID;
		this.content = content;
	}

	@Override
	public void completed() {
		// inform the scheduler that the message has finished
		// meaning that the resource is free to take another message if
		// available
		System.out.println("Message Completed");
		scheduler.putResourceIdle();
	}

	public String getGroupID() {
		return this.groupID;
	}

	public String getContent() {
		return this.content;
	}
}
