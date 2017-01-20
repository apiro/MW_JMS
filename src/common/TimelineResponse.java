package common;

public class TimelineResponse extends ServerResponse {

	private static final long serialVersionUID = 1L;
	private Timeline timeline = null;

	public TimelineResponse(MessageType type, int code, Timeline timeline) {
		super(type, code);
		this.setTimeline(timeline);
	}

	public Timeline getTimeline() {
		return timeline;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}
}
