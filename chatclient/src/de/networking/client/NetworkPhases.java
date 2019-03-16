package de.networking.client;

public enum NetworkPhases
{
	/**
	 * Key exchange process
	 */
	PRE0,
	
	/**
	 * Identifying process
	 */
	PRE1,
	
	/**
	 * Catch-up process
	 */
	PRE2,
	
	/**
	 * Communication process
	 */
	COM,
	
	/**
	 * Post process
	 */
	POST
}
