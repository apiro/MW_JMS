package server;

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

import common.Tweet;

public class ImageStorer extends Handler implements MessageListener, Runnable {

	public ImageStorer(String identity) {
		super(identity);
	}

	private JMSContext jmsContext;

	@Override
	public void onMessage(Message msg) {
		
		print("onMessage");
		
		try {
			
			Tweet tweet = msg.getBody(Tweet.class);
			print(tweet.toString());
			Resources.RS.saveImage(tweet.getImgName(), tweet.getImage());
			print("image saved");
	
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
			
			//lookup saveQueue
			String saveQueueName ="saveQueue";
			Queue saveQueue = (Queue) initialContext.lookup(saveQueueName);
			jmsContext.createConsumer(saveQueue).setMessageListener(this);
			
		} catch (NamingException e) {
			e.printStackTrace();
		}		
		
		print("started");
	}
	
	private Context getContext() throws NamingException {
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}
}
