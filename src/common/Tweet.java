package common;

import java.io.Serializable;

import common.messages.ClientRequest;
import common.messages.MessageType;

public class Tweet extends ClientRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final byte[] image;
	private String text;
	private String imgName; //will be filled by server before inserting in image queues.

	public Tweet(String username, byte[] image, String text) {
		super(username, MessageType.TWEET);
		this.setText(text);
		this.image = image;
		this.setImgName("");
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	@Override
	public String toString() {
		return "Tweet [user=" + super.getUsername() + ", text=" + this.getText() + "]";
	}


}
