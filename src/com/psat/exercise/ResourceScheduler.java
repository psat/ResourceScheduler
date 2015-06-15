package com.psat.exercise;

/**
 * @author Paulo Teixeira
 * @date 15/06/2015 22:40:04
 */
public class ResourceScheduler {

	/**
	 * Counter for keeping the currently assigned resources
	 */
	private int	assignedResources;

	/**
	 * Counter for keeping track of currently working resources, i.e, not idle
	 * <p>
	 * Setter for this attribute was left un-implemented. Must use
	 * {@link #putResourceWorking()} and {@link #putResourceIdle()} methods to
	 * modify this attribute.
	 */
	private int	workingResources;	// setter for this attribute was left
									// unimplemented - should use re

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
		assignedResources = resources;
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
	public int putResourceWorking() {
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
	public int putResourceIdle() {
		if (workingResources > 0) {
			workingResources -= 1;
		}

		return assignedResources - workingResources;
	}

	public static void main(String[] args) {
		// not sure if a main is needed
		// but just in case we need to run as standalone.
	}

}
