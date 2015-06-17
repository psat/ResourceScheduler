package com.psat.exercise.prioritisation;

import java.util.LinkedList;

import com.psat.exercise.Message;

/**
 * Interface to provide a way of defining prioritisation logic to be applied
 * when choosing the next message to be sent
 *
 * @author Sérgio Teixeira
 * @date 17/06/2015 00:27:50
 */
public interface Priority {

	/**
	 * Must implement logic prioritisation and return the {@link Message} that
	 * matches the priority conditions
	 *
	 * @param list
	 *            of messages to apply strategy
	 * @return next prioritised message
	 */
	public Message pickNextPriorityMessage(LinkedList<Message> list);
}
