package com.psat.exercise;

/**
 * Interface to provide the means for communicating that a message was completed
 *
 * @author Sérgio Teixeira
 * @date 16/06/2015 00:25:22
 */
public interface Message {

	/**
	 * When a Message has completed processing, this method should be called:
	 */
	public void completed();

	/**
	 * The message's group id.
	 * <p>
	 * <b> Implementation should not return a <code>null</code> value</b>
	 *
	 * @return the groupID
	 */
	public String getGroupID();

}
