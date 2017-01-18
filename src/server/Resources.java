package server;

import java.util.HashMap;

import common.User;

//The main storage in the server, that is a singleton (static) with all the users subscribed
public class Resources {
	public static Resources RS = new Resources();
	
	private HashMap<String, User> users;
	private HashMap<String, byte[]> images;
	
	public Resources() {
		users = new HashMap<String, User>();
	}
	
	// return 1 if correctly inserted, 0 if already exists
	public boolean addUser(User user){
		String id = user.getId();
		if (getUserById(id) != null)
			return false;
		else
			users.put(id, user);
		return true;
	}
	
	public User getUserById(String id){
		return users.get(id);
	}
	
	public void saveImage(String name, byte[] image){
		images.put(name, image);
	}
	
	public byte[] getImage(String name){
		return images.get(name);
	}
}
