package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Timeline implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<FinalTweet> tweets;
	
	public Timeline() {
		tweets = new ArrayList<FinalTweet>();
	}
	
	public List<FinalTweet> getTweets() {
		return tweets;
	}
	
	public void addTweet(FinalTweet tweet){
		tweets.add(tweet);
	}
}
