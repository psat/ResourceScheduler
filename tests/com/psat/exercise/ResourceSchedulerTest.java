package com.psat.exercise;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Sérgio Teixeira
 * @date 14/06/2015 21:37:02
 */
public class ResourceSchedulerTest {

	/**
	 * Test resources assignment when creating ResourceScheduler.
	 * Must test first case that should cover one resource assigned, then second
	 * case that should cover n resources
	 * assigned to the ResourceScheduler.
	 */
	@Test
	public void testResourcesAssignment() {
		// test for a single resource - set resource as 1 on default constructor
		ResourceScheduler scheduler = new ResourceScheduler();
		assertEquals(1, scheduler.getAssignedResources());

		// test for "n" number of resources
		int resources = 2;
		ResourceScheduler multiScheduler = new ResourceScheduler(2);
		assertEquals(resources, multiScheduler.getAssignedResources());
	}

	/**
	 * Test resources set to be working. The number of working resources must be
	 * lower or equal than the assigned resources
	 */
	@Test
	public void testResourcesSetToWork() {
		ResourceScheduler scheduler = new ResourceScheduler(3);
		scheduler.putResourceWorking();
		assertEquals(1, scheduler.getWorkingResources());
		assertEquals(2, scheduler.putResourceWorking());
		scheduler.putResourceWorking();
		assertEquals(3, scheduler.putResourceWorking());
	}

	/**
	 * Test resources set to be idle. The number of idle resources must be equal
	 * or higher that 0
	 */
	public void testResourceSetToIdle() {
		ResourceScheduler scheduler = new ResourceScheduler(3);
		// test setting resources to idle when there are no (0) working
		// resources - idle resources should be 3
		assertEquals(3, scheduler.putResourceIdle());
		assertEquals(0, scheduler.getWorkingResources());
		scheduler.putResourceWorking();
		scheduler.putResourceWorking();
		scheduler.putResourceWorking();
		scheduler.putResourceWorking();
		assertEquals(1, scheduler.putResourceIdle());
		scheduler.putResourceIdle();
		assertEquals(1, scheduler.getWorkingResources());
		assertEquals(0, scheduler.putResourceIdle());
	}
}
