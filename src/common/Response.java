package common;

public class Response extends Message {

	private final String msg;
	
	public Response(MessageType type, String msg) {
		super(type);
		this.msg = msg;
	}

	public Response(MessageType type, String msg, Timeline timeline) {
		super(type);
		this.msg = msg;
		getParam().add(timeline);
	}
	
	public Response(MessageType type, String msg, byte[] img) {
		super(type);
		this.msg = msg;
		getParam().add(img);
	}
	
	public String getMsg() {
		return msg;
	}
	
	
}
