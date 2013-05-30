package de.cccs.filterretweeter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import de.cccs.filterretweeter.filters.FilterAlreadyRetweeted;
import de.cccs.filterretweeter.filters.FilterBlockedUsers;
import de.cccs.filterretweeter.filters.FilterUsernames;


public class FilterRetweeter {
	public static void main(String[] args) {
		System.out.println("Filter Retweeter");
		
		Twitter twitter = TwitterFactory.getSingleton();
		List<Filter> filterList = new ArrayList<Filter>();
		filterList.add(new FilterAlreadyRetweeted());
		filterList.add(new FilterBlockedUsers(twitter));
		filterList.add(new FilterUsernames("conferencecat\\d*", "confere\\d*", "gpncat\\d*"));
		List<Retweeter> retweeters = new ArrayList<Retweeter>();
		retweeters.add(new Retweeter("#gpn13"));
		retweeters.add(new Retweeter("#gpn"));

		while(true) {
			System.out.println("Checking for tweets...");
			for (Retweeter r: retweeters) {
				r.run(twitter, filterList);
			}
			System.out.println("Checking done. Sleeping...");
			try {
				Thread.sleep(1000*120);
			} catch (InterruptedException e) { }
		}
	}
}
