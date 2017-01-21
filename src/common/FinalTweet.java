package common;

import java.io.Serializable;

public class FinalTweet implements Serializable{

	private static final long serialVersionUID = 6510401251776173716L;
	
	private String userId;
	private String text;
	private byte[] thumbnail;
	private String imageName;

	public FinalTweet(String userId, String text, byte[] thumbnail, String imageName) {
		this.setImageName(imageName);
		this.setText(text);
		this.setThumbnail(thumbnail);
		this.setUserId(userId);
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
	public String getImageName() {
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
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		return "[User: " + userId + "] -  " + text + " --> imgRef: " + imageName + "\n";
	}
	
	
}
