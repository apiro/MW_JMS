package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.Timeline;
import common.Tweet;

import javax.imageio.ImageIO;
import javax.jms.ConnectionFactory;

public class Client implements MessageListener {
	
	public static void main(String[] args) throws NamingException, IOException {
		
		System.out.println("> Starting Client ... ");
		
		String publishQueueName = "tweetQueue";
		
		Context initialContext = Client.getContext(); 
		JMSContext jmsContext = ((ConnectionFactory) initialContext.lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
		
		Queue publishQueue = (Queue) initialContext.lookup(publishQueueName);
		Queue subscribeQueue = jmsContext.createTemporaryQueue();
		
		JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(subscribeQueue);
		
		sendTweet(jmsProducer, publishQueue);
		
	}

	private static Context getContext() throws NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		return new InitialContext(props);
	}
	
	private static void sendTweet(JMSProducer jmsProducer, Queue publishQueue) throws IOException {
		
		String fakeUserName = "fakeUserName";
		BufferedImage fakeImage = ImageIO.read(new File("resources/frank.png"));
		String fakeText = "fakeText";
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(fakeImage, "jpg", baos );
		byte[] fakeImageInByte=baos.toByteArray();
		
		Tweet tweet = new Tweet(fakeUserName, fakeImageInByte, fakeText);
		
		System.out.println(tweet);
		
		jmsProducer.send(publishQueue, tweet);
	}

	@Override
	public void onMessage(Message msg) {
		try {
			Timeline timeline = msg.getBody(Timeline.class);
			this.renderTimeline(timeline);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void renderTimeline(Timeline timeline) {
		// TODO Auto-generated method stub
		
	}

}
