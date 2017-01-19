package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
import common.MessageType;
import common.Request;

import javax.imageio.ImageIO;
import javax.jms.ConnectionFactory;

public class Client implements MessageListener {
	
	static String username;
	
	public static void main(String[] args) throws NamingException, IOException {
		
		System.out.println("> Starting Client ... ");
		
		String publishQueueName = "tweetQueue";
		String requestQueueName = "requestQueue";
		
		System.out.println("> first ... ");
		
		Context initialContext = Client.getContext(); 
		System.out.println("> second ... ");
		JMSContext jmsContext = ((ConnectionFactory) initialContext.lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
		
		System.out.println("> third ... ");
		Queue publishQueue = (Queue) initialContext.lookup(publishQueueName);
		Queue requestQueue = (Queue) initialContext.lookup(requestQueueName);
		Queue subscribeQueue = jmsContext.createTemporaryQueue();
		
		JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(subscribeQueue);
		
		jmsContext.createConsumer(subscribeQueue).setMessageListener(new Client());
		
		// User registration

		createRegistration(jmsProducer, requestQueue);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();	
        
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
		
		String fakeUserName = username;
		BufferedImage fakeImage = ImageIO.read(new File("resources/frank.png"));
		String fakeText = "fakeText";
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(fakeImage, "jpg", baos );
		byte[] fakeImageInByte=baos.toByteArray();
		
		Tweet tweet = new Tweet(fakeUserName, null, fakeText);
		
		System.out.println(tweet);
		
		jmsProducer.send(publishQueue, tweet);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
	}
	
	private static boolean createRegistration(JMSProducer jmsProducer, Queue requestQueue){
		username = "fakeUser";
		System.out.println("Invio richiesta");
		Request request = new Request(username, MessageType.SUBSCRIBE);
		jmsProducer.send(requestQueue, request);
		System.out.println("Richiesta Inviata");
		return true;
	}

	@Override
	public void onMessage(Message msg) {
		try {
			System.out.println(msg.getBody(String.class));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void renderTimeline(Timeline timeline) {
		// TODO Auto-generated method stub
		
	}

}
