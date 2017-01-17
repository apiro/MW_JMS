package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Timeline implements Serializable{

	private static final long serialVersionUID = 1L;
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
