package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

import common.Tweet;

import common.TimelineRequest;
import common.ClientRequest;
import common.FollowRequest;
import common.ImageRequest;
import common.RegistrationRequest;

import common.ServerResponse;

import javax.imageio.ImageIO;
import javax.jms.ConnectionFactory;

public class Client implements MessageListener {
	
	static String username = null;
	static Boolean control = true;
	static Tweet tweet = null;
	static byte[] fakeImageInByte = null;
	
	public static void main(String[] args) throws NamingException, IOException {
		
		print("starting");
		
		String tweetQueueName = "tweetQueue";
		String requestQueueName = "requestQueue";
		
		Context initialContext = Client.getContext(); 
		JMSContext jmsContext = ((ConnectionFactory) initialContext.lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
		
		Queue tweetQueue = (Queue) initialContext.lookup(tweetQueueName);
		Queue requestQueue = (Queue) initialContext.lookup(requestQueueName);
		Queue subscribeQueue = jmsContext.createTemporaryQueue();
		
		JMSProducer jmsProducer = jmsContext.createProducer().setJMSReplyTo(subscribeQueue);
		
		jmsContext.createConsumer(subscribeQueue).setMessageListener(new Client());
		
		interact(requestQueue, tweetQueue, jmsProducer);
		
	}

	private static void initTweetTemplate() throws IOException {
		
		print("initTweetTemplate");
		
		BufferedImage fakeImage = ImageIO.read(new File("resources/frank.png"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(fakeImage, "jpg", baos );
		fakeImageInByte = baos.toByteArray();
		
		tweet = new Tweet(username, fakeImageInByte, null);
	}

	private static void interact(Queue requestQueue, Queue tweetQueue, JMSProducer jmsProducer) throws IOException {
		
		print("interact");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		print("Type your username: ");
		username = br.readLine();
		
		while(control) {
			
			print("Which action do you want to perform ?");
			print("[1] Register | [2] Send Tweet | [3] RequestImage | [4] Follow | [5] RequestTimeline | DEFAULT QUITS");
			
			if(username == null) {
				print("Type your username: ");
				username = br.readLine();
			}
			
	        String choice = br.readLine();	
	        ClientRequest request = null;
	      
	        switch(choice) {
	        	case "1": 
	        		request = new RegistrationRequest(username);
	        		sendRequest(jmsProducer, requestQueue, request);
	        		break;
	        	case "2":
	        		initTweetTemplate();
					print("Type tweet caption: ");
					String caption = br.readLine();
					
					tweet.setText(caption);
					print(tweet.toString() + " is going to be sent");
					sendRequest(jmsProducer, tweetQueue, tweet);
					break;
	        	case "3":
	        		print("Type the image ID: ");
	        		String imageID = br.readLine();
	        		
	        		request = new ImageRequest(username, imageID);
	        		sendRequest(jmsProducer, requestQueue, request);
	        		break;
	        	case "4":
	        		print("Type the users you want to follow (space separated): ");
	        		String[] followUsers = null;
	        		String input = br.readLine();
	        		
	        		followUsers = input.split("\\s+");
	        		request = new FollowRequest(username, new ArrayList<String>(Arrays.asList(followUsers)));
	        		sendRequest(jmsProducer, requestQueue, request);
	        		break;
	        	case "5":
	        		request = new TimelineRequest(username);
	        		sendRequest(jmsProducer, requestQueue, request);
	        		break;
	        	default :
	        		control = false;
	        		break;
	        }
		}
		print("closing");
		
	}

	private static boolean sendRequest(JMSProducer jmsProducer, Queue requestQueue, ClientRequest request) {
		
		print("sendRequest - " + request.getType().toString());
		
		print("> Sending request");
		
		jmsProducer.send(requestQueue, request);
		
		print("> Request sent");
		return true;
		
	}

	private static Context getContext() throws NamingException {
		
		print("getContext");
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}

	@Override
	public void onMessage(Message msg) {
		
		print("onMessage");
		try {
			
			renderResponse(msg.getBody(ServerResponse.class));
			
		} catch (JMSException e) {

			e.printStackTrace();
			
		}
	}

	private void renderResponse(ServerResponse body) {
		
		print("renderResponse");
		print(body.render());
	}

	public static void print(String s) {
		System.out.println("> Client > " + s);
	}
	
}
