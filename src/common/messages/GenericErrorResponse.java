package common.messages;

public class GenericErrorResponse extends ServerResponse {

	private static final long serialVersionUID = 1L;

	public GenericErrorResponse() {
		super(MessageType.GENERICERROR, 0);
	}

}
