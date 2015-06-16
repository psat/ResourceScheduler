package com.psat.exercise;

import org.junit.Test;

public class ConcreteMessageTest {

	/**
	 * Test that a {@link ConcreteMessage} object cannot created with a
	 * <code>null</code> {@link ResourceScheduler} object or a group id
	 */
	@Test(expected = NullPointerException.class)
	public void testConcreteMessageCreationWithNullResourceScheduler() {
		new ConcreteMessage(null, "group1", "a message");
	}

	@Test(expected = NullPointerException.class)
	public void testConcreteMessageCreationWithNullGroupID() {
		ResourceScheduler scheduler = new ResourceScheduler();
		new ConcreteMessage(scheduler, null, "a message");

	}

	@Test(expected = NullPointerException.class)
	public void testConcreteMessageCreationWithNullContent() {
		ResourceScheduler scheduler = new ResourceScheduler();
		new ConcreteMessage(scheduler, "group1", null);
	}

	/**
	 * Test that a message does not throw a {@link NullPointerException} when
	 * {@link ResourceScheduler} is not <code>null</code>
	 *
	 * @throws NullPointerException
	 */
	@Test
	public void testConcreteMessageCreationWithResourceScheduler() throws NullPointerException {
		ResourceScheduler scheduler = new ResourceScheduler(3);
		new ConcreteMessage(scheduler, "group1", "a message");
	}
}
