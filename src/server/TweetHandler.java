package server;

import java.util.ArrayList;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.FinalTweet;
import common.Timeline;
import common.Tweet;
import common.User;
import common.messages.AckResponse;
import common.messages.MessageType;

public class TweetHandler extends Handler implements MessageListener, Runnable{

	public TweetHandler(String identity) {
		super(identity);
	}

	private JMSContext jmsContext;
	private Queue saveQueue;
	private Queue thumbnailQueue;
	private JMSConsumer consumer;
	private int count = 0;
	private JMSProducer producer;
	
	@Override
	public void onMessage(Message msg) {

		print("onMessage");
		
		try {
			Queue replyToQueue = (Queue)msg.getJMSReplyTo();
			Tweet tweet = msg.getBody(Tweet.class);
			//print(tweet.toString());
			count++;
			
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
					user.getMytimeline().addTweet(finalTweet); //add my tweets to my timeline
				}else{ //contains image
					
					tweet.setImgName(""+ Resources.RS.getNewImgName() + "");
					producer.send(saveQueue, tweet); //save Image			
					producer.send(thumbnailQueue, tweet); //create thumbnail
					
				}
				producer.send(replyToQueue, new AckResponse(MessageType.TWEET, 1));	
			}else
				producer.send(replyToQueue, new AckResponse(MessageType.TWEET, 1));
			
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
			consumer = jmsContext.createConsumer(tweetQueue);
			
			//lookup saveQueue
			String saveQueueName ="saveQueue";
			saveQueue = (Queue) initialContext.lookup(saveQueueName);
			
			//lookup thumbnailQueue
			String thumbnailQueueName ="thumbnailQueue";
			thumbnailQueue = (Queue) initialContext.lookup(thumbnailQueueName);
			
			producer = jmsContext.createProducer();
			consumer.setMessageListener(this);
			
		} catch (NamingException e) {
			e.printStackTrace();
		}

		print("started");
	}
	
	public void stopListening(){
		print("Stopping by browser, elaborated " + count);
		consumer.close();
	}
	
	private Context getContext() throws NamingException {
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}
}
