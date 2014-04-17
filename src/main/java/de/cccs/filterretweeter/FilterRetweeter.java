package de.cccs.filterretweeter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.*;
import de.cccs.filterretweeter.filters.FilterAlreadyRetweeted;
import de.cccs.filterretweeter.filters.FilterBlockedUsers;
import de.cccs.filterretweeter.filters.FilterUsernames;


public class FilterRetweeter {
  static final int DELAY = 5;

	public static void main(String[] args) {
		System.out.println("Filter Retweeter");

        Twitter twitter = TwitterFactory.getSingleton();
        try {
            System.out.println("Logged in as " + twitter.getAccountSettings().getScreenName());
        } catch (Exception e) {
            System.out.println("Unable to log in: " + e);
            System.exit(1);
        }
        TwitterStreamFactory streamFactory = new TwitterStreamFactory();

		List<Filter> filterList = new ArrayList<Filter>();
		filterList.add(new FilterAlreadyRetweeted());
		filterList.add(new FilterBlockedUsers(twitter));
		filterList.add(new FilterUsernames("conferencecat\\d*", "confere\\d*", "gpncat\\d*", "gpnation"));

		List<Retweeter> retweeters = new ArrayList<Retweeter>();
		retweeters.add(new Retweeter(twitter, streamFactory, filterList, "#eh14"));
		retweeters.add(new Retweeter(twitter, streamFactory, filterList, "#eh2014"));
		retweeters.add(new Retweeter(twitter, streamFactory, filterList, "#easterhegg"));

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
