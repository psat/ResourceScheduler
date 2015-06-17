package com.psat.exercise.prioritisation;

import java.util.LinkedList;

import com.psat.exercise.Message;

/**
 * Implementation of a FIFO (First In First Out) prioritisation logic
 *
 * @author Sérgio Teixeira
 * @date 17/06/2015 00:35:22
 */
public class FIFOPriority implements Priority {

	@Override
	public Message pickNextPriorityMessage(LinkedList<Message> list) {
		return list.pollLast();
	}
}
