package com.psat.exercise;

/**
 * Implement this interface to be able to send messages to the resources
 *
 * @author Sérgio Teixeira
 * @date 16/06/2015 23:20:31
 */
public interface Gateway {

	/**
	 * Method to send a message to resources
	 *
	 * @param message
	 */
	public void send(Message message);
}
