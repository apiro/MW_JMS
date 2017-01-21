package common.messages;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import common.FinalTweet;
import common.Timeline;

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
	
	@Override
	public String render(){
		
		List<FinalTweet> tweets = timeline.getTweets();
		
		for (FinalTweet ft : tweets){
			System.out.print(ft.toString());
			if (ft.getThumbnail() != null){
				
				try {
					ByteArrayInputStream bais = new ByteArrayInputStream(ft.getThumbnail());
					Image image = ImageIO.read(bais);
					JFrame frame = new JFrame(ft.getUserId()+" - "+ft.getImageName());
					frame.setSize(image.getWidth(null), image.getHeight(null));
					JLabel label = new JLabel(new ImageIcon(image));
					frame.add(label);
					frame.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return super.render();
	}
}
