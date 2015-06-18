package com.psat.exercise;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.psat.exercise.ResourceScheduler.GroupStatus;
import com.psat.exercise.prioritisation.FIFOPriority;
import com.psat.exercise.prioritisation.Priority;

/**
 * Class to test {@link ResourceScheduler} class
 *
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
	@Test
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
		// now should be at the initial state
		assertEquals(3, scheduler.putResourceIdle());
		assertEquals(0, scheduler.getWorkingResources());
	}

	/**
	 * Test that the ResourceScheduler is created with an empty list of
	 * {@link Message} objects
	 */
	@Test
	public void testEmptyListMessagesScheduling() {
		ResourceScheduler scheduler = new ResourceScheduler();
		assertEquals(new LinkedList<Message>(), scheduler.getMessages());

		ResourceScheduler multiResourcesScheduler = new ResourceScheduler(3);
		assertEquals(new LinkedList<Message>(), multiResourcesScheduler.getMessages());
	}

	/**
	 * Test messages being schedule with the ResourceScheduler, basically test
	 * that a group of messages or message are
	 * set to the instance of ResourceScheduler
	 */
	@Test
	public void testMessagesScheduling() {
		// the methods to be tested are relatively straight forward but want to
		// make sure that I'm adding the messages correctly
		ResourceScheduler scheduler = new ResourceScheduler();
		List<Message> messages = new LinkedList<Message>();

		// construct 5 anonymous Message instances just to fill the list
		for (int i = 0; i < 5; i++) {
			messages.add(new Message() {

				@Override
				public void completed() {
					System.out.println("I do nothing for now");
				}

				@Override
				public String getGroupID() {
					return "";
				}

			});
		}

		scheduler.scheduleMessages(messages);
		assertEquals(messages, scheduler.getMessages());

		// add just one message
		Message message = new Message() {

			@Override
			public void completed() {
				System.out.println("I do nothing for now");
			}

			@Override
			public String getGroupID() {
				return "";
			}
		};

		scheduler.scheduleMessage(message);
		List<Message> scheduledMessages = scheduler.getMessages();
		assertEquals(scheduledMessages.size() - 1, scheduledMessages.lastIndexOf(message));
	}

	/**
	 * Test the flow for where the {@link ResourceScheduler} (future
	 * implementation of {@link Gateway}) sends a message to an idle resource
	 * and gets the
	 * completed callback - this isn't really an actual test, but probably a
	 * simulation of a simple, with no prioritisation nor checking for resources
	 * availability, working flow
	 */
	@Test
	public void testMessageForwardToGateWayAndMessageCompletion() {
		final ResourceScheduler scheduler = new ResourceScheduler();
		final Message msg = new ConcreteMessage(scheduler, "group2", "msg1");

		final Gateway gateway = new Gateway() {

			@Override
			public void send(Message message) {
				scheduler.putResourceWorking();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				message.completed();
			}
		};

		// simulate processing workload
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gateway.send(msg);
			}
		});

		assertEquals(0, scheduler.getWorkingResources());
		thread.start();
		try {
			thread.join(2500);
			assertEquals(1, scheduler.getWorkingResources());
			thread.join();
			assertEquals(0, scheduler.getWorkingResources());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = Exception.class)
	public void testGroupTermination() throws Exception {
		ResourceScheduler scheduler = new ResourceScheduler(2);
		// create groups with state and add to a GroupPriority object
		Priority priority = new FIFOPriority();
		scheduler.setPriority(priority);

		LinkedList<Message> list = new LinkedList<Message>();
		scheduler.getGroups().put("group3", GroupStatus.TERMINATED);
		list.add(new ConcreteMessage(scheduler, "group3", "msg1"));
		scheduler.scheduleMessages(list);
		scheduler.applyScheduling();
	}
}
