package common.messages;

public class ImageResponse extends ServerResponse {

	private static final long serialVersionUID = 1L;
	private byte[] image = null;

	public ImageResponse(MessageType type, int code, byte[] image) {
		super(type, code);
		this.setImage(image);
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

}
