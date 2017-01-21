package common.messages;

public class TimelineRequest extends ClientRequest {

	private static final long serialVersionUID = 1L;
	
	public TimelineRequest(String username) {
		super(username, MessageType.TIMELINE);
	}

}
