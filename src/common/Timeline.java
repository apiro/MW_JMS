package common;

import java.util.ArrayList;
import java.util.List;

public class Timeline {
	private List<Tweet> tweets;
	
	public Timeline() {
		tweets = new ArrayList<Tweet>();
	}
	
	public List<Tweet> getTweets() {
		return tweets;
	}
	
	public void addTweet(Tweet tweet){
		tweets.add(tweet);
	}
}
