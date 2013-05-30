package de.cccs.filterretweeter;

import twitter4j.Status;


public interface Filter {
	/**
	 * Checks if the filter matches
	 * @param status the tweet to be matched
	 * @return true if the tweet is to be filtered (i.e. removed) 
	 */
	public boolean filterTweet(Status status);
	
	/**
	 * Notifies the filter that a status has been processed (i.e. retweeted)
	 * @param status the status that passed all filters
	 */
	public void notify(Status status);
}
