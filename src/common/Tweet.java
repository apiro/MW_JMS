package common;

import java.awt.Image;

public class Tweet {
	private final String user;
	private final Image image;
	private final String text;

	public Tweet(String user, Image image, String text) {
		this.user = user;
		this.image = image;
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public Image getImage() {
		return image;
	}

	public String getText() {
		return text;
	}
}
