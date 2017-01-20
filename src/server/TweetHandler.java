package server;

import java.util.ArrayList;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.AckResponse;
import common.FinalTweet;
import common.MessageType;
import common.Timeline;
import common.Tweet;
import common.User;

public class TweetHandler extends Handler implements MessageListener, Runnable{

	public TweetHandler(String identity) {
		super(identity);
	}

	private JMSContext jmsContext;
	private Queue saveQueue;
	private Queue thumbnailQueue;
	
	@Override
	public void onMessage(Message msg) {

		print("onMessage");
		
		try {
			Queue replyToQueue = (Queue)msg.getJMSReplyTo();
			Tweet tweet = msg.getBody(Tweet.class);
			print(tweet.toString());
			
			String userID = tweet.getUsername();
			User user = Resources.RS.getUserById(userID);
			if(user != null) {
				if(tweet.getImage()==null){ //only message tweet
					ArrayList<User> followers = (ArrayList<User>)user.getFollowers();
					FinalTweet finalTweet = new FinalTweet(userID, tweet.getText(), null, "");
					for(User u : followers){
						Timeline timeline = u.getMytimeline();
						timeline.addTweet(finalTweet);
					}
				}else{ //contains image
					
					tweet.setImgName(""+ Resources.RS.getNewImgName() + "");
					jmsContext.createProducer().send(saveQueue, tweet); //save Image			
					jmsContext.createProducer().send(thumbnailQueue, tweet); //create thumbnail
					
				}
				jmsContext.createProducer().send(replyToQueue, new AckResponse(MessageType.TWEET, 1));	
			}else
				jmsContext.createProducer().send(replyToQueue, new AckResponse(MessageType.TWEET, 1));
			
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void run() {
		Context initialContext;
		try {
			initialContext = getContext();
			jmsContext = ((ConnectionFactory) initialContext.lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
			
			//lookup tweetQueue and set messageListener
			String tweetQueueName ="tweetQueue";
			Queue tweetQueue = (Queue) initialContext.lookup(tweetQueueName);
			jmsContext.createConsumer(tweetQueue).setMessageListener(this);
			
			//lookup saveQueue
			String saveQueueName ="saveQueue";
			saveQueue = (Queue) initialContext.lookup(saveQueueName);
			
			
			//lookup thumbnailQueue
			String thumbnailQueueName ="thumbnailQueue";
			thumbnailQueue = (Queue) initialContext.lookup(thumbnailQueueName);
			
		} catch (NamingException e) {
			e.printStackTrace();
		}

		print("started");
	}
	
	private Context getContext() throws NamingException {
		
		print("getContext");
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}
}
