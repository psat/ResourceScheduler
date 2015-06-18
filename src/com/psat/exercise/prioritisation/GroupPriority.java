package com.psat.exercise.prioritisation;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.psat.exercise.Message;
import com.psat.exercise.ResourceScheduler;
import com.psat.exercise.ResourceScheduler.GroupStatus;

/**
 * Implementation of a Group prioritisation logic
 *
 * @author Sérgio Teixeira
 * @date 17/06/2015 13:22:52
 */
public class GroupPriority implements Priority {

	/**
	 * LinkedHashMap to keep track of the groups Status and to keep order of
	 * arrival of groups
	 */
	LinkedHashMap<String, GroupStatus>	groups;

	/**
	 * Making the default constructor private - Do not want to create
	 * {@link GroupPriority} objects
	 * without reference to the group:state mapping kept by the
	 * {@link ResourceScheduler}
	 */
	@SuppressWarnings("unused")
	private GroupPriority() {
		throw new AssertionError();
	}

	/**
	 * Creates a {@link GroupPriority} instance with a reference to the mapping
	 * of group:state that should be provided by the {@link ResourceScheduler}
	 */
	public GroupPriority(LinkedHashMap<String, GroupStatus> groups) throws NullPointerException {
		if (groups == null) {
			throw new NullPointerException("Groups LinkedHashMap is null");
		}

		this.groups = groups;
	}

	/**
	 * Returned message should be firstly from a group that has already in
	 * progress.
	 * Otherwise the priority in which to process groups is defined by the order
	 * in which you receive the first message from the group.<br>
	 * If the resources
	 * are idle and there is no more messages for the first group then can
	 * process another group of messages
	 *
	 * @throws Exception
	 */
	@Override
	public Message pickNextPriorityMessage(LinkedList<Message> list) {
		Message pMessage = null;

		for (Message message : list) {
			String groupID = message.getGroupID();
			GroupStatus status = groups.get(groupID);

			// if added keep message temporarily
			if (GroupStatus.IN_PROGRESS.equals(status) || GroupStatus.CANCELLED.equals(status)
					|| GroupStatus.TERMINATED.equals(status)) {
				pMessage = message;
				break;

			} else if (GroupStatus.ADDED.equals(status) && pMessage == null) {
				pMessage = message;
			} else {
				// it has not been added to the groups for execution
				groups.put(groupID, GroupStatus.ADDED);
				// this is the first group arriving, so process it
				if (groups.size() == 1) {
					pMessage = message;
					break;
				}
			}
		}

		list.remove(pMessage);
		return pMessage;
	}
}
