package de.cccs.filterretweeter.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import de.cccs.filterretweeter.Filter;


public class FilterBlockedUsers implements Filter {
    final static Logger logger = LoggerFactory.getLogger(FilterBlockedUsers.class);
    public final int INTERVAL = 15; // Update every 15 mins
    final Twitter twitter;
    List<Long> blockedIDs = new ArrayList<Long>();
    Date lastUpdate = null;

    public FilterBlockedUsers(Twitter twitter) {
        this.twitter = twitter;
    }

    void updateBlockList() {
        Date now = new Date();
        if (lastUpdate == null || (lastUpdate.getTime() + 60 * INTERVAL) < now.getTime()) {
            try {
                long ids[] = twitter.getBlocksIDs().getIDs();
                blockedIDs = new ArrayList<Long>(ids.length);
                for (long id : ids) {
                    blockedIDs.add(id);
                }
            } catch (TwitterException e) {
                logger.warn("Unable to get block list", e);
            }
            lastUpdate = now;
        }
    }

    @Override
    public boolean filterTweet(Status status) {
        updateBlockList();
        return blockedIDs.contains(status.getUser().getId());
    }

    @Override
    public void notify(Status status) {
    }
}
