package de.cccs.filterretweeter.filters;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import de.cccs.filterretweeter.Filter;


public class FilterAlreadyRetweeted implements Filter {
	protected List<Long> retweetedIds = new ArrayList<Long>();

	@Override
	public boolean filterTweet(Status status) {
		return retweetedIds.contains(status.getId());
	}

	@Override
	public void notify(Status status) {
		retweetedIds.add(status.getId());
	}
}
