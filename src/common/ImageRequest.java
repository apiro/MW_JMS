package common;

public class ImageRequest extends ClientRequest {

	private static final long serialVersionUID = 1L;
	private String imageID;

	public ImageRequest(String username, String imageID) {
		super(username, MessageType.IMAGE);
		this.setImageID(imageID);
	}

	public String getImageID() {
		return imageID;
	}

	public void setImageID(String imageID) {
		this.imageID = imageID;
	}

}
