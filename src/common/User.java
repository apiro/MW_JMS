package common;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String id;
	private final List<User> followers;
	private final Timeline mytimeline;
	
	public User(String id) {
		this.id = id;
		followers = new ArrayList<User>();		
		mytimeline = new Timeline();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<User> getFollowers() {
		return followers;
	}
	
	public void addFollower(User follower){
		followers.add(follower);
	}

	public Timeline getMytimeline() {
		return mytimeline;
	}
	
	
	
}
