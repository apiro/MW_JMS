package common;

import java.io.Serializable;

public class FinalTweet implements Serializable{

	private static final long serialVersionUID = 6510401251776173716L;
	
	private String userId;
	private String text;
	private byte[] thumbnail;
	private int imageName;
	
	
	public FinalTweet(String userId, String text) {
		this.userId = userId;
		this.text = text;
	}
	
	public FinalTweet(String userId, String text, byte[] thumbnail) {
		this.userId = userId;
		this.text = text;
		this.thumbnail = thumbnail;
	}
	
	public String getUserId() {
		return userId;
	}
	public String getText() {
		return text;
	}
	public byte[] getThumbnail() {
		return thumbnail;
	}
	public int getImageName() {
		return imageName;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	public void setImageName(int imageName) {
		this.imageName = imageName;
	}
	
	
}
