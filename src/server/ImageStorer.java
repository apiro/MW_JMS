package server;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
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
	private JMSConsumer consumer;
	private int count = 0;

	@Override
	public void onMessage(Message msg) {
		
		print("onMessage");
		count++;
		
		try {
			
			Tweet tweet = msg.getBody(Tweet.class);
			//print(tweet.toString());
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
			consumer = jmsContext.createConsumer(saveQueue);
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
