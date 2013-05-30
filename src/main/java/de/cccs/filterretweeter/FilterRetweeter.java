package de.cccs.filterretweeter;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class FilterRetweeter {
	public static void main(String[] args) {
		System.out.println("Filter Retweeter");
		FilterRetweeter main = new FilterRetweeter();
		
		Twitter twitter = TwitterFactory.getSingleton();
		try {
			Query query = new Query("#gpn13");
		    QueryResult result = twitter.search(query);	    
		    for (Status status : result.getTweets()) {
		    	if (!status.isRetweet())
		    		System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText()); else
			    		System.out.println("Retweet... @" + status.getUser().getScreenName() + ":" + status.getText()); 
		    }
		} catch (TwitterException e) {
			System.err.println(e);
		}
	}
}
