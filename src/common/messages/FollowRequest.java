package common.messages;

import java.util.ArrayList;

public class FollowRequest extends ClientRequest {

	private static final long serialVersionUID = 1L;
	ArrayList<String> users = null;

	public FollowRequest(String username, ArrayList<String> users) {
		super(username, MessageType.FOLLOW);
		this.setUsers(users);
	}

	public ArrayList<String> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}

}
