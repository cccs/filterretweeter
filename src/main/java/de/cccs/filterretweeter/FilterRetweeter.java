package de.cccs.filterretweeter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import de.cccs.filterretweeter.filters.FilterAlreadyRetweeted;
import de.cccs.filterretweeter.filters.FilterBlockedUsers;
import de.cccs.filterretweeter.filters.FilterUsernames;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


public class FilterRetweeter {
  static final int DELAY = 5;

	public static void main(String[] args) {
		System.out.println("Filter Retweeter");

		Twitter twitter = TwitterFactory.getSingleton();
		List<Filter> filterList = new ArrayList<Filter>();
		filterList.add(new FilterAlreadyRetweeted());
		filterList.add(new FilterBlockedUsers(twitter));
		filterList.add(new FilterUsernames("conferencecat\\d*", "confere\\d*", "gpncat\\d*", "gpnation"));
		List<Retweeter> retweeters = new ArrayList<Retweeter>();
		retweeters.add(new Retweeter(twitter, filterList, "#eh14"));
		retweeters.add(new Retweeter(twitter, filterList, "#eh2014"));

        // Setup stream
        TwitterStreamFactory streamFactory = new TwitterStreamFactory();
        TwitterStream stream = streamFactory.getInstance();
        for (Retweeter r: retweeters) {
            stream.addListener(r);
        }
        stream.sample();

        // Fallback: Do search
        while(true) {
			System.out.println("Checking for tweets...");
			for (Retweeter r: retweeters) {
				r.runSearch();
			}
			System.out.println("Checking done. Sleeping " + DELAY + " minutes...");
			try {
				Thread.sleep(1000*60*DELAY);
			} catch (InterruptedException e) { }
		}
	}
}
