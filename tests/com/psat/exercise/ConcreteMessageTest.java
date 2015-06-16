package com.psat.exercise;

import org.junit.Test;

/**
 * Class to test {@link ConcreteMessage} class
 *
 * @author Sérgio Teixeira
 * @date 16/06/2015 23:16:44
 */
public class ConcreteMessageTest {

	/**
	 * Test that a {@link ConcreteMessage} object cannot be created with a
	 * <code>null</code> {@link ResourceScheduler} object
	 */
	@Test(expected = NullPointerException.class)
	public void testConcreteMessageCreationWithNullResourceScheduler() {
		new ConcreteMessage(null, "group1", "a message");
	}

	/**
	 * Test that a {@link ConcreteMessage} object cannot be created with a
	 * <code>null</code> groupID
	 */
	@Test(expected = NullPointerException.class)
	public void testConcreteMessageCreationWithNullGroupID() {
		ResourceScheduler scheduler = new ResourceScheduler();
		new ConcreteMessage(scheduler, null, "a message");

	}

	/**
	 * Test that a {@link ConcreteMessage} object cannot be created with a
	 * <code>null</code> content
	 */
	@Test(expected = NullPointerException.class)
	public void testConcreteMessageCreationWithNullContent() {
		ResourceScheduler scheduler = new ResourceScheduler();
		new ConcreteMessage(scheduler, "group1", null);
	}

	/**
	 * Test that creation of message does not throw a
	 * {@link NullPointerException} when
	 * it has no <code>null</code> parameters
	 *
	 * @throws NullPointerException
	 */
	@Test
	public void testConcreteMessageCreationWithResourceScheduler() throws NullPointerException {
		ResourceScheduler scheduler = new ResourceScheduler(3);
		new ConcreteMessage(scheduler, "group1", "a message");
	}
}
