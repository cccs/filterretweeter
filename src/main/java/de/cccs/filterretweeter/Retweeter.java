package de.cccs.filterretweeter;

import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class Retweeter {
	final Query query;
	private Date lastUpdate = new Date();
	
	public Retweeter(String query) {
		this.query = new Query(query);
	}

	/**
	 * Check a single tweet against filters.
	 * If filters pass, retweet and update filters.
	 * 
	 * @param twitter
	 * @param status
	 * @param filters
	 * @throws TwitterException
	 */
	public void checkRetweet(Twitter twitter, Status status, List<Filter> filters) throws TwitterException {
		// Check if tweet should be filtered
		for (Filter filter: filters) {
			if (filter.filterTweet(status)) return;
		}
		// Do retweet
		twitter.retweetStatus(status.getId());
		// Notify filters
		for (Filter filter: filters) {
			filter.notify(status);
		}
	}


	/**
	 * Do a single run: Do query, iterate over all results, process
	 * tweets which are newer than the last run and are no retweets by themselves.
	 * 
	 * @param twitter
	 * @param filters
	 */
	public void run(Twitter twitter, List<Filter> filters) {
	    QueryResult result;
		try {
			result = twitter.search(query);
		    for (Status status : result.getTweets()) {
		    	if (!status.isRetweet() && lastUpdate.before(status.getCreatedAt())) {
		    		checkRetweet(twitter, status, filters);
		    	}
		    }
		} catch (TwitterException e) {
			System.err.println("Problem with search: " + e);
		}	    
	    lastUpdate=new Date();
	}
}
