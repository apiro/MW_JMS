package common;

import java.io.Serializable;

public class Requests implements Serializable{
	
	private static final long serialVersionUID = -4116537866638364877L;
	private String userID;
	private MessageType reqType; // TIMELINE if request for timeline
	private int imgName;
	private String userToFollow;
						 // <followed name> if requests to follow
	
	public Requests(String userID, MessageType type, int name) {
		super();
		this.userID = userID;
		this.reqType = type;
		this.imgName = name;
	}
	
	public Requests(String userID, MessageType type, String user) {
		super();
		this.userID = userID;
		this.reqType = type;
		this.userToFollow = user;
	}
	
	public Requests(String userID, MessageType type) {
		super();
		this.userID = userID;
		this.reqType = type;
	}
	
	public String getUserID() {
		return userID;
	}
	public MessageType getType() {
		return reqType;
	}
	public String getUserToFollow() {
		return userToFollow;
	}
	public int getImgName() {
		return imgName;
	}

}
