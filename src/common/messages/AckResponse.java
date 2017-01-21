package common.messages;

public class AckResponse extends ServerResponse implements Response{

	private static final long serialVersionUID = 1L;

	public AckResponse(MessageType type, int code) {
		super(type, code);
	}

}
