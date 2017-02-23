package common.messages;

import java.io.Serializable;

public class ServerResponse implements Serializable, Response {

	private static final long serialVersionUID = 1L;
	private MessageType type;
	private final int code;
	
	public ServerResponse(MessageType type, int code) {
		this.setType(type);
		this.code= code;
	}

//	public ServerResponse(MessageType type, int code, Timeline timeline) {
//		super(type);
//		this.code = code;
//		getParam().add(timeline);
//	}
//	
//	public ServerResponse(MessageType type, int code, byte[] img) {
//		super(type);
//		this.code = code;
//		getParam().add(img);
//	}
	
	public MessageType getType() {
		return type;
	}

	public int getCode() {
		return code;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String render(){
		return "message of type " + this.getType().toString() + " returned with code " + this.getCode();
	}
	
}
