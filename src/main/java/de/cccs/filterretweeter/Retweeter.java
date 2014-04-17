package de.cccs.filterretweeter;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.*;


public class Retweeter implements StatusListener {
    final static Logger logger = LoggerFactory.getLogger(Retweeter.class);
    final Twitter twitter;
    List<Filter> filters;
    final Query query;
    private Date lastUpdate = new Date();


    public Retweeter(Twitter twitter, List<Filter> filters, String query) {
        this.twitter = twitter;
        this.filters = filters;
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
    public void checkRetweet(Twitter twitter, Status status, List<Filter> filters) {
        // Check if tweet should be filtered
        for (Filter filter : filters) {
            if (filter.filterTweet(status)) {
                logger.info("--- Skipping tweet @" + status.getUser().getScreenName() + ":" + status.getText());
                return;
            }
        }
        // Do retweet
        logger.info("+++ Retweeting @" + status.getUser().getScreenName() + ":" + status.getText());
        try {
            twitter.retweetStatus(status.getId());
        } catch (TwitterException e) {
            logger.warn("Unable to retweet status (skipping it): ", e);
        }
        // Notify filters
        for (Filter filter : filters) {
            filter.notify(status);
        }
    }


    /**
     * Process tweets which are newer than the last run and are no retweets by themselves.
     */
    protected void processStatus(Status status) {
        if (!status.isRetweet() && !status.isRetweetedByMe() /*&& lastUpdate.before(status.getCreatedAt())*/) {
            checkRetweet(twitter, status, filters);
        }
    }


    /**
     * Do a single run: Do query, iterate over all results, process them
     */
    public void runSearch() {
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
            processStatus(status);
        }
        lastUpdate = new Date();
    }


    @Override
    public void onStatus(Status status) {
        processStatus(status);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
    }

    @Override
    public void onStallWarning(StallWarning warning) {
    }

    @Override
    public void onException(Exception ex) {
    }
}
