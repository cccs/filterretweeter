package de.cccs.filterretweeter.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import twitter4j.Status;
import de.cccs.filterretweeter.Filter;


public class FilterUsernames implements Filter {
	final List<Pattern> patterns;
	
	public FilterUsernames(String... names) {
		patterns = new ArrayList<Pattern>(names.length);
		for (String name: names) {
			patterns.add(Pattern.compile("^" + name + "$", Pattern.CASE_INSENSITIVE));
		}
	}
	
	@Override
	public boolean filterTweet(Status status) {
		for (Pattern pattern: patterns) {
			if (pattern.matcher(status.getUser().getScreenName()).find()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void notify(Status status) {
	}
}
 