package com.psat.exercise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.Test;

import com.psat.exercise.ResourceScheduler.GroupStatus;
import com.psat.exercise.prioritisation.GroupPriority;

/**
 * Class to test {@link GroupPriority} class logic
 *
 * @author Sérgio Teixeira
 * @date 17/06/2015 09:22:39
 */
public class GroupPriorityTest {

	/**
	 * Test that a {@link GroupPriority} object cannot be created with a
	 * <code>null</code> hahtable
	 */
	@Test(expected = NullPointerException.class)
	public void testGroupPriorityCreationWithNullHashTable() {
		new GroupPriority(null);
	}

	/**
	 * Test that creation of {@link GroupPriority} does not throw a
	 * {@link NullPointerException} when
	 * it has no <code>null</code> parameters
	 *
	 * @throws NullPointerException
	 */
	@Test
	public void testGroupPriorityCreationWithNoNull() throws NullPointerException {
		new GroupPriority(new LinkedHashMap<String, GroupStatus>());
	}

	/**
	 * Test the prioritisation logic for groups
	 */
	@Test
	public void testGroupPrioritisation() {
		ResourceScheduler scheduler = new ResourceScheduler();

		// create groups with state and add to a GroupPriority object
		LinkedHashMap<String, GroupStatus> groups = new LinkedHashMap<String, GroupStatus>();
		groups.put("group1", GroupStatus.ADDED);
		groups.put("group2", GroupStatus.IN_PROGRESS);
		GroupPriority priority = new GroupPriority(groups);

		// Create a list of two messages that match the above groups
		LinkedList<Message> list = new LinkedList<Message>();
		Message msg = new ConcreteMessage(scheduler, "group1", "msg1");
		Message msg2 = new ConcreteMessage(scheduler, "group2", "msg1");
		list.add(msg);
		list.add(msg2);

		// should return group2 message since its in progress
		Message pickedMsg = priority.pickNextPriorityMessage(list);
		assertEquals(msg2, pickedMsg);

		// init groups again - assume here this is an initial state
		// now that groups 1 is in progress should return it
		groups.put("group1", GroupStatus.IN_PROGRESS);
		groups.put("group2", GroupStatus.IN_PROGRESS);
		pickedMsg = priority.pickNextPriorityMessage(list);
		assertEquals(msg, pickedMsg);
		// here list should be empty since the priority polls the Message from
		// the list, and at this time it should have polled 2
		assertTrue("List is not empty", list.isEmpty());

		// add elements to the list again
		list.add(msg);
		list.add(msg2);
		LinkedHashMap<String, GroupStatus> groups1 = new LinkedHashMap<String, GroupStatus>();
		groups1.put("group1", GroupStatus.IN_PROGRESS);
		groups1.put("group2", GroupStatus.ADDED);
		GroupPriority priority1 = new GroupPriority(groups1);
		pickedMsg = priority1.pickNextPriorityMessage(list);
		assertEquals(msg, pickedMsg);

		// add another message for group2
		Message msg3 = new ConcreteMessage(scheduler, "group2", "msg2");
		list.add(msg3);
		groups1.remove("group1");
		groups1.put("group2", GroupStatus.IN_PROGRESS);
		pickedMsg = priority1.pickNextPriorityMessage(list);
		assertEquals(msg2, pickedMsg);
		pickedMsg = priority1.pickNextPriorityMessage(list);
		assertEquals(msg3, pickedMsg);
		assertTrue("List is not empty", list.isEmpty());
	}

}
