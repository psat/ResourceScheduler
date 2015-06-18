package com.psat.exercise;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.psat.exercise.prioritisation.FIFOPriority;
import com.psat.exercise.prioritisation.GroupPriority;
import com.psat.exercise.prioritisation.Priority;

/**
 * This class is responsible for scheduling messages that are to be sent to the
 * gateway, apply some prioritisation strategy and for managing the
 * assigned resources.
 *
 * @author Sérgio Teixeira
 * @date 15/06/2015 22:40:04
 */
public class ResourceScheduler implements Gateway {

	/**
	 * Enumeration for the Group States
	 *
	 * @author Sérgio Teixeira
	 * @date 17/06/2015 20:55:51
	 */
	public static enum GroupStatus {
		ADDED, IN_PROGRESS, CANCELLED, TERMINATED
	}

	/**
	 * Counter for keeping the currently assigned resources
	 */
	private int									assignedResources;

	/**
	 * Counter for keeping track of currently working resources, i.e, not idle
	 * <p>
	 * Setter for this attribute was left un-implemented. Must use
	 * {@link #putResourceWorking()} and {@link #putResourceIdle()} methods to
	 * modify this attribute.
	 */
	private int									workingResources;

	/**
	 * List of {@link Message} objects to be scheduled for processing
	 */
	private LinkedList<Message>					messages;

	/**
	 * Attribute to get random int's - to help simulate a workload for each
	 * message
	 */
	private final Random						rand	= new Random();

	/**
	 * Attribute to whom prioritisation will be delegated to
	 */
	private Priority							priority;

	private LinkedHashMap<String, GroupStatus>	groups;

	/**
	 * Default constructor. This will create an instance with assigned resources
	 * of 1
	 */
	public ResourceScheduler() {
		this(1);
	}

	/**
	 * Creates a ResourceScheduler instance initialised with a given number of
	 * resources
	 *
	 * @param resources
	 */
	public ResourceScheduler(int resources) {
		messages = new LinkedList<Message>();
		assignedResources = resources;
		// explicitly initialise to 0
		workingResources = 0;
		groups = new LinkedHashMap<>();
	}

	public int getAssignedResources() {
		return assignedResources;
	}

	public void setAssignedResources(int assignedResources) {
		this.assignedResources = assignedResources;
	}

	public int getWorkingResources() {
		return workingResources;
	}

	/**
	 * Put resource as working. Increase number of working resources.
	 * <p>
	 * The working resources will be at most equal to the assigned resources.
	 *
	 * @return the number of working resources
	 */
	public synchronized int putResourceWorking() {
		if (workingResources < assignedResources) {
			workingResources += 1;
		}

		return workingResources;
	}

	/**
	 * Put resource as idle. Decrease number of working resources.
	 * <p>
	 * The working resources will be at least equal to 0.
	 *
	 * @return the number of idle resources
	 */
	public synchronized int putResourceIdle() {
		if (workingResources > 0) {
			workingResources -= 1;
		}

		return assignedResources - workingResources;
	}

	/**
	 * Schedule the messages to be sent to the gateway to be processed by idle
	 * resources
	 *
	 * @param messages
	 */
	public void scheduleMessages(List<Message> messages) {
		this.messages.addAll(messages);
	}

	/**
	 * Schedule the message to be sent to the gateway to be processed by idle
	 *
	 * @param message
	 */
	public void scheduleMessage(Message message) {
		this.messages.add(message);
	}

	/**
	 * @return the list of messages to be processed
	 */
	public List<Message> getMessages() {
		return messages;
	}

	@Override
	public void send(final Message message) {
		System.out.println("Sending message: " + message.toString());
		// simulate processing workload - each message should take some amount
		// of time, slighty different from each other, hence generate values
		// from 1 to 6
		final int workload = rand.nextInt((9 - 1) + 1) + 1;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(workload * 1000);
					message.completed();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public LinkedHashMap<String, GroupStatus> getGroups() {
		return groups;
	}

	/**
	 * This method performs the processing work of this class, which are:\n
	 * <ul>
	 * <li>checking resources availability,</li>
	 * <li>deciding which messages are sent first by applying prioritisation
	 * (actually delegates the work to a {@link Priority} instance</li>
	 * <li>sending the messages to the {@link Gateway} which currently is itself
	 * </li>
	 * </ul>
	 */
	public void applyScheduling() throws Exception {
		if (messages.size() == 0) {
			System.out.println("There are no messages to process");
			return;
		}

		if (workingResources == assignedResources) {
			System.out.println("All resources working - Messages queued");

		} else {
			boolean reschedule = false;
			int idleResources = assignedResources - workingResources;

			for (int i = 0; i < idleResources; i++) {
				Message message = priority.pickNextPriorityMessage(messages);

				if (message != null) {
					String groupID = message.getGroupID();
					GroupStatus status = groups.get(groupID);

					if (GroupStatus.ADDED.equals(status)) {
						groups.put(groupID, GroupStatus.IN_PROGRESS);
					} else if (GroupStatus.CANCELLED.equals(status)) {
						System.out.println("Group Cancelled - Not processing: " + message.toString());
						reschedule = true;
						break;

					} else if (GroupStatus.TERMINATED.equals(status)) {
						throw new Exception("Terminated Group Message received");
					}

					putResourceWorking();
					send(message);
				}
			}

			if (reschedule) {
				applyScheduling();
			}
		}
	}

	/**
	 * Adds the message to the schedule and applies the scheduling processing.
	 * <p>
	 * Check {@link #applyScheduling()}
	 *
	 * @param message
	 */
	public void applyScheduling(Message message) throws Exception {
		scheduleMessage(message);
		applyScheduling();
	}

	/**
	 * Run a simulation for Fifo Prioritisation
	 */
	public static void runFifo() {
		System.out.println("Schedule FIFO Priority Execution");
		ResourceScheduler scheduler = new ResourceScheduler(2);
		scheduler.setPriority(new FIFOPriority());
		LinkedList<Message> list = new LinkedList<Message>();
		list.push(new ConcreteMessage(scheduler, "groupID1", "msg1"));
		list.push(new ConcreteMessage(scheduler, "groupID2", "msg1"));
		list.push(new ConcreteMessage(scheduler, "groupID2", "msg2"));

		try {
			scheduler.scheduleMessages(list);
			scheduler.applyScheduling();

			scheduler.applyScheduling(new ConcreteMessage(scheduler, "groupID1", "msg2"));
			scheduler.applyScheduling(new ConcreteMessage(scheduler, "groupID1", "msg3"));
			scheduler.applyScheduling(new ConcreteMessage(scheduler, "groupID3", "msg1"));
			scheduler.scheduleMessage(new ConcreteMessage(scheduler, "groupID2", "msg3"));
			scheduler.applyScheduling();
			Thread.sleep(500);
			scheduler.applyScheduling();
			Thread.sleep(10000);

			scheduler.applyScheduling(new ConcreteMessage(scheduler, "groupID4", "msg1"));
			scheduler.applyScheduling(new ConcreteMessage(scheduler, "groupID3", "msg2"));

			Thread.sleep(500);
			scheduler.applyScheduling();
			Thread.sleep(500);
			scheduler.applyScheduling();

			scheduler.applyScheduling(new ConcreteMessage(scheduler, "groupID3", "msg3"));
			scheduler.applyScheduling(new ConcreteMessage(scheduler, "groupID4", "msg2"));
			Thread.sleep(500);
			scheduler.applyScheduling();

		} catch (Exception e) {
			// this should be logged or on a GUI show a dialog of the error
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	/**
	 * Run a simulation for Group Prioritisation
	 */
	public static void runGroupPriority() {
		System.out.println("Schedule Group Priority Execution");

		ResourceScheduler scheduler = new ResourceScheduler(2);
		// create groups with state and add to a GroupPriority object
		GroupPriority priority = new GroupPriority(scheduler.getGroups());
		scheduler.setPriority(priority);

		LinkedList<Message> list = new LinkedList<Message>();
		try {
			list.add(new ConcreteMessage(scheduler, "group2", "msg1"));
			list.add(new ConcreteMessage(scheduler, "group1", "msg1"));
			list.add(new ConcreteMessage(scheduler, "group3", "msg1"));
			list.add(new ConcreteMessage(scheduler, "group2", "msg2"));
			list.add(new ConcreteMessage(scheduler, "group2", "msg3"));
			list.add(new ConcreteMessage(scheduler, "group3", "msg2"));
			list.add(new ConcreteMessage(scheduler, "group1", "msg2"));

			scheduler.scheduleMessages(list);
			scheduler.applyScheduling();

			Thread.sleep(5000);
			scheduler.getGroups().put("group2", GroupStatus.CANCELLED);
			scheduler.scheduleMessage(new ConcreteMessage(scheduler, "group2", "msg4"));
			scheduler.scheduleMessage(new ConcreteMessage(scheduler, "group2", "msg5"));
			scheduler.applyScheduling();

			Thread.sleep(5000);
			scheduler.getGroups().put("group3", GroupStatus.TERMINATED);
			scheduler.scheduleMessage(new ConcreteMessage(scheduler, "group3", "msg3"));
			scheduler.scheduleMessage(new ConcreteMessage(scheduler, "group1", "msg3"));
			scheduler.scheduleMessage(new ConcreteMessage(scheduler, "group3", "msg4"));
			scheduler.scheduleMessage(new ConcreteMessage(scheduler, "group3", "msg5"));
			scheduler.applyScheduling();

		} catch (Exception e) {
			// this should be logged or on a GUI show a dialog of the error
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		runGroupPriority();
	}

}
