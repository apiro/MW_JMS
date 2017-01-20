package common;

public class RegistrationRequest extends ClientRequest {

	private static final long serialVersionUID = 1L;

	public RegistrationRequest(String username) {
		super(username, MessageType.SUBSCRIBE);
	}

}
