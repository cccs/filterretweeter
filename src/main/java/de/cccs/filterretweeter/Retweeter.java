package de.cccs.filterretweeter;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class Retweeter {
	final static Logger logger = LoggerFactory.getLogger(Retweeter.class);
	final Query query;
	private Date lastUpdate = new Date();

	public Retweeter(String query) {
		this.query = new Query(query).count(30);
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
			if (filter.filterTweet(status)) {
				logger.info("--- Skipping tweet @" + status.getUser().getScreenName() + ":" + status.getText());
				return;
			}
		}
		// Do retweet
		logger.info("+++ Retweeting @" + status.getUser().getScreenName() + ":" + status.getText());
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
		} catch (TwitterException e) {
			logger.warn("Problem with search: ", e);
      return;
		}
    List<Status> tweets = result.getTweets();
    logger.info("Query " + query.getQuery() + " returned " + tweets.size() + " tweets");
    for (Status status : tweets) {
      if (!status.isRetweet() && !status.isRetweetedByMe() /*&& lastUpdate.before(status.getCreatedAt())*/) {
        try {
          checkRetweet(twitter, status, filters);
        } catch (TwitterException e) {
          logger.warn("Unable to retweet status (skipping it): ", e);
        }
      }
    }
    lastUpdate=new Date();
	}
}
